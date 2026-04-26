package Operations;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitOperations{
    private final Map<String, CalcOperation> operationMap;
    private static final Logger logger = LoggerFactory.getLogger(InitOperations.class);

    public InitOperations() {
        operationMap = new HashMap<>();
        logger.info("Начало работы с конфигом");
    }

    private BufferedReader GetFile(InputStream configFactoryByte) {
        logger.info("Попытка открыть конфиг..");
        if (configFactoryByte == null) {
            logger.error("Не найден конфиг для фабрики");
            throw new NullPointerException("Конфиг файл не найден");
        }
        return new BufferedReader (new InputStreamReader(configFactoryByte, StandardCharsets.UTF_8));
    }

    public void ReadConfig(String ConfigName) throws Exception {
        logger.info("Чтение конфига...");
        String line;
        try (InputStream configFactoryByte = InitOperations.class.getResourceAsStream(ConfigName)){
            BufferedReader configFactory = GetFile(configFactoryByte);
            while ((line = configFactory.readLine()) != null) {
                String[] symbols = line.split(" ");
                try {
                    Class<?> OpClass = Class.forName(symbols[1]);
                    CalcOperation op_obj = (CalcOperation) OpClass.getDeclaredConstructor().newInstance();
                    operationMap.put(symbols[0], op_obj);
                    logger.info("Прочитан: {}", symbols[0]);
                } catch (ClassNotFoundException e) {
                    System.err.println(e.getMessage());
                    logger.warn("Неизвестная операция: {}", symbols[0]);
                }
            }
        } catch (IOException | NoSuchMethodException | InstantiationException | IllegalAccessException| InvocationTargetException e) {
            logger.error("ошибка чтения конфига");
            throw e;
        }
    }

    public Map<String, CalcOperation> getOperationMap() {
        return new HashMap<>(operationMap);
    }

}



