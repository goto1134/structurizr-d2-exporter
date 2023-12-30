rootProject.name = "structurizr-d2-exporter"
include("lib")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("spotbugs", "com.github.spotbugs").version("6.0.4")
            plugin("git-versioning", "me.qoomon.git-versioning").version("6.4.3")
            plugin("kotlin", "org.jetbrains.kotlin.jvm").version("1.9.22")
            plugin("dokka", "org.jetbrains.dokka").version("1.9.10")
            library("structurizr-export", "com.structurizr", "structurizr-export").version("1.19.0")
            library("structurizr-client", "com.structurizr", "structurizr-client").version("1.29.0")
            library("junit-jupyter", "org.junit.jupiter", "junit-jupiter").version("5.10.1")
        }
    }
}
