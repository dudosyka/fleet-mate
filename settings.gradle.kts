plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

fun service(name: String) = "$name-service"

rootProject.name = "fleet-mate"
include(service("trip"))
include(service("stat"))
include(service("faults"))
include("lib")
include(service("crypt"))
