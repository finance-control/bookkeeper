plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":transfers:transfers-usecases"))
    api(project(":accounts:accounts-usecases"))
    implementation("org.springframework:spring-context:6.1.4")

    testImplementation(testFixtures(project(":transfers:transfers-domain")))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
}