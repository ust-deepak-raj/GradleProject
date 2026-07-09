plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation("org.flywaydb:flyway-core:10.22.0")
    testImplementation("org.flywaydb:flyway-mysql:10.22.0")
    testImplementation("com.mysql:mysql-connector-j:9.4.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed", "skipped")
    }
}