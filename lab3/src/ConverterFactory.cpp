#include "ConverterFactory.h"
#include "../converter/MuteConverter.h"
#include "../converter/MixConverter.h"
#include "../converter/EchoConverter.h"
#include "exceptions/ConfigException.h"
#include "creaters/Creater_echo.h"
#include "creaters/Creater_mix.h"
#include "creaters/Creater_mute.h"
#include <iostream>

ConverterFactory::ConverterFactory() {
    registerAll();
}

void ConverterFactory::registerAll() {
    _creators["mute"] = &Creater_mute::Create_converter;
    _creators["mix"] = &Creater_mix::Create_converter;
    _creators["echo"] = &Creater_echo::Create_converter;
}

std::unique_ptr<Converter> ConverterFactory::getConverter(const std::string& cmd, const std::vector<std::string>& args, const std::vector<std::string>& inputFiles)
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