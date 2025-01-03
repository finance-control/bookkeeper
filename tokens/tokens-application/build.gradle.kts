plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":tokens:tokens-usecases"))
    api(project(":tokens:adapters:tokens-data"))
    api(project(":tokens:adapters:tokens-hashing"))
    api(project(":tokens:adapters:tokens-encryption"))

    implementation("org.springframework:spring-context:6.1.4")
}
