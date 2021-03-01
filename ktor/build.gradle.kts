import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.serialization") version "1.4.30"
}

group = "io.codeal"
version = "1.0.0"

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    useIR = true
    jvmTarget = "11"
}

dependencies {
    implementation(kotlin("stdlib"))

    // ktor
    implementation(platform("io.ktor:ktor-bom:1.5.1"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-serialization")
    implementation("io.ktor:ktor-server-netty")
    testImplementation("io.ktor:ktor-server-tests")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("io.kotlintest:kotlintest-assertions:3.4.2")
    testImplementation("io.kotlintest:kotlintest-assertions-json:3.4.2")
}
