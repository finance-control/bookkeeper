plugins {
    kotlin("jvm") version "1.9.22"
    id ("java-library")
    id("org.springframework.boot") version "3.2.2" apply false
    kotlin("plugin.spring") version "1.9.22"
    id("io.spring.dependency-management") version "1.1.4"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":common:common-events"))
    implementation("org.springframework.boot:spring-boot-starter:3.2.3")
}