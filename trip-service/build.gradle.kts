plugins {
    id("buildlogic.kotlin-application-conventions")
}

group = "com.fleetmate.trip"
version = "0.0.1"

dependencies {
    implementation(project(":lib"))
}

application {
    mainClass.set("com.fleetmate.trip.ApplicationKt")
}