#include <gtest/gtest.h>
#include "BitArray.h"

class BitArrayTestConstruct : public ::testing::Test {
protected:
    void SetUp() override {
        empty = std::make_unique<BitArray>();
        with_long = std::make_unique<BitArray>(35, 154);
    }


    std::unique_ptr<BitArray> empty;
    std::unique_ptr<BitArray> with_long;

};

class BitArrayTestBaseOperation : public ::testing::Test {
protected:
    void SetUp() override {
        obj_1 = std::make_unique<BitArray>(8, 154);
        obj_2 = std::make_unique<BitArray>(13, 320);
        obj_3 = std::make_unique<BitArray>(32, 460);
    }

    std::unique_ptr<BitArray> obj_1;
    std::unique_ptr<BitArray> obj_2;
    std::unique_ptr<BitArray> obj_3;
};

class BitArrayTestBitOperations : public ::testing::Test {
protected:
    void SetUp() override {
        obj_1 = std::make_unique<BitArray>(8, 154);
        obj_2 = std::make_unique<BitArray>(8, 150);
    }
    std::unique_ptr<BitArray> obj_1;
    std::unique_ptr<BitArray> obj_2;
};

class BitArrayTestShift : public ::testing::Test {
protected:
    void SetUp() override {
        obj_1 = std::make_unique<BitArray>(37, 589);
        obj_1->set(31, true);
        string_with_shift_left = "1001001101000000000000000000000100000";
        string_with_shift_right= "0000100100110100000000000000000000010";
    }


    std::string string_with_shift_right;
    std::string string_with_shift_left;
    std::unique_ptr<BitArray> obj_1;
};

class BitArrayTestSetBits : public ::testing::Test {
protected:
    void SetUp() override {
        obj_1 = std::make_unique<BitArray>(38, 678);
        all_zero = "00000000000000000000000000000000000000";
    }
    std::unique_ptr<BitArray> obj_1;
    std::string all_zero;
};

TEST_F(BitArrayTestConstruct, Constuctor) {
    EXPECT_EQ(empty->size(), 0);
    EXPECT_EQ(with_long->operator[](0), 1);
    EXPECT_EQ(with_long->operator[](33), 0);
}

TEST_F(BitArrayTestBaseOperation, base_operation) {
    obj_1->swap(*obj_2);
    EXPECT_EQ(obj_1->to_string(), "1010000000000");

    obj_3->resize(34, false);
    EXPECT_FALSE(obj_3->operator[](33));

    obj_3->resize(36, true);
    EXPECT_TRUE(obj_3->operator[](35));

    obj_3->push_back(1);
    EXPECT_TRUE(obj_3->operator[](36));

    obj_3->push_back(0);
    EXPECT_FALSE(obj_3->operator[](37));

    obj_3->resize(4);
    EXPECT_EQ(obj_3->to_string(), "1110");
}

TEST_F(BitArrayTestBitOperations, bitoperations) {
    BitArray obj_3(operator&(*obj_1, *obj_2));
    EXPECT_EQ(obj_3.to_string(), "10010010");

    obj_3.operator=(operator|(*obj_1, *obj_2));
    EXPECT_EQ(obj_3.to_string(), "10011110");

    obj_3.operator=(operator^(*obj_1, *obj_2));
    EXPECT_EQ(obj_3.to_string(), "00001100");

    EXPECT_TRUE(operator!=(*obj_1, *obj_2));

}

TEST_F(BitArrayTestShift, testshift) {
    BitArray obj_2(obj_1->operator>>(4));
    EXPECT_EQ(obj_2.to_string(), string_with_shift_right);

    BitArray obj_3(obj_2.operator<<(4));
    EXPECT_EQ(obj_3.to_string(), string_with_shift_left);
}

TEST_F(BitArrayTestSetBits, testsetbits) {
    obj_1->reset(0);
    EXPECT_FALSE(obj_1->operator[](1));

    obj_1->set();
    EXPECT_EQ(obj_1->count(), 38);

    EXPECT_TRUE(obj_1->any());

    obj_1->reset();

    EXPECT_EQ(obj_1->to_string(), all_zero);

    EXPECT_TRUE(obj_1->none());

    BitArray obj_2(obj_1->operator~());

    EXPECT_EQ(obj_2.count(), 38);

    EXPECT_EQ(obj_1->size(), 38);

    EXPECT_FALSE(obj_1->empty());




}
