import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"

    id("com.avast.gradle.docker-compose") version "0.14.0"
}

group = "io.codeal"
version = "1.0.0"

repositories {
    mavenCentral()
}

tasks.test {
    doFirst {
        val postgresContainer =
            dockerCompose.servicesInfos["postgres"]?.firstContainer ?: error("couldn't find postgres container")
        val postgresPort = postgresContainer.ports[5432] ?: error("couldn't find postgres port")
        systemProperty("postgres.port", postgresPort)
    }

    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    useIR = true
    jvmTarget = "11"
}
compileTestKotlin.kotlinOptions {
    useIR = true
    jvmTarget = "11"
}

dockerCompose {
    useComposeFiles.add("src/test/resources/docker-compose.yaml")
    isRequiredBy(tasks.test)
    isStopContainers = false
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // database
    implementation(platform("org.jdbi:jdbi3-bom:3.16.0"))
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-kotlin")
    runtimeOnly("org.postgresql:postgresql:42.2.19")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}
