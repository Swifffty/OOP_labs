#pragma once

#include "WavStruct.h"
//#include "CustomExceptions.h"
#include <fstream>
#include <string>

class WavWriter {
private:
    std::ofstream file;
    size_t data_size = 0;
public:
    WavWriter(const std::string& filename);
    ~WavWriter();
    void writeSample(int16_t sample);
    void header();
};