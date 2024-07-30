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

tasks.named("build") {
    doLast {
        delete("$rootDir/docker/jvm-services/dist/trip-service.jar")
        copy {
            from("$rootDir/trip-service/build/libs/trip-service-all.jar")
            into("$rootDir/docker/jvm-services/dist")
            rename {
                "trip-service.jar"
            }
        }
    }
}