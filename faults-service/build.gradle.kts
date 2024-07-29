plugins {
    id("buildlogic.kotlin-application-conventions")
}

group = "com.fleetmate.faults"
version = "0.0.1"

dependencies {
    implementation(project(":lib"))
}

application {
    mainClass.set("com.fleetmate.faults.ApplicationKt")
}