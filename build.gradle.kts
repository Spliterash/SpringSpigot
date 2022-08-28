import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer

plugins {
    id("java-library")
    id("maven-publish")
    id("io.freefair.lombok") version "6.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}


group = "ru.spliterash"
version = "1.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.assemble.get().dependsOn(tasks.shadowJar)

tasks.shadowJar {
    mergeServiceFiles {
        include("META-INF/spring/**")
    }

    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
    transform(PropertiesFileTransformer::class.java) {
        paths = listOf("META-INF/spring.factories")
        mergeStrategy = "append"
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        name = "spigot-repo"
    }

    maven {
        name = "vault-repo"
        url = uri("https://nexus.hc.to/content/repositories/pub_releases")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18.1-R0.1-SNAPSHOT")
    api(platform("org.springframework.boot:spring-boot-dependencies:2.7.3"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-data-mongodb")
    api("org.redisson:redisson-spring-boot-starter:3.17.6")

    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    runtimeOnly("mysql:mysql-connector-java")

    api("org.apache.commons:commons-lang3:3.12.0")
    api("commons-io:commons-io:2.11.0")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.7.3")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            repositories {
                maven {
                    name = "nexus"
                    url = uri("https://nexus.spliterash.ru/repository/spring-spigot")
                    credentials {
                        username = findProperty("SPLITERASH_NEXUS_USR")?.toString()
                        password = findProperty("SPLITERASH_NEXUS_PSW")?.toString()
                    }
                }
            }
        }
    }
}
