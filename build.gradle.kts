plugins {
    id("marine.library-conventions")
}

//val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
//    implementation("org.springframework.data:spring-data-commons")
    api(libs.spring.data)
//    implementation("org.springframework.boot:spring-boot-starter-web")
    api(libs.spring.boot.web)
//    api("tech.units:indriya:2.2")
//    api("tech.uom.lib:uom-lib-jackson:2.1")
//    api("tech.uom.lib:uom-lib-assertj:2.1")
    api(libs.bundles.uom)
//    api("com.uber:h3:4.1.1")
//    api("org.locationtech.spatial4j:spatial4j:0.8")
//    api("org.locationtech.jts:jts-core:1.20.0")
    api(libs.bundles.spatial)
//    api("com.github.dtmo.jfiglet:jfiglet:1.0.1")
    api(libs.figlet)
//    implementation("net.agkn:hll:1.6.0")
//    implementation("com.google.guava:guava:33.3.1-jre")

//    testFixturesImplementation("org.springframework.boot:spring-boot-starter-web")
//    testFixturesImplementation("org.springframework.data:spring-data-jpa")
//    testFixturesImplementation("jakarta.persistence:jakarta.persistence-api")
//    testFixturesImplementation("org.locationtech.spatial4j:spatial4j:0.8")
    testFixturesImplementation(libs.spring.data.jpa)
    testFixturesImplementation(libs.jakarta.persistence)

    testImplementation(libs.uom.assetyj)
//    testFixturesImplementation(libs.uom.assetyj)
//    testFixturesImplementation(project())
}