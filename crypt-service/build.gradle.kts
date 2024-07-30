plugins {
    id("buildlogic.kotlin-application-conventions")
}

group = "com.fleetmate.crypt"
version = "0.0.1"

dependencies {
    implementation(project(":lib"))
}

application {
    mainClass.set("com.fleetmate.crypt.ApplicationKt")
}

tasks.named("build") {
    doLast {
        delete("$rootDir/docker/jvm-services/dist/crypt-service.jar")
        copy {
            from("$rootDir/crypt-service/build/libs/crypt-service-all.jar")
            into("$rootDir/docker/jvm-services/dist")
            rename {
                "crypt-service.jar"
            }
        }
    }
}