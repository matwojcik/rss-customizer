package mwojcik.rss_customizer.rss.domain

import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.StrictLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Json
import mwojcik.rss_customizer.rss.feeds.FeedFactory
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext
import scala.xml.Unparsed

class FeedRouter(feedFactories: List[FeedFactory])(implicit printer: Json => String, ec: ExecutionContext) extends FailFastCirceSupport with StrictLogging with ScalaXmlSupport {

  private val feedMap = feedFactories.groupBy(_.name).mapValues(_.head)

  def routes = rejectEmptyResponse {
    pathPrefix("feed") {
      path(Segment) { name: String =>
        logger.debug(s"Serving feed $name")
        complete {
          feedMap.get(name).map { feedFactory =>
            val feed = feedFactory.create
            logger.debug(s"Feed items: ${feed.items.map(_.title)}")
            logger.trace(s"$feed")

            
            <rss version="2.0">
              <channel>
                <title>
                  {feed.title}
                </title>
                <link>
                  {feed.link}
                </link>
                <lastBuildDate>{DateTime.now().toDateTimeISO}</lastBuildDate>
                <pubDate>{DateTime.now().toDateTimeISO}</pubDate>
                {for (item <- feed.items) yield {
                    <item>
                      <title>
                        {item.title}
                      </title>
                      <description>
                        {Unparsed(item.description)}
                      </description>
                      <link>
                        {item.link}
                      </link>
                    </item>
                  }
                }

              </channel>
            </rss>

          }
        }
      }
    }
  }
}
