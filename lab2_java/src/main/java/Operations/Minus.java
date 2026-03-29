package Operations;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import Exceptions.FewElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Minus implements CalcOperation {
    private final static Logger logger = LoggerFactory.getLogger(DefineParameters.class);
    public void execute(Queue<Double> stack, Map<String, Double> defineMap, String[] args) throws FewElements {
        try {
            Double a = stack.pop();
            Double b = stack.pop();
            logger.debug("{} - {}", b, a);
            Double result = a - b;
            logger.debug("Result: {}", result);
            stack.push(result);
        } catch (NoSuchElementException e) {
            logger.error("недостаточно элементов на стеке");
            throw new FewElements("недостаточно элементов на стеке для выполнения операции '-'");
        }
    }
}
