package asuna.sbt

import sbt._
import Keys._

import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.autoImport._

import sbtbuildinfo.BuildInfoPlugin
import sbtbuildinfo.BuildInfoPlugin.autoImport._

import sbtdocker.DockerPlugin
import sbtdocker.DockerPlugin.autoImport._

import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.GitPlugin.autoImport.{ git, versionWithGit }

object CommonSettingsAutoPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = AssemblyPlugin && BuildInfoPlugin && DockerPlugin && GitVersioning

  // Base URL for Docker Repository
  val base = "096202052535.dkr.ecr.us-east-1.amazonaws.com"

  override def projectSettings = Seq(
    organization := "asuna",

    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-Xlint"
    ),

    resolvers += "Aincrad" at "s3://aincrad.asuna.io",

    publishTo := Some("Aincrad" at "s3://aincrad.asuna.io"),

    git.formattedShaVersion := git.gitHeadCommit.value.map(_.toString),

    mainClass in assembly := Some(s"asuna.$name.Main"),
    assemblyJarName in assembly := s"$name-assembly.jar",
    assemblyMergeStrategy in assembly := {
      case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
      case x if x contains "publicsuffix" => MergeStrategy.first
      case x => (assemblyMergeStrategy in assembly).value(x)
    },

    dockerfile in docker := {
      // The assembly task generates a fat JAR file
      val artifact: File = assembly.value
      val artifactTargetPath = s"/app/${artifact.name}"

      new Dockerfile {
        from("java")
        add(artifact, artifactTargetPath)
        entryPoint("java", "-jar", artifactTargetPath)
      }
    },

    imageNames in docker := Seq(
      // Sets the latest tag
      ImageName(s"$base/${name.value}:latest"),
      ImageName(s"$base/${name.value}:${version.value}")
    ),

    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "buildinfo",

    // remove any non-nop implementation of SLF4J during tests
    (dependencyClasspath in Test) ~= { cp =>
      cp.filterNot(_.data.name.contains("slf4j-log4j12"))
        .filterNot(_.data.name.contains("logback"))
    }

  )

  override def buildSettings = versionWithGit
}
