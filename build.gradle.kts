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
    mavenCentral()
    maven {
        url = URI.create("https://repo.enonic.com/public/")
    }
    maven {
        url = URI.create("https://maven.abstractolotl.de/snapshots/")
    }
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.22")
    testImplementation("org.projectlombok:lombok:1.18.22")

    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("com.google.common:google-collect:0.5")
    implementation("org.reflections:reflections:0.10.2")
    testImplementation("org.reflections:reflections:0.10.2")

    implementation( "org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("de.ancozockt:aoclib:3.0.0-SNAPSHOT")
    testImplementation("de.ancozockt:aoclib:3.0.0-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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

@AInputData(day = $newDayNum, year = 2023)
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
