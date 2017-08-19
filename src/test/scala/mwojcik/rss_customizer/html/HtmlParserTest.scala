package mwojcik.rss_customizer.html

import mwojcik.rss_customizer.rss.Feed
import mwojcik.rss_customizer.rss.Feed.Item
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class HtmlParserTest extends FeatureSpec with GivenWhenThen with Matchers {

  val parser = new HtmlParser

  feature("HtmlParsing") {
    scenario("Correct file") {
      Given("Correct html file")
      When("Parsing the file")
      val fileName = "src/test/resources/example.html"
      val feed = parser.parse(fileName)

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
