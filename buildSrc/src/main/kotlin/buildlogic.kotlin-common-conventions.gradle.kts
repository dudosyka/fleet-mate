val kotlinVersion: String by project
val kotlinxCoroutinesVersion: String by project

val ktorVersion: String by project
val kodeinVersion: String by project
val jbcryptVersion: String by project

val logbackVersion: String by project
val prometeusVersion: String by project

val exposedVersion: String by project
val hikaricpVersion: String by project
val jdbcPostgresVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    fun jetBrains(module: String, version: String) = "org.jetbrains.$module:$version"
    fun kotlin(module: String) = jetBrains("kotlin:kotlin-$module", kotlinVersion)
    fun ktor(part: String, module: String) = "io.ktor:ktor-$part-$module-jvm:$ktorVersion"
    fun ktorServer(module: String) = ktor(part = "server", module = module)

    //Ktor server
    implementation(ktorServer("host-common"))
    implementation(ktorServer("status-pages"))
    implementation(ktorServer("call-logging"))
    implementation(ktorServer("call-id"))
    implementation(ktorServer("metrics-micrometer"))
    implementation(ktorServer("content-negotiation"))
    implementation(ktorServer("netty"))
    implementation(ktorServer("compression"))
    implementation(ktorServer("cors"))
    implementation(ktor("serialization", "kotlinx-json"))
    implementation(ktorServer("core"))
    implementation(ktorServer("auth"))
    implementation(ktorServer("auth-jwt"))
    implementation(ktorServer("websockets"))
    testImplementation(ktorServer("tests"))

    //Database
    api(jetBrains("exposed:exposed-core", exposedVersion))
    api(jetBrains("exposed:exposed-dao", exposedVersion))
    api(jetBrains("exposed:exposed-jdbc", exposedVersion))
    api(jetBrains("exposed:exposed-java-time", exposedVersion))
    implementation("com.zaxxer:HikariCP:$hikaricpVersion")
    implementation("org.postgresql:postgresql:$jdbcPostgresVersion")

    //DI
    implementation("org.kodein.di:kodein-di-jvm:$kodeinVersion")

    //Tests
    testImplementation(kotlin("test-junit"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //Logging and metrics
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometeusVersion")

    //Kotlinx coroutines
    runtimeOnly(kotlin("reflect", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation(jetBrains("kotlinx:kotlinx-coroutines-core", kotlinxCoroutinesVersion))

    //Crypto
    implementation("org.mindrot:jbcrypt:$jbcryptVersion")

    implementation("org.reflections:reflections:0.9.12")
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
