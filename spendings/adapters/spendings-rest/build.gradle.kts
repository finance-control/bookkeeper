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
    api(project(":spendings:spendings-usecases"))
    api(project(":spendings:spendings-usecases"))
    api(project(":common:user-context"))
    api(project(":common:common-base"))
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation(testFixtures(project(":spendings:spendings-domain")))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.2")
    testImplementation("org.springframework.security:spring-security-test:6.1.4")
}
