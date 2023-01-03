rootProject.name = "structurizr-d2-exporter"
include("lib")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("spotbugs", "com.github.spotbugs").version("5.0.13")
            plugin("git-versioning", "me.qoomon.git-versioning").version("6.3.6")
            library("structurizr-export", "com.structurizr", "structurizr-export").version("1.7.0")
            library("structurizr-core", "com.structurizr", "structurizr-core").version("1.16.1")
            library("structurizr-client", "com.structurizr", "structurizr-client").version("1.16.1")
            library("junit-jupyter", "org.junit.jupiter", "junit-jupiter").version("5.9.1")
        }
    }
}
