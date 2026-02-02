#include "WavWriter.h"
#include "../exceptions/FileException.h"
#include <cstring>

WavWriter::WavWriter(const std::string& filename) {
    _file.open(filename, std::ios::binary);
    if (!_file.is_open()) {
        throw FileException("Could not open output file for writing: " + filename);
    }
    writeEmptyHeader();
}

void WavWriter::writeEmptyHeader() {
    WavHeader emptyHeader;
    std::memcpy(emptyHeader.chunkId, "RIFF", 4);
    std::memcpy(emptyHeader.format, "WAVE", 4);
    std::memcpy(emptyHeader.subchunk1Id, "fmt ", 4);
    emptyHeader.subchunk1Size = 16;
    emptyHeader.audioFormat = 1;     // PCM
    emptyHeader.numChannels = 1;      // Mono
    emptyHeader.sampleRate = 44100;
    emptyHeader.bitsPerSample = 16;
    emptyHeader.byteRate = 44100 * 2;
    emptyHeader.blockAlign = 2;
    std::memcpy(emptyHeader.subchunk2Id, "data", 4);
    emptyHeader.chunkSize = 0;
    emptyHeader.subchunk2Size = 0;

    _file.write(reinterpret_cast<const char*>(&emptyHeader), sizeof(WavHeader));
}

void WavWriter::writeSecond(const std::vector<int16_t>& buffer) {
    if (buffer.empty()) {
        return;
    }
    
    _file.write(reinterpret_cast<const char*>(buffer.data()), buffer.size() * sizeof(int16_t));
    _dataSize += buffer.size() * sizeof(int16_t);
}

void WavWriter::finalize() {
    uint32_t chunkSize = 36 + _dataSize;

    _file.seekp(4, std::ios::beg);
    _file.write(reinterpret_cast<const char*>(&chunkSize), 4);

    _file.seekp(40, std::ios::beg);
    _file.write(reinterpret_cast<const char*>(&_dataSize), 4);
}

WavWriter::~WavWriter() {
    if (_file.is_open()) {
        finalize();
        _file.close();
    }
}