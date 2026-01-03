#ifndef LAB3_CREATER_MIX_H
#define LAB3_CREATER_MIX_H

#include "IO/WavReader.h"
#include "../converter/MixConverter.h"
#include "exceptions/ConfigException.h"

class Creater_mix {
public:
   static std::unique_ptr<Converter> Create_converter(const std::vector<std::string>& args, const std::vector<std::string>& inputs) {
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
        auto reader =  std::make_unique<WavReader>(inputs[fileIdx]);
        return std::make_unique<MixConverter>(std::move(reader), std::stoul(args[1]));
    }
};

#endif //LAB3_CREATER_MIX_H