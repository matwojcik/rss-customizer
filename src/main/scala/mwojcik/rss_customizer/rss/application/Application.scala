package mwojcik.rss_customizer.rss.application

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import mwojcik.rss_customizer.rss.domain.FeedRouter
import mwojcik.rss_customizer.rss.feeds.{Dilbert, ScalaTimes}
import pureconfig.loadConfig

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Application extends App with StrictLogging {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  try {
    logger.info("Application starting")

    val config: RssCustomizerConfig = {
      loadConfig[RssCustomizerConfig] match {
        case Left(failures) =>
          throw new ExceptionInInitializerError(s"Unable to resolve configuration: $failures")
        case Right(conf) => conf
      }
    }

    val feedRouter = new FeedRouter(List(new Dilbert, new ScalaTimes))
    val healthCheck = new HealthCheck()

    val routes: Route = {
      feedRouter.routes ~ healthCheck.routes
    }

    Http().bindAndHandle(routes, config.http.host, config.http.port)

    logger.info(s"Started handling requests on ${config.http.address}")
  } catch {
    case t: Throwable =>
      logger.error("Error occurred during app initialisation", t)
      throw t
  }

  scala.sys.addShutdownHook {
    logger.info("Shutting down actor system...")
    system.terminate()
    Await.result(system.whenTerminated, 30 seconds)
  }
}
