package mwojcik.rss_customizer.rss.feeds
import mwojcik.rss_customizer.html.HtmlParser
import mwojcik.rss_customizer.html.HtmlParser.{ArticleSelector, FeedParser, Selector}
import mwojcik.rss_customizer.rss.domain.Feed

class Dilbert extends FeedFactory{
  override def name: String = "dilbert"
  override def create: Feed = {
    val parser = new HtmlParser
    parser.parseUrl("""http://dilbert.com/""", FeedParser(
      Selector("title"),
      ArticleSelector(
        articleSelector = Selector(query = ".comic-item"),
        titleSelector = Selector(".comic-title-name"),
        contentSelector = Selector(".img-comic-link"),
        linkSelector = Selector(".img-comic-link")
      )
    ))
  }
}
