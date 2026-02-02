#include "MixConverter.h"

MixConverter::MixConverter(std::unique_ptr<WavReader> additionalReader, size_t startSecond) {
    _additionalReader = std::move(additionalReader);
    _startSecond = startSecond;
}

void MixConverter::process(std::vector<int16_t>& buffer, size_t currentSecond) {
    if (currentSecond < _startSecond) {
        return;
    }

    if (_additionalReader->readSecond(_mixBuffer)) {
        size_t minSize = std::min(buffer.size(), _mixBuffer.size());
        for (size_t i = 0; i < minSize; ++i) {
            buffer[i] = (buffer[i] + _mixBuffer[i]) / 2;
        }
    }
}

std::string MixConverter::getHelp() const {
    return "mix $<n> <start> - Mixes current stream with stream from file #n starting from 'start' second.";
}