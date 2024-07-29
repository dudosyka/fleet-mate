val kotlinVersion: String by project

val ktorVersion: String by project
val kodeinVersion: String by project

val exposedVersion: String by project
val hikaricpVersion: String by project
val jdbcPostgresVersion: String by project


val jbcryptVersion: String by project
val qrcodeKotlinVersion: String by project
val apachePoiVersion: String by project

plugins {
    id("buildlogic.kotlin-common-conventions")

    application
}

group = "com.fleetmate"
version = "0.0.1"

repositories {
    mavenCentral()
}

application {
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    fun ktor(part: String, module: String) = "io.ktor:ktor-$part-$module-jvm:$ktorVersion"
    fun ktorServer(module: String) = ktor(part = "server", module = module)
    fun ktorClient(module: String) = ktor(part = "client", module = module)
    fun jetBrains(module: String, version: String) = "org.jetbrains.$module:$version"
    fun kotlin(module: String) = jetBrains("kotlin:kotlin-$module", kotlinVersion)

    //Xls lib
    implementation("org.apache.poi:poi:$apachePoiVersion")
    implementation("org.apache.poi:poi-ooxml:$apachePoiVersion")

    //QR-generation
    implementation("io.github.g0dkar:qrcode-kotlin:$qrcodeKotlinVersion")
}
