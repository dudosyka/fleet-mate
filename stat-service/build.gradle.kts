plugins {
    id("buildlogic.kotlin-application-conventions")
}

group = "com.fleetmate.stat"
version = "0.0.1"

dependencies {
    implementation(project(":lib"))
}

application {
    mainClass.set("com.fleetmate.stat.ApplicationKt")
}

tasks.named("build") {
    doLast {
        delete("$rootDir/docker/jvm-services/dist/stat-service.jar")
        copy {
            from("$rootDir/stat-service/build/libs/stat-service-all.jar")
            into("$rootDir/docker/jvm-services/dist")
            rename {
                "stat-service.jar"
            }
        }
    }
}