package mwojcik.rss_customizer.rss.application

import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.StrictLogging

/**
  * Dummy Health-Check.
  */
class HealthCheck extends StrictLogging {

  def routes = rejectEmptyResponse {
    pathPrefix("health-check") {
      complete {
        logger.debug("Healthy :)")
        "OK"
      }
    }
  }

}
