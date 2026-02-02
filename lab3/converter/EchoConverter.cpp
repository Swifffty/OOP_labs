#include "EchoConverter.h"
#include <algorithm>

EchoConverter::EchoConverter(uint32_t delaySeconds, float decay) {
    _delay = delaySeconds;
    _decay = decay;
}

void EchoConverter::process(std::vector<int16_t>& buffer, size_t currentSecond) {

    if (currentSecond < _delay) {
        _history.push_back(buffer);
        return;
    }

    std::vector<int16_t>& oldSamples = _history.front();

    for (size_t i = 0; i < buffer.size(); ++i) {
        int32_t mixed = buffer[i] + static_cast<int32_t>(oldSamples[i] * _decay);

        buffer[i] = static_cast<int16_t>(std::clamp(mixed, -32768, 32767));
    }

    _history.erase(_history.begin());
    _history.push_back(buffer);
}

std::string EchoConverter::getHelp() const {
    return "echo <delay> <decay> : Adds an echo effect with specified delay (seconds) and decay factor (0.0-1.0).";
}
