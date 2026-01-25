plugins {
    kotlin("jvm") version "2.2.21"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "kr.astar"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://repo.nexomc.com/releases")
    maven("https://maven.devs.beer/")
    maven("https://repo.momirealms.net/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compileOnly("com.nexomc:nexo:${rootProject.properties["nexo_version"]}") { exclude("*") }
    compileOnly("dev.lone:api-itemsadder:${rootProject.properties["itemsadder_version"]}")
    compileOnly("net.momirealms:craft-engine-core:${rootProject.properties["craft_engine_version"]}")
    compileOnly("net.momirealms:craft-engine-bukkit:${rootProject.properties["craft_engine_version"]}")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
