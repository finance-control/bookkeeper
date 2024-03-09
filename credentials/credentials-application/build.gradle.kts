plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":credentials:credentials-usecases"))
    api(project(":credentials:adapters:credentials-data"))
    api(project(":credentials:adapters:credentials-encryption"))


    implementation("org.springframework:spring-context:6.1.4")
}