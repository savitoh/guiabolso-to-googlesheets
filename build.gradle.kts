import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.7.20"
    application
    id("org.jetbrains.kotlin.kapt") version "1.4.30"
}

group = "com.github.savitoh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


apply(plugin = "kotlin-kapt")
dependencies {
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation(kotlin("test"))
    implementation("info.picocli:picocli:4.7.0")
    implementation("com.google.api-client:google-api-client:2.1.1")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20221216-2.0.0")
    kapt("info.picocli:picocli-codegen:4.7.0")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
        exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events(
            org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
        )
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}

application {
    mainClass.set("MainKt")
}

