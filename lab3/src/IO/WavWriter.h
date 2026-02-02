#ifndef WAV_WRITER_H
#define WAV_WRITER_H

#include <string>
#include <fstream>
#include <vector>
#include <cstdint>
#include "WavHeader.h"

class WavWriter {
public:
    explicit WavWriter(const std::string& filename);
    ~WavWriter();

    void writeSecond(const std::vector<int16_t>& buffer);
    void finalize(); // Обновляет размеры в заголовке перед закрытием

private:
    std::ofstream _file;
    uint32_t _dataSize = 0;
    void writeEmptyHeader();
};

#endif