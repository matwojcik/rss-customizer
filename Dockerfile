FROM hseeberger/scala-sbt

WORKDIR /rss-customizer
ADD . /rss-customizer
EXPOSE 9000

CMD sbt run