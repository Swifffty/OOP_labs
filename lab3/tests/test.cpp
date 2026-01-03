#include <gtest/gtest.h>
#include <vector>
#include <cstdint>
#include <cstring>
#include <algorithm>
#include <memory>

#include "../src/IO/WavHeader.h"
#include "../src/Parser/CommandLineParser.h"
#include "ConverterFactory.h"
#include "converter/MuteConverter.h"

TEST(WavFileTest, HeaderSizeIsStandard) {
    EXPECT_EQ(sizeof(WavHeader), 44);
}

TEST(WavFileTest, MemcmpValidation) {
    WavHeader header;
    std::memcpy(header.chunkId, "RIFF", 4);
    EXPECT_EQ(std::memcmp(header.chunkId, "RIFF", 4), 0);
}

TEST(ParserTest, ValidArguments) {
    const char* argv[] = {"sound_proc", "-c", "config.txt", "out.wav", "in1.wav", "in2.wav"};
    CommandLineParser parser(6, const_cast<char**>(argv));

    EXPECT_EQ(parser.getConfigPath(), "config.txt");
    EXPECT_EQ(parser.getOutputPath(), "out.wav");
    EXPECT_EQ(parser.getInputFiles().size(), 2);
}

TEST(FactoryTest, CreateKnownConverter) {
    ConverterFactory factory;
    std::vector<std::string> args = {"0", "10"};
    std::vector<std::string> inputs = {"in1.wav"};

    auto conv = factory.createConverter("mute", args, inputs);
    ASSERT_NE(conv, nullptr);
}

TEST(FactoryTest, ThrowsOnUnknownConverter) {
    ConverterFactory factory;
    auto conv = factory.createConverter("unknown_effect", {}, {});
    EXPECT_EQ(conv, nullptr);
}

TEST(MuteConverterTest, ProcessSilence) {
    std::vector<int16_t> buffer = {1000, -2000, 3000, -4000};
    MuteConverter mute(0, 1);
    mute.process(buffer, 0);
    for (int16_t sample : buffer) {
        EXPECT_EQ(sample, 0);
    }
}

TEST(AudioMathTest, ClampPreventsOverflow) {
    int32_t loud = 50000;
    int32_t quiet = -60000;
    int16_t clampedHigh = static_cast<int16_t>(std::clamp(loud, -32768, 32767));
    int16_t clampedLow = static_cast<int16_t>(std::clamp(quiet, -32768, 32767));
    EXPECT_EQ(clampedHigh, 32767);
    EXPECT_EQ(clampedLow, -32768);
}

TEST(BufferTest, ResizeAfterRead) {
    std::vector<int16_t> buffer(44100, 0);
    size_t bytesRead = 2000;
    buffer.resize(bytesRead / sizeof(int16_t));
    EXPECT_EQ(buffer.size(), 1000);
}

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
