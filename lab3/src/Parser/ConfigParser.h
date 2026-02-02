#ifndef CONFIG_PARSER_H
#define CONFIG_PARSER_H

#include <string>
#include <vector>
#include <fstream>

struct Instruction {
    std::string command;
    std::vector<std::string> args;
};

class ConfigParser {
public:
    explicit ConfigParser(const std::string& filename);
    std::vector<Instruction> parse();

private:
    std::string _filename;
};

#endif