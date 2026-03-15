import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;


public class Main {
    static void main(String[] args) {
            try (Calculate calc = (args.length == 0) ? new Calculate() : new Calculate(args[0])){
                calc.do_calculate();
            } catch (IOException | NullPointerException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException e) {
                System.err.println(e.getMessage());
            } catch (ArithmeticException | NoSuchElementException | ClassNotFoundException |
                     IllegalArgumentException e) {
                System.out.println(e.getMessage());

        }
    }
}
