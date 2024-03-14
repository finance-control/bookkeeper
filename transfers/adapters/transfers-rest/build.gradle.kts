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
    api(project(":common:user-context"))

    implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
    implementation("org.springframework:spring-context:6.1.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.0")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.6.1")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(project(":transfers:transfers-fixtures"))
}
