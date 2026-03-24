import Exceptions.ConfigException;


import java.io.IOException;


public class Main {
    static void main(String[] args) {
        String ConfigName = "config.txt";
        try (Calculate calc = (args.length == 0) ? new Calculate(ConfigName) : new Calculate(args[0], ConfigName)){
            calc.do_calculate();
        } catch (ConfigException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
