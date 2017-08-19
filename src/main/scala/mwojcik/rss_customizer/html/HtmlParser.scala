package mwojcik.rss_customizer.html

import mwojcik.rss_customizer.rss.Feed
import mwojcik.rss_customizer.rss.Feed.Item
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

class HtmlParser {
  def parse(address: String): Feed = {
    val browser = JsoupBrowser()

    val document = browser.parseFile(address)

    val articles = document >> elementList(".article")
    val items = articles map { article =>
      val title = article >> allText(".title-name")
      val content = article >> allText(".article-content")
      val link = article >> attr("href")("a.title")
      Item(title, content, link)
    }

    val title = document >> allText("title")

    Feed(title, items, address)
  }
}
