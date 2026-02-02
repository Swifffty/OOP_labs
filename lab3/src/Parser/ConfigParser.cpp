#include "ConfigParser.h"
#include "exceptions/ConfigException.h"
#include <sstream>

ConfigParser::ConfigParser(const std::string& filename) {
    _filename = filename;
}

std::vector<Instruction> ConfigParser::parse() {
    std::ifstream file(_filename);
    if (!file.is_open()) {
        throw ConfigException("Could not open config file: " + _filename);
    }

    std::vector<Instruction> instructions;
    std::string line;
    while (std::getline(file, line)) {
        if (line.empty() || line[0] == '#') {
            continue;
        }

        std::stringstream ss(line);
        Instruction instr;
        ss >> instr.command;

        std::string arg;
        while (ss >> arg) {
            instr.args.push_back(arg);
        }
        instructions.push_back(instr);
    }
    return instructions;
}