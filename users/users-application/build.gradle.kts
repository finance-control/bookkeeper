plugins {
    kotlin("jvm") version "1.9.22"
    id("org.springframework.boot") version "3.2.2"
    kotlin("plugin.spring") version "1.9.22"
    id("io.spring.dependency-management") version "1.1.4"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":users:users-usecases"))
    api(project(":users:adapters:users-data"))
    api(project(":users:adapters:users-credentials"))
    api(project(":users:adapters:users-rest"))

    implementation("org.springframework:spring-context:6.1.4")
}