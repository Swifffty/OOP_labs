#ifndef MUTE_CONVERTER_H
#define MUTE_CONVERTER_H

#include "Converter.h"

class MuteConverter : public Converter {
public:
    MuteConverter(size_t start, size_t end);
    void process(std::vector<int16_t>& buffer, size_t currentSecond) override;
    std::string getHelp() const override;

private:
    size_t _start;
    size_t _end;
};

#endif