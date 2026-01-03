#ifndef LAB3_CREATE_MUTE_H
#define LAB3_CREATE_MUTE_H

#include "../converter/MuteConverter.h"
#include "exceptions/ConfigException.h"
#include <vector>
#include <string>


class Creater_mute {
public:
    static std::unique_ptr<Converter> Create_converter(const std::vector<std::string>& args, const std::vector<std::string>& inputs) {
        if (args.size() < 2) {
            throw ConfigException("mute requires 2 arguments (start, end)");
        }
        return std::make_unique<MuteConverter>(std::stoul(args[0]), std::stoul(args[1]));
    }

};

#endif //LAB3_CREATE_MUTE_H