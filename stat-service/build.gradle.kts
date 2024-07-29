plugins {
    id("buildlogic.kotlin-application-conventions")
}

//group = "com.fleetmate.stat"
version = "0.0.1"

dependencies {
    implementation(project(":lib"))
}

application {
    mainClass.set("com.fleetmate.stat.ApplicationKt")
}