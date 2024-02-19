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
    api(project(":spendings:spendings-usecases"))
    api(project(":spendings:adapters:spendings-data"))
    api(project(":spendings:adapters:spendings-rest"))

    implementation("org.springframework:spring-context:6.1.4")
}