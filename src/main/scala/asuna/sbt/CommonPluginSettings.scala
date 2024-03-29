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

object CommonPluginSettingsAutoPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override def requires =
    AssemblyPlugin &&
    BuildInfoPlugin &&
    DockerPlugin &&
    GitVersioning

  // Base URL for Docker Repository
  val base = "asuna.azurecr.io"

  override def projectSettings = Seq(
    git.formattedShaVersion := git.gitHeadCommit.value.map(_.toString),

    mainClass in assembly := Some(s"asuna.${name.value}.Main"),
    assemblyJarName in assembly := s"${name.value}-assembly.jar",
    assemblyMergeStrategy in assembly := {
      case x if x contains "io.grpc" => MergeStrategy.first
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x if x.endsWith("BUILD") => MergeStrategy.discard
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
    buildInfoPackage := "buildinfo"
  )

  override def buildSettings = versionWithGit
}
