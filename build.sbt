

name := "project-euler-util"

scalaVersion := "2.13.5"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",    // source files are in UTF-8
  "-deprecation",          // warn about use of deprecated APIs
  "-unchecked",            // warn about unchecked type parameters
  "-feature",              // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint",                // enable handy linter warnings
//  "-Xfatal-warnings"       // compilation warning fail as errors
)

scalacOptions ++= Seq("-target:jvm-1.8")
