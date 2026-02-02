#include <gtest/gtest.h>
#include <vector>
#include <cstdint>
#include <algorithm>
#include <memory>
#include <string>
#include <fstream>
#include <cstring>

#include "../src/IO/WavReader.h"
#include "../src/Parser/CommandLineParser.h"
#include "ConverterFactory.h"
#include "converter/MuteConverter.h"
#include "../src/exceptions/SoundProcessorException.h"
#include "../src/IO/WavWriter.h"

void CreateDummyWav(const std::string& filename) {
    std::ofstream file(filename, std::ios::binary);
    WavHeader header;

    std::memcpy(header.chunkId, "RIFF", 4);
    std::memcpy(header.format, "WAVE", 4);
    std::memcpy(header.subchunk1Id, "fmt ", 4);
    std::memcpy(header.subchunk2Id, "data", 4);

    header.audioFormat = 1;
    header.numChannels = 1;
    header.sampleRate = 44100;
    header.bitsPerSample = 16;
    header.byteRate = 44100 * 2;
    header.blockAlign = 2;

    file.write(reinterpret_cast<const char*>(&header), sizeof(WavHeader));
    file.close();
}

TEST(CoreTest, WavHeaderCheck) {
    EXPECT_EQ(sizeof(WavHeader), 44);
}

TEST(CoreTest, ExceptionMessage) {
    std::string msg = "Test Error";
    try {
        throw SoundProcessorException(msg);
    } catch (const std::exception& e) {
        EXPECT_STREQ(e.what(), msg.c_str());
    }
}

TEST(ParserTest, FullArgsStream) {
    const char* argv[] = {"proc", "-c", "cfg.txt", "out.wav", "in1.wav", "in2.wav"};
    CommandLineParser parser(6, const_cast<char**>(argv));
    EXPECT_EQ(parser.getConfigPath(), "cfg.txt");
    EXPECT_EQ(parser.getOutputPath(), "out.wav");
    EXPECT_EQ(parser.getInputFiles().size(), 2);
}

TEST(FactoryRegistrationTest, CreateAllConverters) {
    CreateDummyWav("in1.wav");
    CreateDummyWav("in2.wav");

    ConverterFactory factory;
    std::vector<std::string> inputs = {"in1.wav", "in2.wav"};

    EXPECT_NE(factory.getConverter("mute", {"0", "1"}, inputs), nullptr);
    EXPECT_NE(factory.getConverter("echo", {"1", "0.5"}, inputs), nullptr);
    EXPECT_NE(factory.getConverter("mix", {"$2", "10"}, inputs), nullptr);

    std::remove("in1.wav");
    std::remove("in2.wav");
}

TEST(MuteLogicTest, IntervalProcessing) {
    std::vector<int16_t> buffer = {100, 200, 300};
    MuteConverter mute(1, 3);

    mute.process(buffer, 2);
    EXPECT_EQ(buffer[0], 0);
    EXPECT_EQ(buffer[1], 0);
}

TEST(MixLogicTest, ClampingManualCheck) {
    int32_t s1 = 30000;
    int32_t s2 = 10000;
    int16_t res = static_cast<int16_t>(std::clamp(s1 + s2, -32768, 32767));
    EXPECT_EQ(res, 32767);
}

TEST(IOTest, ReaderValidation) {
    std::string name_file = "missing.wav";
    EXPECT_ANY_THROW({ WavReader reader(name_file); });
    std::ofstream f("corrupt.wav");
    f << "RIFF";
    f.close();
    std::string file_name = "corrupt.wav";
    EXPECT_ANY_THROW({ WavReader reader(file_name); });
    std::remove("corrupt.wav");
}

TEST(IOTest, WriterLogic) {
    std::string file_name = "test_out.wav";
    EXPECT_NO_THROW({ WavWriter writer(file_name); });
    std::remove("test_out.wav");
}

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
