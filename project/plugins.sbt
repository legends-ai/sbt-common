// This project is its own plugin
unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "src" / "main" / "scala"

addSbtPlugin("com.frugalmechanic" % "fm-sbt-s3-resolver" % "0.9.0")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.6.1")
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")
