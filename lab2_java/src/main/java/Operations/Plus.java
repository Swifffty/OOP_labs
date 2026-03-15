package Operations;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;
import Exceptions.FewElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plus implements CalcOperation{
    private final static Logger logger = LoggerFactory.getLogger(Plus.class);
    public void execute(ArrayDeque<Double> numbers, Map<String, Double> defineMap, String[] args) throws FewElements {
        try {
            Double a = numbers.pop();
            Double b = numbers.pop();
            logger.debug("{} + {}", b, a);
            Double result = a + b;
            logger.debug("Result: {}", result);
            numbers.push(result);
        } catch (NoSuchElementException e) {
            logger.error("недостаточно элементов на стеке");
            throw new FewElements("недостаточно элементов на стеке для выполнения операции '+'");
        }
    }
}
