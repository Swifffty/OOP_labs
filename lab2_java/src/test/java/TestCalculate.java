import Operations.CalcOperation;
import Operations.Pop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import Operations.InitOperations;

public class TestCalculate {
@Test
    public void TestInitOperations() {
        try (Calculate TestCalc = new Calculate("TestFile.txt", "EmptyConfig.txt")){
            PrintStream originalErr = System.err;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream captureStream = new PrintStream(baos);
            System.setErr(captureStream);
            TestCalc.do_calculate();
            String capturedText = baos.toString();
            assertEquals("Неизвестная операция\n",capturedText);
            System.setErr(originalErr);
            captureStream.flush();
            baos.reset();

            InitOperations TestWithCommands = new InitOperations();
            TestWithCommands.ReadConfig("Config.txt");
            Map<String, CalcOperation> result = TestWithCommands.getOperationMap();
            assertNotNull(result.get("POP"));
            assertEquals(Pop.class, result.get("POP").getClass());


            System.setErr(captureStream);
            InitOperations WithoutConfig = new InitOperations();
            WithoutConfig.ReadConfig("NoConfig.txt123");
            String TextException = baos.toString();
            assertEquals("Конфиг файл не найден\n", TextException);
            System.setErr(originalErr);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @Test
    public void CalculateTest() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(baos);
        System.setErr(captureStream);
        try (Calculate TestCalc = new Calculate("NonExistFile234123", "Config.txt")) {
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        assertEquals("NonExistFile234123 (No such file or directory)\n", baos.toString());

        System.setErr(originalErr);
        captureStream.flush();
        baos.reset();
        PrintStream originOut = System.out;
        System.setOut(captureStream);

        try (Calculate TestCalc = new Calculate("TestFile.txt", "Config.txt")) {
            TestCalc.do_calculate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        assertEquals("3.0\n", baos.toString());

        captureStream.flush();
        baos.reset();
        try (Calculate TestCalc = new Calculate("AllOperations.txt", "Config.txt")) {
            TestCalc.do_calculate();
            assertEquals("4.0\n", baos.toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.setOut(originOut);
    }

}
