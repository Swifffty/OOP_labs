package Operations;

import Exceptions.FewElements;
import Exceptions.SqrtByNegative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;
import java.lang.Math;

public class Sqrt implements CalcOperation {
    private final static Logger logger = LoggerFactory.getLogger(Sqrt.class);
    public void execute(ArrayDeque<Double> stack, Map<String, Double> defineMap, String[] args) throws FewElements, SqrtByNegative {
        try {
            Double a = stack.pop();
            logger.debug("Корень из {}", a);
            a = Math.sqrt(a);
            logger.debug("Result: {}", a);
            if (Double.compare(a, 0.0) == -1) {
                logger.error("Попытка извлечь корень из отрицательного числа");
                throw new SqrtByNegative("Попытка извлечь корень из отрицательного числа!");
            }
            stack.push(a);
        } catch (NoSuchElementException e) {
            logger.error("недостаточно элементов на стеке");
            throw new FewElements("недостаточно элементов на стеке для выполнения операции 'SQRT'");
        }
    }
}

