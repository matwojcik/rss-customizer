package mwojcik.rss_customizer.rss.feeds

import mwojcik.rss_customizer.rss.domain.Feed

trait FeedFactory {
  /**
    * Name under which the feed will be available
    */
  def name: String
  def create: Feed
}
