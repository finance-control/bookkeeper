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
    api(project(":transfers:transfers-usecases"))
    api(project(":transfers:adapters:transfers-data"))
    api(project(":transfers:adapters:transfers-rest"))
    api(project(":transfers:adapters:transfers-category"))
    api(project(":transfers:adapters:transfers-account"))

    implementation("org.springframework:spring-context:6.1.4")
}