package com.ancozockt.advent;

import de.ancozockt.aoclib.InputHelper;
import de.ancozockt.aoclib.ReflectionHelper;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        if(!System.getenv().containsKey("AOC_TOKEN")){
            System.exit(-1);
            return;
        }

        File folder = new File("output");
        if(!folder.exists()){
            folder.mkdirs();
        }

        sortedDays().forEach(execution -> {
            AInputData inputData = execution.inputData();
            IAdventDay adventDay = execution.adventDay();

            if (inputData.skip()) {
                log.info("Skipping calculation for {}/{}", inputData.day(), inputData.year());
                return;
            }

            log.info("Starting calculation for {}/{}", inputData.day(), inputData.year());

            saveOutput(inputData.day(),
                    adventDay.part1(new InputHelper(inputData)),
                    adventDay.part2(new InputHelper(inputData)));
        });
    }

    private static List<ReflectionHelper.AdventDayExecution> sortedDays(){
        List<ReflectionHelper.AdventDayExecution> executions = ReflectionHelper.getAdventDays("com.ancozockt.advent.days");

        executions.sort(Comparator.comparingInt(execution -> execution.inputData().day()));

        return executions;
    }

    private static void saveOutput(int day, String part1, String part2) {
        File file = new File("output", "day-" + day + ".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException ignored){}
        }

        try {
            FileWriter fileWriter = new FileWriter(file);

            fileWriter.write("Part1: " + part1);
            fileWriter.write("\nPart2: " + part2);

            fileWriter.close();
        } catch (IOException ignored) { }
    }

}
