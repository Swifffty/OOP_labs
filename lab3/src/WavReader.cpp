#include "WavReader.h"
#include <cstring>

WavReader::WavReader(const std::string &filename) {
    file.open(filename, std::ios::binary);
    if (!file.is_open()) {
//        throw FileNotFoundException("Не удалось открыть файл: " + filename);
    }
    validateFormat();
}

WavReader::~WavReader() {
    if (file.is_open()) {
        file.close();
    }
}

void WavReader::validateFormat() {
    RiffChunk riff;
    FmtChunk fmt;
    DataChunk data;

    file.read(reinterpret_cast<char*>(&riff), sizeof(RiffChunk));
    if (file.gcount() != sizeof(RiffChunk) || strncmp(riff.chunkId, "RIFF", 4) != 0) {
      // throw WavFormatException("Файл не является RIFF/WAV.");
    }
    file.read(reinterpret_cast<char*>(&fmt), sizeof(FmtChunk));
    if (file.gcount() != sizeof(FmtChunk) || strncmp(fmt.chunkId, "fmt ", 4) != 0) {
        //throw WavFormatException("Отсутствует 'fmt ' чанк.");
    }

    if (fmt.audioFormat != 1 || fmt.numChannels != 1 || fmt.bitsPerSample != 16 || fmt.sampleRate != 44100) {
        //throw WavFormatException("Формат не поддерживается. Требуется: PCM, 16 бит, Моно, 44100 Гц.");
    }

    file.read(reinterpret_cast<char*>(&data), sizeof(DataChunk));
    if (file.gcount() != sizeof(DataChunk) || strncmp(data.chunkId, "data", 4) != 0) {
        // throw WavFormatException("Отсутствует 'data' чанк.");
    }
    data_chunk = data;

}

bool WavReader::readSamples(int16_t &sample) {
    if (!file.is_open()) {
        return false;
    }

    file.read(reinterpret_cast<char*>(&sample), sizeof(int16_t));

    if (file.gcount() == sizeof(int16_t)) {
        return true;
    }

    return false;
}
