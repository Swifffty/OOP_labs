package Operations;

import Exceptions.DivByZero;
import Exceptions.FewElements;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Div implements CalcOperation {
    private final static Logger logger = LoggerFactory.getLogger(Div.class);
    public void execute(Queue<Double> stack, Map<String, Double> defineMap, String[] args) throws FewElements, DivByZero {
        try {
            Double a = stack.pop();
            Double b = stack.pop();
            logger.debug("{} / {}", a, b);
            if (Double.compare(b, 0.0) == 0) {
                logger.error("Деление на ноль");
                throw new DivByZero("Деление на ноль!");
            }
            Double result = a / b;
            logger.debug("Result: {}", result);
            stack.push(result);
        } catch (NoSuchElementException e) {
            logger.error("недостаточно элементов на стеке");
            throw new FewElements("недостаточно элементов на стеке для выполнения операции '/'");
        }
    }
}

