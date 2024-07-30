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

tasks.named("build") {
    doLast {
        delete("$rootDir/docker/jvm-services/dist/faults-service.jar")
        copy {
            from("$rootDir/faults-service/build/libs/faults-service-all.jar")
            into("$rootDir/docker/jvm-services/dist")
            rename {
                "faults-service.jar"
            }
        }
    }
}