package Operations;

import Exceptions.ReassignmentParameter;
import Exceptions.WrongArgsPush;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefineParameters implements CalcOperation {
    private final static Logger logger = LoggerFactory.getLogger(DefineParameters.class);
    public void execute(Queue<Double> stack, Map<String, Double> defineMap, String[] args) {
        if (args.length == 3 && args[2].matches("\\d+") && args[1].matches("[a-zA-Z]+")) {
            if (defineMap.containsKey(args[1])) {
                logger.error("Попытка переназначить параметр");
                throw new ReassignmentParameter("Попытка переназанчить существующий параметр");

            }
            defineMap.put(args[1], Double.valueOf(args[2]));
            logger.debug("Define {} -> {}", args[1], args[2]);
        } else {
            logger.error("Неверный аргумент для DEFINE");
            throw new WrongArgsPush("Команде DEFINE назначен неверный аргумент или он пустой");
        }
    }
}
