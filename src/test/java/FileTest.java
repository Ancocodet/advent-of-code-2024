import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
public class FileTest {

    @Test
    public void testInputs(){
        String path = "src/test/resources/input";

        File file = new File(path);

        assertTrue(file.exists());
    }

    @Test
    public void testOutputs(){
        String path = "src/test/resources/output";

        File file = new File(path);
        assertTrue(file.exists());
    }

}
