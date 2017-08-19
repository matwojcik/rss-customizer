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
        Selector(".comic-item"),
        Selector(".comic-title-name"),
        Selector(".img-comic-container"),
        Selector(".img-comic-link")
      )
    ))
  }
}
