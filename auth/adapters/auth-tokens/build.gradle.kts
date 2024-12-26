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
    api(project(":tokens:tokens-usecases"))

    implementation("org.springframework:spring-context:6.1.4")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    testImplementation(testFixtures(project(":credentials:credentials-domain")))
}
