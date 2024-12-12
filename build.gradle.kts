import java.net.URI

plugins {
    id("java")
    id("application")
}

group = "com.ancozockt"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.ancozockt.advent.Main")
}

repositories {
    mavenCentral()
    maven {
        url = URI.create("https://maven.abstractolotl.de/releases/")
    }
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.36")
    testImplementation("org.projectlombok:lombok:1.18.36")

    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.reflections:reflections:0.10.2")
    testImplementation("org.reflections:reflections:0.10.2")

    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-collections4:4.5.0-M2")

    implementation("de.ancozockt:aoclib:4.1.0")
    testImplementation("de.ancozockt:aoclib:4.1.0")

    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("ch.qos.logback:logback-classic:1.5.12")
    testImplementation("ch.qos.logback:logback-classic:1.5.12")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

task("generateNextDay") {
    doLast {
        val prevDayNum = fileTree("$projectDir/src/main/java/com/ancozockt/advent/days").matching {
            include("Day*.java")
        }.maxOf {
            val (prevDayNum) = Regex("Day(\\d+)").find(it.name)!!.destructured
            prevDayNum.toInt()
        }
        val newDayNum = prevDayNum + 1;
        File("$projectDir/src/main/java/com/ancozockt/advent/days", "Day$newDayNum.java").writeText(
            """
package com.ancozockt.advent.days;

import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import de.ancozockt.aoclib.interfaces.IInputHelper;

@AInputData(day = $newDayNum, year = 2024)
public class Day$newDayNum implements IAdventDay {

    @Override
    public String part1(IInputHelper inputHelper) {
        return null;
    }

    @Override
    public String part2(IInputHelper inputHelper) {
        return null;
    }

}
"""
        )

        // Generate input file
        File("$projectDir/src/test/resources/input", "day$newDayNum-input").writeText(
            """#Input data for day $newDayNum#
"""
        )

        // Generate output file
        File("$projectDir/src/test/resources/output", "day$newDayNum-output").writeText(
            """#nottestable#
#nottestable#
"""
        )
    }
}
