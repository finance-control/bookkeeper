plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":assets:assets-usecases"))
    api(project(":assets:adapters:assets-rest"))

    implementation("org.springframework:spring-context:6.1.4")
}