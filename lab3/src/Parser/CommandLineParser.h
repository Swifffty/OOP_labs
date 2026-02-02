#ifndef COMMAND_LINE_PARSER_H
#define COMMAND_LINE_PARSER_H

#include <string>
#include <vector>

class CommandLineParser {
public:
    CommandLineParser(int argc, char** argv);

    bool helpRequested() const;
    std::string getConfigPath() const;
    std::string getOutputPath() const;
    const std::vector<std::string>& getInputFiles() const;

private:
    void parse(int argc, char** argv);

    bool _helpRequested = false;
    std::string _configPath;
    std::string _outputPath;
    std::vector<std::string> _inputFiles;
};

#endif