package Operations;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;
import Exceptions.PopEmptyStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pop implements CalcOperation {
    private final static Logger logger = LoggerFactory.getLogger(Pop.class);
    public void execute(ArrayDeque<Double> stack, Map<String, Double> defineMap, String[] args) {
        try{
        double pop = stack.removeFirst();
        logger.debug("Снят: {}", pop);
    } catch (NoSuchElementException e) {
            logger.error("недостаточно элементов на стеке");
            throw new PopEmptyStack("Попытка снять элемент с пустого стека!");
        }
    }
}
