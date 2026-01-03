#ifndef SOUND_PROCESSOR_H
#define SOUND_PROCESSOR_H

#include <string>
#include <vector>
#include "ConverterFactory.h"

class SoundProcessor {
public:
    SoundProcessor(const std::string& configPath, const std::string& outputPath, const std::vector<std::string>& inputPaths);
    void run(ConverterFactory &factory);

private:
    std::string _configPath;
    std::string _outputPath;
    std::vector<std::string> _inputPaths;
};

#endif