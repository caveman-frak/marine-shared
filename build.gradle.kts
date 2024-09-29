plugins {
    id("marine.library-conventions")
}

dependencies {
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-web")
    api("tech.units:indriya:2.2")
    api("tech.uom.lib:uom-lib-jackson:2.1")
    api("tech.uom.lib:uom-lib-assertj:2.1")
    api("com.uber:h3:4.1.1")
    api("org.locationtech.spatial4j:spatial4j:0.8")
    api("org.locationtech.jts:jts-core:1.20.0")
    api("com.github.dtmo.jfiglet:jfiglet:1.0.1")
    implementation("net.agkn:hll:1.6.0")
    implementation("com.google.guava:guava:33.3.1-jre")
    testFixturesImplementation("com.uber:h3:4.1.1")
    testFixturesImplementation("org.locationtech.spatial4j:spatial4j:0.8")
}