plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":auth:auth-usecases"))
    api(project(":auth:adapters:auth-spring"))
    api(project(":auth:adapters:auth-credentials"))
}
