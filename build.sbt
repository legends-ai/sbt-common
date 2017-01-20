sbtPlugin := true
name := "sbt-common"
version := "0.1.3"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")

lazy val root = (project in file(".")).enablePlugins(
  AssemblyPlugin,
  BuildInfoPlugin,
  DockerPlugin,
  GitVersioning
)
