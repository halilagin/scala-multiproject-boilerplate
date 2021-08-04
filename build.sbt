/* vim: set ts=2 sts=2 sw=2 noet */

name := "akka_training"
organization in ThisBuild := "peralabs"
//don't use scala 2.12.0, because it throws error of "Task not serializable" while applying groupby function on streaming data.
scalaVersion in ThisBuild := "2.13.1"

version := "0.0.1"

lazy val dependencies =
	new {
		val akkaV							= "2.6.10"
		val logbackV					= "1.2.3"
		val scalatestV				= "3.2.1"
		val slf4jV						= "1.7.30"
		val jacksonV					= "2.11.1"
		val yamlV							= "1.27"
	}

val modelDependencies = Seq()
val daoDependencies = Seq()
val serviceDependencies = Seq()
val webDependencies = Seq()



resolvers ++= Seq (
  Opts.resolver.sbtIvySnapshots,
  "Local Ivy2 Repository" at "file://" + Path.userHome.absolutePath + "/.ivy2/local",
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
  "Confluent" at "https://packages.confluent.io/maven"
)

resolvers += Resolver.mavenLocal


lazy val commonSettings = Seq(
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.ivy2/local",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.mavenLocal,
    "Confluent" at "https://packages.confluent.io/maven"
  )
)

lazy val settings = commonSettings 


lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)



lazy val modelAssemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)


lazy val daoAssemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)


lazy val serviceAssemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)


lazy val webAssemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)


lazy val global = project
  .in(file("."))
  .settings(settings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    model,
    dao,
    service,
    web
  )



val copyJars = TaskKey[Unit]("copyJars", "Copy all dependency jars to target/lib")
copyJars := {
	val files: Seq[File] = (fullClasspath in Compile).value.files.filter( !_.isDirectory)
	files.foreach( f => IO.copyFile(f, file("./target/lib/" + f.getName())))
}


lazy val model = project
  .in(file("model"))
  .settings(
    name := "model",
    settings,
		modelAssemblySettings,
    libraryDependencies ++= modelDependencies,

  )
  //.disablePlugins(AssemblyPlugin)


lazy val dao = project
  .in(file("dao"))
  .settings(
    name := "dao",
    settings,
		daoAssemblySettings,
    libraryDependencies ++= daoDependencies,
  )
  //.disablePlugins(AssemblyPlugin)
	.dependsOn(model)


lazy val service = project
  .in(file("service"))
  .settings(
    name := "service",
    settings,
		serviceAssemblySettings,
    libraryDependencies ++= serviceDependencies,
  )
  //.disablePlugins(AssemblyPlugin)
	.dependsOn(dao)


lazy val web = project
  .in(file("web"))
  .settings(
    name := "web",
    settings,
		webAssemblySettings,
    libraryDependencies ++= webDependencies,
  )
  //.disablePlugins(AssemblyPlugin)
	.dependsOn(service)
