#ifndef CONVERTER_H
#define CONVERTER_H

#include <vector>
#include <cstdint>
#include <string>

class Converter {
public:
    virtual ~Converter() = default;

    virtual void process(std::vector<int16_t>& buffer, size_t currentSecond) = 0;

    virtual std::string getHelp() const = 0;
};

#endif