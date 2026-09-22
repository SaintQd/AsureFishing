plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.saintqd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven(url = "https://maven.enginehub.org/repo/")
    maven(url = "https://jitpack.io")
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2+")
    compileOnly(files("../AsureLib/build/libs/AsureLib-1.0-SNAPSHOT.jar"))

    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.19-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:5.+")
}

tasks.withType<Jar> {

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}