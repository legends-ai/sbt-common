package asuna.sbt

import sbt._
import Keys._

object CommonSettingsAutoPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def projectSettings = Seq(
    organization := "asuna",

    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-Xlint"
    ),

    // TODO(igm): migrate to Azure
    resolvers += "Aincrad" at "s3://aincrad.asuna.io",

    publishTo := Some("Aincrad" at "s3://aincrad.asuna.io")
  )

}
