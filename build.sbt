/*enablePlugins(AndroidApp)*/ android.Plugin.androidBuild

// Specifying the Android target Sdk version
/*platformTarget := "android-26"*/ platformTarget in Android := "android-22"

// Application Name
name := "AudioGroupTraining"

// Application Version
version := "1.0.0"

// Scala version
scalaVersion := "2.11.4"

// Repositories for dependencies
resolvers ++= Seq(Resolver.mavenLocal,
  DefaultMavenRepository,
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeRepo("snapshots"),
  Resolver.typesafeIvyRepo("snapshots"),
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.defaultLocal,
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases")

// Override the run task with the android:run
run <<= run in Android

proguardScala in Android := true

useProguard in Android := true

proguardOptions in Android ++= Seq(
  "-ignorewarnings",
  "-keep class scala.Dynamic")

libraryDependencies ++= Seq(
  aar("com.android.support" % "appcompat-v7" % "22.1.1"),
  aar("com.android.support" % "recyclerview-v7" % "22.1.1"),
  "com.google.android" % "android" % "4.1.1.4" % "test",
  "org.specs2" %% "specs2-core" % "2.4.15" % "test",
  "org.specs2" %% "specs2-mock" % "3.0-M2" % "test"
)


/*scalaVersion := "2.11.8"

enablePlugins(AndroidApp)
android.useSupportVectors

versionCode := Some(1)
version := "0.1-SNAPSHOT"
name := "AudioGroupExample"

instrumentTestRunner :=
  "android.support.test.runner.AndroidJUnitRunner"

platformTarget := "android-26"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

libraryDependencies ++=
  "com.android.support" % "appcompat-v7" % "24.0.0" ::
  "com.android.support.test" % "runner" % "0.5" % "androidTest" ::
  "com.android.support.test.espresso" % "espresso-core" % "2.2.2" % "androidTest" ::
  Nil*/
