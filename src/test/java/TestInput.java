import de.ancozockt.aoclib.annotations.AInputData;
import de.ancozockt.aoclib.interfaces.IInputHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestInput implements IInputHelper {

    private String fileName;

    public TestInput(AInputData inputData){
        this.fileName = "day" + inputData.day() + "-input";
    }

    @Override
    public BufferedReader getInput() {
        InputStream ioStream = getClass()
            .getClassLoader()
            .getResourceAsStream("input/" + fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }

        return new BufferedReader(new InputStreamReader(ioStream));
    }

    @Override
    public String getInputAsString() {
        return getInput().lines().collect(Collectors.joining("\n"));
    }

    @Override
    public Stream<String> getInputAsStream() {
        return getInput().lines();
    }
}
