plugins {
    kotlin("jvm") version "1.9.22"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":spendings:spendings-domain"))
    api(project(":common:common-events"))

    testImplementation(testFixtures(project(":spendings:spendings-domain")))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
}