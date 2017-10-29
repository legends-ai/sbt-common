sbtPlugin := true
name := "sbt-common"
version := "0.1.13"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.5.0")
