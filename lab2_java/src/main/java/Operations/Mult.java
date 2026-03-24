package Operations;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;

import Exceptions.FewElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mult implements CalcOperation{
    private final static Logger logger = LoggerFactory.getLogger(Mult.class);
    public void execute(ArrayDeque<Double> stack, Map<String, Double> defineMap, String[] args) throws FewElements {
        try {
            Double a = stack.pop();
            Double b = stack.pop();
            logger.debug("{} * {}", b, a);
            Double result = a * b;
            logger.debug("Result: {}", result);
            stack.push(result);
        } catch (NoSuchElementException e) {
            logger.error("недостаточно элементов на стеке");
            throw new FewElements("не достаточно элементов на стеке для выполнения операции '*'");
        }
    }
}
