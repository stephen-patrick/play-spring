import sbt._
import Keys._


object ApplicationBuild extends Build {

    val appName         = "play-2.0-spring-module"
    val appVersion      = "1.0"

    val appDependencies = Seq(
    		"play" % "play_2.9.1" % "2.0",
    		"org.springframework" % "spring-asm" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-beans" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-core" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-context" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-expression" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-test" % "3.1.1.RELEASE"
    		
    )
    
    val main = Project(appName, base = file(".")).settings(
	    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
	
	    version := appVersion,

	    libraryDependencies ++= appDependencies
	)

}
