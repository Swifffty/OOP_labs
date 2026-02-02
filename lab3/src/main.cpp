#include <iostream>
#include "Parser/CommandLineParser.h"
#include "SoundProcessor.h"
#include "ConverterFactory.h"
#include "exceptions/CLIException.h"
#include "exceptions/ConfigException.h"
#include "exceptions/FileException.h"

int main(int argc, char** argv) {
    try {
        CommandLineParser cli(argc, argv);

        ConverterFactory factory;

        if (cli.helpRequested()) {
            factory.printAllHelp();
            return 0;
        }

        SoundProcessor processor(cli.getConfigPath(), cli.getOutputPath(), cli.getInputFiles());
        
        processor.run(factory);

    } catch (const CLIException& e) {
        std::cerr << "CLI Error: " << e.what() << std::endl;
        return 1;
    } catch (const ConfigException& e) {
        std::cerr << "Config Error: " << e.what() << std::endl;
        return 2;
    } catch (const FileException& e) {
        std::cerr << "File Error: " << e.what() << std::endl;
        return 3;
    } catch (const std::exception& e) {
        std::cerr << "Unexpected Error: " << e.what() << std::endl;
        return 4;
    }

    return 0;
}