package mwojcik.rss_customizer.rss.application

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import io.circe.{Json, Printer}
import mwojcik.rss_customizer.rss.domain.FeedRouter
import mwojcik.rss_customizer.rss.feeds.Dilbert
import pureconfig.loadConfig

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.language.postfixOps

object Application extends App with StrictLogging{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  private val jsonToStringWithoutNullValuesPrinter = Printer(preserveOrder = true, dropNullKeys = true, indent = "").pretty _
  implicit val printer: Json => String = jsonToStringWithoutNullValuesPrinter

  val config: RssCustomizerConfig = {
    loadConfig[RssCustomizerConfig] match {
      case Left(failures) =>
        throw new ExceptionInInitializerError(s"Unable to resolve configuration: $failures")
      case Right(conf) => conf
    }
  }

  try {
    logger.info("Application starting")

    val feedRouter = new FeedRouter(List(new Dilbert))

    val routes: Route = {
     feedRouter.routes
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
