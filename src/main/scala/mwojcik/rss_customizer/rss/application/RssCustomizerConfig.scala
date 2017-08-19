package mwojcik.rss_customizer.rss.application

case class RssCustomizerConfig(http: HttpConfig)

case class HttpConfig(host: String, port: Int) {
  def address : String = s"$host:$port"
}