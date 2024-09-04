plugins {
    id("buildlogic.kotlin-application-conventions")
}

group = "com.fleetmate.file"
version = "0.0.1"

dependencies {
    implementation(project(":lib"))
}

application {
    mainClass.set("com.fleetmate.file.ApplicationKt")
}

tasks.named("build") {
    doLast {
        delete("$rootDir/docker/jvm-services/dist/file-service.jar")
        copy {
            from("$rootDir/file-service/build/libs/file-service-all.jar")
            into("$rootDir/docker/jvm-services/dist")
            rename {
                "file-service.jar"
            }
        }
    }
}