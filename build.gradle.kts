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

