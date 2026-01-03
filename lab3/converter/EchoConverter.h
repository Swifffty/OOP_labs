#ifndef ECHO_CONVERTER_H
#define ECHO_CONVERTER_H

#include "Converter.h"
#include <vector>
#include <cstdint>

class EchoConverter : public Converter {
public:
    EchoConverter(uint32_t delaySeconds, float decay);

    void process(std::vector<int16_t>& buffer, size_t currentSecond) override;
    std::string getHelp() const override;

private:
    uint32_t _delay;
    float _decay;
    std::vector<std::vector<int16_t>> _history;
};

#endif