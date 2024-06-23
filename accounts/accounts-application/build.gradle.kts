plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":accounts:accounts-usecases"))
    api(project(":accounts:adapters:accounts-data"))
    api(project(":accounts:adapters:accounts-rest"))
    api(project(":accounts:adapters:accounts-events"))

    implementation("org.springframework:spring-context:${rootProject.extra["springframework_version"]}")
}