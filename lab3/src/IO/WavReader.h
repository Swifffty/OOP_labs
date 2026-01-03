#ifndef WAV_READER_H
#define WAV_READER_H

#include <string>
#include <fstream>
#include <vector>
#include <cstdint>
#include "WavHeader.h"

class WavReader {
public:
    explicit WavReader(const std::string& filename);
    ~WavReader();

    bool readSecond(std::vector<int16_t>& buffer);

    void reset();

private:
    std::ifstream _file;
    WavHeader _header;
    void validateHeader();
};

#endif