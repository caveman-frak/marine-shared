plugins {
    id("marine.library-conventions")
}

dependencies {
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.locationtech.spatial4j:spatial4j:0.8")
    implementation("tech.uom.lib:uom-lib-jackson:2.1")
    implementation("com.github.dtmo.jfiglet:jfiglet:1.0.1")
}