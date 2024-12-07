import de.ancozockt.aoclib.ReflectionHelper;
import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IAdventDay;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public class DayTest {

    @ParameterizedTest
    @MethodSource("getDays")
    public void testDay(IAdventDay adventDay){
        AInputData inputData = adventDay.getClass().getAnnotation(AInputData.class);

        System.out.println("======= Testing Day: " + inputData.day() + " =======");

        String className = adventDay.getClass().getSimpleName();

        assert Integer.parseInt(className.replace("Day", "")) == inputData.day();
        assert inputData.year() == 2024;

        String[] outputs = readOutputs(inputData.day());

        assert outputs != null;
        System.out.println("Output does exist: ✓");

        TestInput testInput = new TestInput(inputData);
        try {
            if(!outputs[0].equalsIgnoreCase("#nottestable#")) {
                String part1 = adventDay.part1(testInput);
                try {
                    long answer = Long.parseLong(part1);
                    long expected = Long.parseLong(outputs[0]);

                    System.out.println("Part 1: " + answer + " | Expected: " + expected);

                    assert answer == expected;
                } catch (NumberFormatException exception) {
                    System.out.println("Part 1: " + part1 + " | Expected: " + outputs[0]);
                    assert part1.equals(outputs[0]);
                }
                System.out.println("Part-1: ✓");
            }else{
                System.out.println("Part-1: x (could not be tested)");
            }
        } catch (NullPointerException ignored){ }


        try{
            if(!outputs[1].equalsIgnoreCase("#nottestable#")){
                String part2 = adventDay.part2(testInput);
                try {
                    long answer = Long.parseLong(part2);
                    long expected = Long.parseLong(outputs[1]);

                    System.out.println("Part 2: " + answer + " | Expected: " + expected);
                    assert answer == expected;
                }catch (NumberFormatException exception){
                    System.out.println("Part 2: " + part2 + " | Expected: " + outputs[1]);
                    assert part2.equals(outputs[1]);
                }
                System.out.println("Part-2: ✓");
            } else {
                System.out.println("Part-2: x (could not be tested)");
            }
        }catch (NullPointerException ignored){ }
    }

    static Stream<IAdventDay> getDays(){
        return ReflectionHelper.getAdventDays("com.ancozockt.advent.days").stream()
                .sorted(Comparator.comparingInt(o -> o.inputData().day()))
                .map(ReflectionHelper.AdventDayExecution::adventDay);
    }

    private String[] readOutputs(int day){
        ArrayList<String> outputs = new ArrayList<>();

        String line;
        try (BufferedReader reader = readFromFile("output/day" + day + "-output")){
            while ((line = reader.readLine()) != null){
                outputs.add(line);
            }
        }catch (IOException ignored) { }

        return outputs.toArray(new String[]{});
    }

    private BufferedReader readFromFile(String fileName){
        InputStream ioStream = getClass()
            .getClassLoader()
            .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }

        return new BufferedReader(new InputStreamReader(ioStream));
    }

}
