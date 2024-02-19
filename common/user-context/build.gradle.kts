plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.security:spring-security-core:6.1.4")
    implementation("org.springframework:spring-web:6.1.4")
    implementation("org.springframework:spring-webmvc:6.1.4")
}