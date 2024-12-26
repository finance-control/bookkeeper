plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":users:users-usecases"))
    api(project(":users:adapters:users-data"))
    api(project(":users:adapters:users-credentials"))
    api(project(":users:adapters:users-rest"))
    api(project(":users:adapters:users-tokens"))

    implementation("org.springframework:spring-context:6.1.4")
}