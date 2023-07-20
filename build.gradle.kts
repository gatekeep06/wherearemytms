plugins {
    id("java")
    id("dev.architectury.loom") version("0.12.0-SNAPSHOT")
    id("architectury-plugin") version("3.4-SNAPSHOT")
    kotlin("jvm") version ("1.7.10")
}

group = property("maven_group")!!
version = property("mod_version")!!

architectury {
    platformSetupLoomIde()
    fabric()
}

repositories {
    mavenCentral()
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.impactdev.net/repository/development/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    // Fabric API
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    // Fabric Kotlin
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.3+kotlin.1.7.10")

    // Architectury
    modImplementation("dev.architectury", "architectury-fabric", "6.5.69")

    // Cobblemon
//    modImplementation("curse.maven:cobblemon-687131:${property("cobblemon_curse_file_id")}")
    modImplementation("com.cobblemon:fabric:${property("cobblemon_version")}")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

java {
    withSourcesJar()
}
