package asuna.sbt

import sbt._
import Keys._

object CommonSettingsAutoPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def projectSettings = Seq(
    organization := "asuna",

    scalaOrganization := "org.typelevel",
    scalaVersion := "2.12.3-bin-typelevel-4",

    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-deprecation",
      "-feature",
      "-Xlint",
      "-Ypartial-unification"
    ),

    resolvers += "Aincrad" at "s3://aincrad.asuna.io",

    sources in (Compile, doc) := Seq.empty,
    publishArtifact in (Compile, packageDoc) := false,
    publishTo := Some("Aincrad" at "s3://aincrad.asuna.io")
  )

}
