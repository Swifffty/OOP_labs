package Operations;

import java.util.ArrayDeque;
import java.util.Map;
import Exceptions.WrongArgsPush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Push implements CalcOperation{
    private final static Logger logger = LoggerFactory.getLogger(Push.class);
    public void execute(ArrayDeque<Double> numbers, Map<String, Double> defineMap, String[] args) {
        if (args.length > 1) {
            if (args[1].matches("\\d+")) {
                numbers.push(Double.valueOf(args[1]));
            } else if (defineMap.containsKey(args[1])) {
                numbers.push(defineMap.get(args[1]));
            }
            logger.debug("Положил на стек: {}", args[1]);
        } else {
            logger.error("Неверный аргмент");
            throw new WrongArgsPush("Команде Push назначен неверный аргумент или он пустой");
        }
    }
}
