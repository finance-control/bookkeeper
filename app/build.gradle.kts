plugins {
    kotlin("jvm") version "1.9.22"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":spendings:spendings-application"))
    implementation(project(":transfers:transfers-application"))
    implementation(project(":categories:categories-application"))
    implementation(project(":accounts:accounts-application"))

    implementation(project(":auth:adapters:auth-spring")) // TODO: remake to application
    implementation(project(":common:common-rest"))
    implementation(project(":spring-events"))
    implementation(project(":spring-transactions"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework:spring-context:6.1.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
}