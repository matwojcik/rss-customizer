logLevel := Level.Warn
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.1")
resolvers += "Bintray sbt plugin releases" at "http://dl.bintray.com/sbt/sbt-plugin-releases/"
