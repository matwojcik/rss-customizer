package mwojcik.rss_customizer.rss.domain

import mwojcik.rss_customizer.rss.domain.Feed.Item

case class Feed(title: String, items: List[Item], link: String)

object Feed {
  case class Item(title: String, description: String, link: String)
}