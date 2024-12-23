plugins {
    kotlin("jvm") version "1.9.20"
    application
}

kotlin {
    jvmToolchain(18)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("org.apache.commons:commons-lang3:3.0")
    implementation("com.github.shiguruikai:combinatoricskt:1.6.0")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}
