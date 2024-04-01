plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("org.springframework.boot") version "3.2.2" apply false
    id("io.spring.dependency-management") version "1.1.4"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-logging:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-logging:3.2.4")

    implementation("org.springframework:spring-context:6.1.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.3")
}
