rootProject.name = "structurizr-d2-exporter"
include("lib")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("spotbugs", "com.github.spotbugs").version("6.0.26")
            plugin("git-versioning", "me.qoomon.git-versioning").version("6.4.4")
            plugin("kotlin", "org.jetbrains.kotlin.jvm").version("2.1.0")
            plugin("dokka", "org.jetbrains.dokka").version("2.0.0-Beta")
            library("structurizr-export", "com.structurizr", "structurizr-export").version("3.2.0")
            library("structurizr-dsl", "com.structurizr", "structurizr-dsl").version("3.2.0")
            library("junit-jupyter", "org.junit.jupiter", "junit-jupiter").version("5.11.3")
        }
    }
}
