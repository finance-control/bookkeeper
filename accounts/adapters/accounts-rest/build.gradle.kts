plugins {
    kotlin("jvm") version "1.9.22"
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
    api(project(":accounts:accounts-usecases"))
    api(project(":common:user-context"))

    implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
    implementation("org.springframework.boot:spring-boot-starter-web-services:3.2.3")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.4.0")

    testImplementation(testFixtures(project(":accounts:accounts-domain")))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.3")
    testImplementation("org.springframework.security:spring-security-test:${rootProject.extra["springframework_version"]}")
}
