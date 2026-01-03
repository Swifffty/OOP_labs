#include "CommandLineParser.h"
#include "../exceptions/CLIException.h"
#include <iostream>

CommandLineParser::CommandLineParser(int argc, char** argv) {
    parse(argc, argv);
}

void CommandLineParser::parse(int argc, char** argv) {
    if (argc < 2) {
        throw CLIException("Insufficient arguments. Use -h for help.");
    }

    std::string firstArg = argv[1];

    if (firstArg == "-h") {
        _helpRequested = true;
        return;
    }

    if (firstArg == "-c") {

        if (argc < 5) {
            throw CLIException("Missing arguments for -c flag.\nUsage: sound_processor -c <config.txt> <output.wav> <input1.wav> [inputs...]");
        }

        _configPath = argv[2];
        _outputPath = argv[3];

        for (int i = 4; i < argc; ++i) {
            _inputFiles.push_back(argv[i]);
        }
    } else {
        throw CLIException("Unknown flag: " + firstArg + ". Use -h for help.");
    }
}

bool CommandLineParser::helpRequested() const {
    return _helpRequested;
}
std::string CommandLineParser::getConfigPath() const {
    return _configPath;
}
std::string CommandLineParser::getOutputPath() const {
    return _outputPath;
}
const std::vector<std::string>& CommandLineParser::getInputFiles() const {
    return _inputFiles;
}
