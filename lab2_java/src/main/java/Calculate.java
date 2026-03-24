import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque
import java.util.Queue;;

import Exceptions.*;
import Operations.CalcOperation;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;

import Operations.InitOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Exceptions.ConfigException;

public class Calculate implements AutoCloseable {
    private final static Logger logger = LoggerFactory.getLogger(Calculate.class);
    private final Queue<Double> stack;
    private final Map<String, CalcOperation> operation;
    private final Map<String, Double> defineMap;
    private final BufferedReader comands;

    public Calculate(String arg1, String arg2) throws ConfigException {
        logger.info("Открытите файла с выражениями..");
        try  {
            FileReader file = new FileReader(arg1);
            comands = new BufferedReader(file);
            InitOperations operations = new InitOperations();
            operations.ReadConfig(arg2);
            operation = operations.getOperationMap();
            stack = new ArrayDeque<>();
            defineMap = new HashMap<>();
        } catch (Exception e) {
            throw new ConfigException(e.getMessage());
        }
    }
    public Calculate(String arg) throws ConfigException  {
        logger.info("Открытие потока ввода через консоль");
        InputStreamReader file = new InputStreamReader(System.in);
        comands = new BufferedReader(file);
        try {
            InitOperations operations = new InitOperations();
            operations.ReadConfig(arg);
            operation = operations.getOperationMap();
            stack = new ArrayDeque<>();
            defineMap = new HashMap<>();
        } catch (Exception e) {
            throw new ConfigException(e.getMessage());
        }
    }


    public void do_calculate(){
        logger.info("начало вычислений...");
        try {
            String line;
            line = comands.readLine();
            while (line != null) {
                String[] args = line.split(" ");
                logger.debug("Обработка операции: {}", line);
                if (!operation.containsKey(args[0])) {
                    logger.error("Операция не найдена");
                    throw new NoOperationsException("Неизвестная операция");
                }
                CalcOperation op = operation.get(args[0]);
                op.execute(stack, defineMap, args);
                line = comands.readLine();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @Override
    public void close() throws IOException {
        if (comands != null) {
            logger.info("Закрытие ресурсов");
            comands.close();
        }
    }
}
