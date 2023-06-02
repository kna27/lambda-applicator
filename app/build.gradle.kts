plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    implementation("com.google.guava:guava:31.1-jre")
}

application {
    mainClass.set("lambda.Console")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}
