package Operations;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

public interface CalcOperation {
    void execute(Queue<Double> stack, Map<String, Double> defineMap, String[] args);
}
