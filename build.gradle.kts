plugins {
    id("marine.library-conventions")
}

dependencies {
    api(libs.spring.data)
    api(libs.spring.boot.web)
    api(libs.bundles.uom)
    api(libs.bundles.spatial)
    api(libs.figlet)
    api(libs.spring.boot.actuator)
    testFixturesImplementation(libs.spring.data.jpa)
    testFixturesImplementation(libs.jakarta.persistence)

    testImplementation(libs.uom.assetyj)
}