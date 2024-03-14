plugins {
    kotlin("jvm") version "1.9.22"
    id("org.springframework.boot") version "3.2.2"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(project(":common:common-properties"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

}