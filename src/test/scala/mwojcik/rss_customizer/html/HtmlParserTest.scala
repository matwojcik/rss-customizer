package mwojcik.rss_customizer.html

import mwojcik.rss_customizer.html.HtmlParser.{ArticleSelector, FeedParser, Selector}
import mwojcik.rss_customizer.rss.domain.Feed
import mwojcik.rss_customizer.rss.domain.Feed.Item
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class HtmlParserTest extends FeatureSpec with GivenWhenThen with Matchers {

  val htmlParser = new HtmlParser

  feature("HtmlParsing") {
    scenario("Correct file") {
      Given("Correct html file")
      val fileName = "src/test/resources/example.html"
      val feedParser = FeedParser(
        titleSelector = Selector("title"),
        articleSelector = ArticleSelector(
          Selector(".article"),
          Selector(".title-name"),
          Selector(".article-content"),
          Selector("a.title")
        ))

      When("Parsing the file")
      val feed = htmlParser.parseFile(fileName, feedParser)

      Then("Feed should be extracted")
      feed shouldBe Feed(
        "Example feed",
        List(
          Item("Article title", "Some content", "http://example.com/1234"),
          Item("Article title2", "Some content2", "http://example.com/2222")
        ),
        fileName
      )
    }
  }

}
