plugins {
    java
    jacoco
    id("org.springframework.boot") version "2.7.17"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.xedlab"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.30"
val mapstructVersion = "1.5.5.Final"
val postgresqlVersion = "42.7.3"
val liquibaseVersion = "4.25.0"

val springdocVersion = "1.7.0"

val assertjVersion = "3.22.0"


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.postgresql:postgresql:${postgresqlVersion}:")

    implementation("org.liquibase:liquibase-core:${liquibaseVersion}")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")

    implementation("org.springdoc:springdoc-openapi-ui:${springdocVersion}")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:${assertjVersion}")

}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules {
        rule {
            isEnabled = true
            element = "CLASS"
            includes = listOf(
                "com.xedlab.usersService.controller.UserController",
                "com.xedlab.usersService.controller.SubscribeController",
                "com.xedlab.usersService.controller.HardSkillController",

                "com.xedlab.usersService.domain.users.DefaultUserService",
                "com.xedlab.usersService.domain.users.DefaultCheckExistsUserService",
//                "com.xedlab.usersService.domain.users.UserMapperImpl",

                "com.xedlab.usersService.domain.hardSkill.DefaultHardSkillService",
//                "com.xedlab.usersService.domain.hardSkill.HardSkillMapperImpl",

                "com.xedlab.usersService.domain.subscribe.DefaultSubscribeService",
//                "com.xedlab.usersService.domain.subscribe.HardSkillMapperImpl",
            )

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}
tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

tasks.build {
    dependsOn(tasks.check)
}