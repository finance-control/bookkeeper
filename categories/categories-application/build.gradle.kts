plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":categories:categories-usecases"))
    api(project(":categories:adapters:categories-data"))
    api(project(":categories:adapters:categories-rest"))

    implementation("org.springframework:spring-context:6.1.4")
}