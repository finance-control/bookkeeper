plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":spendings:spendings-usecases"))
    api(project(":spendings:adapters:spendings-data"))
    api(project(":spendings:adapters:spendings-rest"))
    api(project(":spendings:adapters:spendings-category"))
    api(project(":spendings:adapters:spendings-account"))

    implementation("org.springframework:spring-context:6.1.4")
}