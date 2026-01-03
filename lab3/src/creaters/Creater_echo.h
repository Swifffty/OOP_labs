#ifndef LAB3_CREATER_ECHO_H
#define LAB3_CREATER_ECHO_H

#include "../converter/EchoConverter.h"
#include "exceptions/ConfigException.h"

class Creater_echo {
public:
    static std::unique_ptr<Converter> Create_converter(const std::vector<std::string>& args, const std::vector<std::string>& inputs) {
        if (args.size() < 2) {
            throw ConfigException("mute requires 2 arguments (start, end)");
        }
        return std::make_unique<EchoConverter>(std::stoul(args[0]), std::stof(args[1]));
    }
};

#endif //LAB3_CREATER_ECHO_H