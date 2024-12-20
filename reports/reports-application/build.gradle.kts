plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":reports:reports-usecases"))
    api(project(":reports:adapters:reports-data"))
    api(project(":reports:adapters:reports-events"))
    api(project(":reports:adapters:reports-rest"))

    implementation("org.springframework:spring-context:6.1.4")
}