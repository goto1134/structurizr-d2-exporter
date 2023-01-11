plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.spotbugs)
    alias(libs.plugins.git.versioning)
}

description = "Exports Structurizr models and views to D2 format"
group = "io.github.goto1134"
version = "1.0.1-SNAPSHOT"

gitVersioning.apply {
    refs {
        tag("v(?<version>.*)") {
            version = "\${ref.version}"
        }
    }
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation(libs.structurizr.export)
    implementation(libs.structurizr.core)
    testImplementation(libs.structurizr.client)
    testImplementation(libs.junit.jupyter)
}

base {
    archivesName.set(rootProject.name)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to rootProject.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "goto1134"
            )
        )
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven {
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            name = "OSSRH"
            credentials {
                val ossrhUsername: String? by project
                val ossrhPassword: String? by project
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = base.archivesName.get()

            pom {
                name.set("Structurizr D2 Exporter")
                description.set(project.description)
                url.set("https://github.com/goto1134/structurizr-d2-exporter")
                scm {
                    connection.set("scm:https://github.com/goto1134/structurizr-d2-exporter.git")
                    developerConnection.set("scm:git@github.com:goto1134/structurizr-d2-exporter.git")
                    url.set("https://github.com/goto1134/structurizr-d2-exporter.git")
                }

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("goto1134")
                        name.set("Andrey Yefanov")
                        email.set("1134togo@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

spotbugs {
    omitVisitors.set(listOf(
        // This project forces \n instead of system-specific line separators
        // https://spotbugs.readthedocs.io/en/stable/detectors.html#formatstringchecker
        "FormatStringChecker"
    ))
}