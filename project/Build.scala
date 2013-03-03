import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "browser-connect"
  val appVersion      = "0.2-SNAPSHOT"
  val appDependencies = Seq()

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-feature", "-deprecation")
  )
}
