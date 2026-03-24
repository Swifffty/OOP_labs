package Operations;

import java.util.ArrayDeque;
import java.util.Map;

public interface CalcOperation {
    void execute(ArrayDeque<Double> stack, Map<String, Double> defineMap, String[] args); // односторонняя очередь
}
