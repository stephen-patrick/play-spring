import sbt._
import Keys._


object ApplicationBuild extends Build {

    val appName         = "play-2.0-spring-module"
    val appVersion      = "1.0"

    val appDependencies = Seq(
    		"play" %% "play" % "2.0-SNAPSHOT",
    		"org.springframework" % "spring-asm" % "3.0.7.RELEASE",
    		"org.springframework" % "spring-core" % "3.0.7.RELEASE",
    		"org.springframework" % "spring-context-support" % "3.0.7.RELEASE"
    		
    )
    
    val main = Project(appName, base = file(".")).settings(
	    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
	
	    version := appVersion,

	    libraryDependencies ++= appDependencies
	)

}
