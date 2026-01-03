#include "ConverterFactory.h"
#include "../converter/MuteConverter.h"
#include "../converter/MixConverter.h"
#include "../converter/EchoConverter.h"
#include "exceptions/ConfigException.h"
#include <iostream>

ConverterFactory::ConverterFactory() {
    registerAll();
}

void ConverterFactory::registerAll() {
    _creators["mute"] = [](const std::vector<std::string>& args, const std::vector<std::string>& inputs){
        if (args.size() < 2) {
            throw ConfigException("mute requires 2 arguments (start, end)");
        }
        return std::make_unique<MuteConverter>(std::stoul(args[0]), std::stoul(args[1]));
    };

    _creators["mix"] = [](const std::vector<std::string>& args, const std::vector<std::string>& inputs) {
        if (args.size() < 2) {
            throw ConfigException("mix requires 2 arguments ($index, start)");
        }
        if (args[0][0] != '$') {
            throw ConfigException("mix reference must start with $");
        }

        size_t fileIdx = std::stoul(args[0].substr(1)) - 1;
        if (fileIdx >= inputs.size()) {
            throw ConfigException("Input file index out of range");
        }

        auto reader = std::make_unique<WavReader>(inputs[fileIdx]);
        return std::make_unique<MixConverter>(std::move(reader), std::stoul(args[1]));
    };

    _creators["echo"] = [](const std::vector<std::string>& args, const std::vector<std::string>& inputs) {
        if (args.size() < 2) {
            throw ConfigException("echo requires 2 arguments (delay, decay)");
        }
        return std::make_unique<EchoConverter>(std::stoul(args[0]), std::stof(args[1]));
    };
}

std::unique_ptr<Converter> ConverterFactory::createConverter(const std::string& cmd, const std::vector<std::string>& args, const std::vector<std::string>& inputFiles)
{
    auto it = _creators.find(cmd);
    if (it == _creators.end()) {
        {
            throw ConfigException("Unknown converter: " + cmd);
        }
    }

    return it->second(args, inputFiles);
}

void ConverterFactory::printAllHelp() {
    std::cout << "Supported converters:\n"
              << " - mute <start> <end>\n"
              << " - mix $<index> <start>\n"
              << " - echo <delay> <decay>\n";
}