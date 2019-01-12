package mwojcik.rss_customizer.html

import mwojcik.rss_customizer.html.HtmlParser.{ArticleSelector, FeedParserConfig}
import mwojcik.rss_customizer.rss.domain.Feed
import mwojcik.rss_customizer.rss.domain.Feed.Item
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

import scala.language.implicitConversions

/**
  * Parses Html into Feed, using FeedParserConfig, which specified how the items and its
  * attributes should be parsed out of html source.
  */
class HtmlParser {
  def parseFile(fileName: String, parser: FeedParserConfig): Feed = {
    val document = JsoupBrowser().parseFile(fileName)
    parse(document, fileName, parser)
  }

  def parseUrl(address: String, parser: FeedParserConfig): Feed = {
    val document = JsoupBrowser().get(address)
    parse(document, address, parser)
  }

  private def parse(document: Browser#DocumentType, address: String, parser: FeedParserConfig) = {
    val (titleSelector, articleSelector) = (parser.titleSelector, parser.articleSelector)
    val articles = document >> elementList(articleSelector.articleSelector.query)
    val items = articles map asItem(articleSelector) filterNot(_.link.isEmpty)
    val title = document >> allText(titleSelector.query)

    Feed(title, items, address)
  }

  private def asItem(articleSelector: ArticleSelector)(article: Element) = {
    val title = article >> allText(articleSelector.titleSelector.query)
    val content = (article >> elementList(articleSelector.contentSelector.query)).map(_.innerHtml).mkString
    val link = article >> attr("href")(articleSelector.linkSelector.query)
    Item(title, content, link)
  }
}

object HtmlParser {

  case class Selector(query: String) extends AnyVal

  case class FeedParserConfig(
                         titleSelector: Selector,
                         articleSelector: ArticleSelector
                       )

  case class ArticleSelector(articleSelector: Selector,
                             titleSelector: Selector,
                             contentSelector: Selector,
                             linkSelector: Selector)

}