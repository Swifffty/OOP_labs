#include "WavReader.h"
#include "../exceptions/FileException.h"
#include <cstring>

WavReader::WavReader(const std::string& filename) {
    _file.open(filename, std::ios::binary);
    if (!_file.is_open()) {
        throw FileException("Could not open input file: " + filename);
    }

    _file.read(reinterpret_cast<char*>(&_header), sizeof(WavHeader));
    if (_file.gcount() < sizeof(WavHeader)) {
        throw FileException("File too small to be a WAV file: " + filename);
    }

    validateHeader();
}

void WavReader::validateHeader() {
    if (std::memcmp(_header.chunkId, "RIFF", 4) != 0 ||
        std::memcmp(_header.format, "WAVE", 4) != 0) {
        throw FileException("Unsupported file format (not RIFF/WAVE)");
        }

    if (_header.audioFormat != 1) {
        throw FileException("Only PCM format is supported");
    }

    if (_header.numChannels != 1) {
        throw FileException("Only mono audio is supported");
    }

    if (_header.sampleRate != 44100) {
        throw FileException("Only 44100 Hz sample rate is supported");
    }

    if (_header.bitsPerSample != 16) {
        throw FileException("Only 16-bit audio is supported");
    }
}

bool WavReader::readSecond(std::vector<int16_t>& buffer) {
    buffer.assign(44100, 0);
    _file.read(reinterpret_cast<char*>(buffer.data()), 44100 * sizeof(int16_t));

    size_t bytesRead = _file.gcount();
    if (bytesRead == 0) {
        return false;
    }

    buffer.resize(bytesRead / sizeof(int16_t));
    return true;
}

void WavReader::reset() {
    _file.clear();
    _file.seekg(sizeof(WavHeader), std::ios::beg);
}

WavReader::~WavReader() {
    if (_file.is_open()) _file.close();
}