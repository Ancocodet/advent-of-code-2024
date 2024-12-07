package com.ancozockt.advent;

import de.ancozockt.aoclib.InputHelper;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        if(!System.getenv().containsKey("AOC_TOKEN")){
            System.exit(-1);
            return;
        }

        Reflections reflections = new Reflections("com.ancozockt.advent.days");

        File folder = new File("output");
        if(!folder.exists()){
            folder.mkdirs();
        }

        reflections.getTypesAnnotatedWith(AInputData.class).forEach(aClass -> {
            AInputData inputData = aClass.getAnnotation(AInputData.class);
            IAdventDay adventDay = (IAdventDay) createNewInstanceOfClass(aClass);

            if (inputData.skip()) {
                System.out.println("Skipping calculation for " + inputData.day() + "/" + inputData.year());
                return;
            }

            System.out.println("Starting calculation for " + inputData.day() + "/" + inputData.year());

            File file = new File("output", "day-" + inputData.day() + ".txt");
            if(!file.exists()){
                try {
                    file.createNewFile();
                }catch (IOException ignored){}
            }

            InputHelper inputHelper = new InputHelper(inputData);
            try {
                FileWriter fileWriter = new FileWriter(file);

                fileWriter.write("Part1: " + Objects.requireNonNull(adventDay)
                    .part1(inputHelper));

                fileWriter.write("\nPart2: " + Objects.requireNonNull(adventDay)
                    .part2(inputHelper));

                fileWriter.close();
            } catch (IOException ignored) { }
        });
    }

    private static <T> T createNewInstanceOfClass(Class<T> someClass) {
        try {
            return someClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}
