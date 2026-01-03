#ifndef MIX_CONVERTER_H
#define MIX_CONVERTER_H

#include "Converter.h"
#include "../src/IO/WavReader.h"
#include <memory>

class MixConverter : public Converter {
public:

    MixConverter(std::unique_ptr<WavReader> additionalReader, size_t startSecond);
    void process(std::vector<int16_t>& buffer, size_t currentSecond) override;
    std::string getHelp() const override;

private:
    std::unique_ptr<WavReader> _additionalReader;
    size_t _startSecond;
    std::vector<int16_t> _mixBuffer;
};

#endif