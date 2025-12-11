#pragma once

#include "WavStruct.h"
//#include "CustomExceptions.h"
#include <fstream>
#include <string>

class WavReader {
private:
    std::ifstream file;
    DataChunk data_chunk;
public:
    WavReader(const std::string& filename);
    ~WavReader();
    size_t getNumSamples() const;
//    void seekToDataStart();
    void validateFormat();
    bool readSamples(int16_t& samples);
};

