#include "SoundProcessor.h"
#include "IO/WavReader.h"
#include "IO/WavWriter.h"
#include "Parser/ConfigParser.h"
#include <iostream>

SoundProcessor::SoundProcessor(const std::string& configPath, const std::string& outputPath, const std::vector<std::string>& inputPaths) {
    _configPath = configPath;
    _outputPath = outputPath;
    _inputPaths = inputPaths;
}

void SoundProcessor::run(ConverterFactory &factory) {
    ConfigParser config(_configPath);

    if (_inputPaths.empty()) {
        throw std::runtime_error("No input files provided");
    }

    auto instructions = config.parse();

    std::vector<std::unique_ptr<Converter>> chain;
    for (const auto& instr : instructions) {
        auto conv = factory.createConverter(instr.command, instr.args, _inputPaths);
        chain.push_back(std::move(conv));
    }

    WavReader mainReader(_inputPaths[0]);
    WavWriter writer(_outputPath);

    std::vector<int16_t> buffer;
    size_t currentSecond = 0;

    while (mainReader.readSecond(buffer)) {
        for (auto& converter : chain) {
            converter->process(buffer, currentSecond);
        }
        writer.writeSecond(buffer);
        currentSecond++;
    }

    std::cout << "Успешно!" << "\n";
}