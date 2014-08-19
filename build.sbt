name := """dolog"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
lazy val core = project in file("core")

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
    

    
val appDependencies = Seq(
   // Add your project dependencies here,
  "commons-io" % "commons-io" % "2.4",
    "com.jcraft.jsch" % "jsch" % "0.1.51"
)
