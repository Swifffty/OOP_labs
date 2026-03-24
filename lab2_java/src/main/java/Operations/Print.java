package Operations;

import Exceptions.FewElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;

public class Print implements CalcOperation {
    private final static Logger logger = LoggerFactory.getLogger(Plus.class);
    public void execute(ArrayDeque<Double> stack, Map<String, Double> defineMap, String[] args) throws FewElements {
        try {
            Double a = stack.peekFirst();
            logger.debug("Вывел: {}", a);
            System.out.println(a);
        } catch (NoSuchElementException e) {
            logger.error("недостаточно элементов на стеке");
            throw new FewElements("недостаточно элементов на стеке для выполнения операции 'PRINT'");
        }
    }
}
