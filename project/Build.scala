import sbt._
import Keys._
import PlayProject._


object ApplicationBuild extends Build {

    val appName         = "play-2.0-spring-module"
    val appVersion      = "1.0"

    val appDependencies = Seq(
    		"org.springframework" % "spring-asm" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-beans" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-core" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-context" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-expression" % "3.1.1.RELEASE",
    		"org.springframework" % "spring-test" % "3.1.1.RELEASE"
    		
    )
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here   
    		organization := "madeby2"
    )

}


