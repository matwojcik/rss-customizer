package mwojcik.rss_customizer.html

import mwojcik.rss_customizer.html.HtmlParser.{ArticleSelector, FeedParser}
import mwojcik.rss_customizer.rss.Feed
import mwojcik.rss_customizer.rss.Feed.Item
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.language.implicitConversions

class HtmlParser {

  def parse(address: String, parser: FeedParser): Feed = {
    val (titleSelector, articleSelector) = (parser.titleSelector, parser.articleSelector)

    val document = JsoupBrowser().parseFile(address)
    val articles = document >> elementList(articleSelector.articleSelector.query)
    val items = articles map asItem(articleSelector)
    val title = document >> allText(titleSelector.query)

    Feed(title, items, address)
  }

  private def asItem(articleSelector: ArticleSelector)(article: Element) = {
    val title = article >> allText(articleSelector.titleSelector.query)
    val content = article >> allText(articleSelector.contentSelector.query)
    val link = article >> attr("href")(articleSelector.linkSelector.query)
    Item(title, content, link)
  }
}

object HtmlParser {

  case class Selector(query: String) extends AnyVal

  case class FeedParser(
                         titleSelector: Selector,
                         articleSelector: ArticleSelector
                       )

  case class ArticleSelector(articleSelector: Selector,
                             titleSelector: Selector,
                             contentSelector: Selector,
                             linkSelector: Selector)

}