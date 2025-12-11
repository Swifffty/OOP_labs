#pragma once

#include <cstdint>
#include <iostream>

struct RiffChunk {
    char chunkId[4] = {'R', 'I', 'F', 'F'};
    uint32_t chunkSize = 0;
    char format[4] = {'W', 'A', 'V', 'E'};
};

struct FmtChunk {
    char chunkId[4] = {'f', 'm', 't', ' '};
    uint32_t chunkSize = 16;
    uint16_t audioFormat = 1;
    uint16_t numChannels = 1;
    uint32_t sampleRate = 44100;
    uint32_t byteRate = 88200;
    uint16_t blockAlign = 2;
    uint16_t bitsPerSample = 16;
};

struct DataChunk {
    char chunkId[4] = {'d', 'a', 't', 'a'};
    uint32_t chunkSize = 0;
};
