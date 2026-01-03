#ifndef WAV_HEADER_H
#define WAV_HEADER_H

#include <cstdint>

#pragma pack(push, 1)
struct WavHeader {
    // RIFF Chunk
    char chunkId[4];       // RIFF
    uint32_t chunkSize;    // 36 + subChunk2Size
    char format[4];        // WAVE

    // fmt Subchunk
    char subchunk1Id[4];   // "fmt "
    uint32_t subchunk1Size;// 16 для PCM
    uint16_t audioFormat;  // 1 для PCM
    uint16_t numChannels;  // 1 для моно
    uint32_t sampleRate;   // 44100
    uint32_t byteRate;     // sampleRate * numChannels * bitsPerSample/8
    uint16_t blockAlign;   // numChannels * bitsPerSample/8
    uint16_t bitsPerSample;// 16

    // data Subchunk
    char subchunk2Id[4];   // data
    uint32_t subchunk2Size;// количество байт в аудиоданных
};
#pragma pack(pop)

#endif