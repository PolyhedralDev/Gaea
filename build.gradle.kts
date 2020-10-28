import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream

plugins {
    java
    id("com.github.johnrengelman.shadow").version("6.0.0")
}

repositories {
    flatDir {
        dirs("lib")
    }
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("http://maven.enginehub.org/repo/")
    }
    maven {
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "com.solostudios.omnivoxscraper"
//version = "0.0.1-alpha.3"
val versionObj = Version("1", "12", "3")
version = versionObj

dependencies {
    implementation("org.jetbrains:annotations:20.1.0") // more recent.
    implementation("org.spigotmc:spigot-api:1.16.2-R0.1-SNAPSHOT")
    implementation("com.googlecode.json-simple:json-simple:1.1")
    implementation("javax.vecmath:vecmath:1.5.2")
    implementation("commons-io:commons-io:2.4")
    implementation("co.aikar:taskchain-bukkit:3.7.2")
    implementation("com.esotericsoftware:reflectasm:1.11.9")
    compile("org.bstats:bstats-bukkit:1.7")
    
    // JUnit.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
    
    maxHeapSize = "1G"
    ignoreFailures = false
    failFast = true
    maxParallelForks = 12
}

val shadowRelocation = tasks.create("shadowRelocation", ConfigureShadowRelocation::class) {
    target = tasks.shadowJar.get()
    prefix = "libs"
}


tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveBaseName.set("Gaea")
    setVersion(project.version)
    dependsOn(shadowRelocation)
}


/**
 * Version class that does version stuff.
 */
class Version(val major: String, val minor: String, val revision: String, val preReleaseData: String? = null) {
    
    override fun toString(): String {
        return if (preReleaseData.isNullOrBlank())
            "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-$preReleaseData+${getGitHash()}"
    }
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
