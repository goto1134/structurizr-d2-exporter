import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
    alias(libs.plugins.git.versioning)
    `maven-publish`
    signing
}

description = "Exports Structurizr models and views to D2 format"
group = "io.github.goto1134"
version = "1.0.1-SNAPSHOT"

gitVersioning.apply {
    refs {
        tag("v(?<version>.*)") {
            version = "\${ref.version}"
        }
        branch("main") {
            describeTagPattern = "v(?<version>.*)"
            version = "\${describe.tag.version}+\${describe.distance}.\${commit.short}-SNAPSHOT"
        }
    }
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.structurizr.export)
    testImplementation(libs.structurizr.client)
    testImplementation(libs.junit.jupyter)
    testImplementation(kotlin("test-junit5"))
}

base {
    archivesName.set(rootProject.name)
}

val dokkaJavadocJar = tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
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
            artifact(dokkaJavadocJar)
            artifact(tasks.kotlinSourcesJar)

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
                        name.set("Andrey Efanov")
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
