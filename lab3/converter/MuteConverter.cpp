#include "MuteConverter.h"

MuteConverter::MuteConverter(size_t start, size_t end) {
    _start =start;
    _end = end;
}

void MuteConverter::process(std::vector<int16_t>& buffer, size_t currentSecond) {

    if (currentSecond >= _start && currentSecond < _end) {
        std::fill(buffer.begin(), buffer.end(), 0);
    }
}

std::string MuteConverter::getHelp() const {
    return "mute <start> <end> - Mutes the sound from 'start' to 'end' seconds.";
}