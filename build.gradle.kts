plugins {
    id("marine.library-conventions")
    id("io.freefair.lombok") version "8.0.0-rc2"
    id("io.spring.dependency-management") version "1.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.locationtech.spatial4j:spatial4j:0.8")
    implementation("tech.uom.lib:uom-lib-jackson:2.1")
}