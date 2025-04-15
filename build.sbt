
/*
 build.sbt adapted from https://github.com/pbassiner/sbt-multi-project-example/blob/master/build.sbt
*/


name := "admin-service"
ThisBuild / organization := "de.dnpm.dip"
ThisBuild / scalaVersion := "2.13.16"
ThisBuild / version      := "1.0-SNAPSHOT"


//-----------------------------------------------------------------------------
// PROJECTS
//-----------------------------------------------------------------------------

lazy val global = project
  .in(file("."))
  .settings(
    settings,
    publish / skip := true
  )
  .aggregate(
     api,
     impl
  )

lazy val api = project
  .settings(
    name := "admin-service-api",
    settings,
    libraryDependencies ++= Seq(
      dependencies.scalatest,
      dependencies.service_base
    )
  )

lazy val impl = project
  .settings(
    name := "admin-service-impl",
    settings,
    libraryDependencies ++= Seq(
      dependencies.scalatest,
      dependencies.connector_base
    )
  )
  .dependsOn(api)


//-----------------------------------------------------------------------------
// DEPENDENCIES
//-----------------------------------------------------------------------------

lazy val dependencies =
  new {
    val scalatest      = "org.scalatest" %% "scalatest"      % "3.2.18"        % Test
    val service_base   = "de.dnpm.dip"   %% "service-base"   % "1.0-SNAPSHOT"
    val connector_base = "de.dnpm.dip"   %% "connector-base" % "1.0-SNAPSHOT"
  }


//-----------------------------------------------------------------------------
// SETTINGS
//-----------------------------------------------------------------------------

lazy val settings = commonSettings


// Compiler options from: https://alexn.org/blog/2020/05/26/scala-fatal-warnings/
lazy val compilerOptions = Seq(
  // Feature options
  "-encoding", "utf-8",
  "-explaintypes",
  "-feature",
  "-language:existentials",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ymacro-annotations",

  // Warnings as errors!
  "-Xfatal-warnings",

  // Linting options
  "-unchecked",
  "-Xcheckinit",
  "-Xlint:adapted-args",
  "-Xlint:constant",
  "-Xlint:delayedinit-select",
  "-Xlint:deprecation",
  "-Xlint:doc-detached",
  "-Xlint:inaccessible",
  "-Xlint:infer-any",
  "-Xlint:missing-interpolator",
  "-Xlint:nullary-unit",
  "-Xlint:option-implicit",
  "-Xlint:package-object-classes",
  "-Xlint:poly-implicit-overload",
  "-Xlint:private-shadow",
  "-Xlint:stars-align",
  "-Xlint:type-parameter-shadow",
  "-Wdead-code",
  "-Wextra-implicit",
  "-Wnumeric-widen",
  "-Wunused:imports",
  "-Wunused:locals",
  "-Wunused:patvars",
  "-Wunused:privates",
  "-Wunused:implicits",
  "-Wvalue-discard",

  // Deactivated to avoid many false positives from 'evidence' parameters in context bounds
//  "-Wunused:params",
)


lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++=
    Seq("Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository") ++
    Resolver.sonatypeOssRepos("releases") ++
    Resolver.sonatypeOssRepos("snapshots")
)

