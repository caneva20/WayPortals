import java.util.*
import java.io.FileInputStream

defaultTasks("copyToDevServer")

plugins {
    java
    `maven-publish`
    id("io.freefair.lombok") version "5.3.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    mavenLocal()
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")
    implementation("co.aikar:idb-core:1.0.0-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.xerial:sqlite-jdbc:3.34.0")
    implementation("com.google.dagger:dagger:2.31")
    implementation("io.papermc:paperlib:1.0.6")
    implementation("ch.jalu:configme:1.2.0")

    compileOnly("org.spigotmc:spigot-api:1.16.3-R0.1-SNAPSHOT")
    compileOnly("me.caneva20:C20core:1.1.7")
    compileOnly("com.google.auto.factory:auto-factory:1.0-beta8")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.5.0")

    annotationProcessor("com.google.auto.factory:auto-factory:1.0-beta8")
    annotationProcessor("com.google.dagger:dagger-compiler:2.31")
}

val group = "me.caneva20"
val artifactId = "wayportals"

val appName = "WayPortals"
val appDescription = "A more vanilla alternative to /home"
val authors = "caneva20"
val website = "https://github.com/caneva20/WayPortals"

val version = "0.4.0"
val isSnapshot = true

val versionFinal = "${version}${(if (isSnapshot) "-SNAPSHOT" else "")}"
val main = "${group}.${artifactId}.${appName}"

val minecraft = Minecraft()

class Minecraft {
    val apiVersion = 1.16
    val depend = "C20Core, ProtocolLib"
}

val envProperties = Properties()
val envPropertiesFile = rootProject.file("env.properties")
if (envPropertiesFile.exists()) {
    envProperties.load(FileInputStream(envPropertiesFile))
}

val minimizeJar = if (envProperties.contains("minimizeJar")) envProperties["minimizeJar"] as Boolean else true
val devServerLocation = envProperties["devServerLocation"]

project.group = group
project.version = versionFinal
project.description = appName

java.sourceCompatibility = JavaVersion.VERSION_11

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.processResources {
    expand(
            "version" to versionFinal,
            "name" to appName,
            "website" to website,
            "description" to appDescription,
            "authors" to authors,
            "main" to main,
            "api_version" to minecraft.apiVersion,
            "depend" to minecraft.depend
    )
}

tasks.shadowJar {
    relocate("co.aikar.commands", "me.caneva20.wayportals.acf")
    relocate("co.aikar.idb", "me.caneva20.wayportals.idb")
    relocate("co.aikar.locales", "me.caneva20.wayportals.acf-locales")
    relocate("io.papermc.lib", "me.caneva20.wayportals.paperlib")
    relocate("ch.jalu.configme", "me.caneva20.wayportals.configme")

    if (minimizeJar) {
        minimize()
    }

    archiveClassifier.set("")
    archiveBaseName.set(appName)
    archiveVersion.set(versionFinal)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.register<Copy>("copyToDevServer") {
    dependsOn(tasks.build)

    if (devServerLocation != null) {
        val finalName = "$appName-$versionFinal.jar"

        println("Building plugin and copying $finalName to $devServerLocation/plugins/")

        from("${project.buildDir}/libs/$finalName")
        into("$devServerLocation/plugins/")
    } else {
        println("Building plugin")
        println("env.properties not set up with a dev server location (devServerLocation=$devServerLocation)")
    }
}