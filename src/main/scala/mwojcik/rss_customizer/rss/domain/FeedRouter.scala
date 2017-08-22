package mwojcik.rss_customizer.rss.domain

import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.StrictLogging
import mwojcik.rss_customizer.rss.feeds.FeedFactory
import mwojcik.rss_customizer.rss.marshallers.RssXmlMarshaller

import scala.concurrent.ExecutionContext
import scala.xml.Unparsed

class FeedRouter(feedFactories: List[FeedFactory])(implicit ec: ExecutionContext) extends StrictLogging with RssXmlMarshaller {

  private val feedMap = feedFactories.groupBy(_.name).mapValues(_.head)

  def routes = rejectEmptyResponse {
    pathPrefix("feed") {
      path(Segment) { name: String =>
        logger.debug(s"Serving feed $name")
        complete {
          feedMap.get(name)
            .map(asFeed)
            .map(asRss)
            .orElse(unknownFeed(name))
        }
      }
    }
  }

  private def asFeed(feedFactory: FeedFactory) = {
    val feed = feedFactory.create
    logger.debug(s"Feed items: ${feed.items.map(_.title)}")
    logger.trace(s"$feed")
    feed
  }

  private def asRss(feed: Feed) = {
    <rss version="2.0">
      <channel>
        <title>
          {feed.title}
        </title>
        <link>
          {feed.link}
        </link>{for (item <- feed.items) yield {
        <item>
          <title>
            {if (item.title.isEmpty) "NoName" else item.title}
          </title>
          <description>
            {Unparsed(item.description)}
          </description>
          <guid>
            {item.link}
          </guid>
          <link>
            {item.link}
          </link>
        </item>
      }}

      </channel>
    </rss>
  }

  private def unknownFeed(name: String) = {
    logger.warn(s"Unknown feed $name")
    None
  }
}
