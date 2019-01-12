package mwojcik.rss_customizer.rss.feeds

import mwojcik.rss_customizer.html.HtmlParser
import mwojcik.rss_customizer.html.HtmlParser.{ArticleSelector, FeedParserConfig, Selector}
import mwojcik.rss_customizer.rss.domain.Feed

class ScalaTimes extends FeedFactory{
  override def name: String = "scalatimes"
  override def create: Feed = {
    val parser = new HtmlParser
    parser.parseUrl("""https://scalatimes.com/""", FeedParserConfig(
      Selector(".middle"),
      ArticleSelector(
        articleSelector = Selector(query = ".section-content"),
        titleSelector = Selector(".article-content a span strong span"),
        contentSelector = Selector(".article-content > span > em"),
        linkSelector = Selector(".article-content a")
      )
    ))
  }
}
