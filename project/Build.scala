import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

	val appName         = "kender-test1"
	val appVersion      = "1.0-SNAPSHOT"

	val appDependencies = Seq(
		// Add your project dependencies here,
		jdbc,
		anorm,
		"net.debasishg" %% "redisclient" % "2.9",
		"com.janrain" %% "bp-scala-api" % "0.1-SNAPSHOT"
	)

	val main = play.Project(appName, appVersion, appDependencies).settings(
		// Add your own project settings here
		resolvers ++= Seq(
			Resolver.file("home", file(Path.userHome.getAbsolutePath + "/.ivy2/local"))(Resolver.defaultIvyPatterns),
			"spray repo" at "http://repo.spray.io"
		)
	)

}
