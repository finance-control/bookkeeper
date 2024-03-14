plugins {
    kotlin("jvm") version "1.9.22"
    id("org.springframework.boot") version "3.1.4"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    id("io.spring.dependency-management") version "1.1.4"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":common:common-base"))
    implementation("org.flywaydb:flyway-maven-plugin:10.9.1")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:3.2.3")

    runtimeOnly("org.postgresql:postgresql:42.7.2")

    testApi("org.testcontainers:postgresql:1.19.7")
    testApi("org.springframework.boot:spring-boot-starter-test:3.2.3")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false
}