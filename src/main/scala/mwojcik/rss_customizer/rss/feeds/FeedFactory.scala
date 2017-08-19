package mwojcik.rss_customizer.rss.feeds

import mwojcik.rss_customizer.rss.domain.Feed

trait FeedFactory {
  def name: String
  def create: Feed
}
