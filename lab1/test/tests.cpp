#include "gtest/gtest.h"
#include "BitArray.h" // Путь к вашему заголовочному файлу
#include <string>

using namespace bitarray;

// --- Фикстура для BitArray ---
class BitArraySimpleTest : public ::testing::Test {
protected:
    // Константа для удобства
    const int TEST_SIZE = 10;
};

// =================================================================================================
// 1. Конструкторы и Базовые Методы (size, empty, to_string)
// =================================================================================================

TEST_F(BitArraySimpleTest, ConstructorDefaultAndSize) {
    BitArray ba_default;
    EXPECT_EQ(ba_default.size(), 0);
    EXPECT_TRUE(ba_default.empty());

    BitArray ba_init(TEST_SIZE, 0);
    EXPECT_EQ(ba_init.size(), TEST_SIZE);
    EXPECT_EQ(ba_init.to_string(), "0000000000");
}

TEST_F(BitArraySimpleTest, ConstructorWithInitialValue) {
    // 5 (101) для 8 бит. В вашей реализации: "00000101"
    BitArray ba(8, 5);
    EXPECT_EQ(ba.to_string(), "00000101");
}

TEST_F(BitArraySimpleTest, CopyAndAssignment) {
    BitArray original(5, 0b10101); // 10101
    BitArray copy = original;
    BitArray assigned;
    assigned = original;

    EXPECT_EQ(copy.to_string(), "10101");
    EXPECT_EQ(assigned.to_string(), "10101");
}

// =================================================================================================
// 2. Доступ, Установка и Сброс ([], set, reset)
// =================================================================================================

TEST_F(BitArraySimpleTest, AccessAndSetReset) {
    BitArray ba(4, 0b1100); // 0011

    // Доступ
    EXPECT_TRUE(ba[0]); // Индекс 0
    EXPECT_FALSE(ba[3]); // Индекс 3

    // Установка
    ba.set(3, true); // 1011
    EXPECT_TRUE(ba[3]);
    EXPECT_EQ(ba.to_string(), "1011");

    // Сброс
    ba.reset(0); // 1010
    EXPECT_FALSE(ba[0]);
    EXPECT_EQ(ba.to_string(), "1010");

    // set() и reset() - все биты
    ba.set();
    EXPECT_TRUE(ba.any());
    ba.reset();
    EXPECT_TRUE(ba.none());
}

TEST_F(BitArraySimpleTest, IndexOutOfBoundsThrows) {
    BitArray ba(5, 0);
    // Проверка выхода за границы
    EXPECT_THROW(ba[5], std::string);
    EXPECT_THROW(ba.set(-1, true), std::string);
}

// =================================================================================================
// 3. Логические Операторы
// =================================================================================================

TEST_F(BitArraySimpleTest, BitwiseOperators) {
    BitArray a(4, 0b1100); // 0011
    BitArray b(4, 0b1010); // 0101

    // &=
    BitArray a_and = a;
    a_and &= b; // 0001
    EXPECT_EQ(a_and.to_string(), "0001");

    // |
    BitArray or_res = a | b; // 0111
    EXPECT_EQ(or_res.to_string(), "0111");

    // ^
    BitArray xor_res = a ^ b; // 0110
    EXPECT_EQ(xor_res.to_string(), "0110");

    // ~
    BitArray not_res = ~a; // ~0011 = 1100
    EXPECT_EQ(not_res.to_string(), "1100");
}

// =================================================================================================
// 4. Сдвиги (Shift Operators)
// =================================================================================================

TEST_F(BitArraySimpleTest, ShiftOperators) {
    BitArray ba(8, 0b00011000); // 00011000

    // <<=
    BitArray left = ba;
    left <<= 2; // 01100000
    EXPECT_EQ(left.to_string(), "01100000");

    // >>
    BitArray right = ba >> 3; // 00000011. Проверка не-присваивающего оператора.
    EXPECT_EQ(right.to_string(), "00000011");

    // Сдвиг, который обнуляет (n > size)
    left <<= 8;
    EXPECT_EQ(left.to_string(), "00000000");
}

// =================================================================================================
// 5. Разное (resize, count, push_back, any/none, ==)
// =================================================================================================

TEST_F(BitArraySimpleTest, ResizeGrowShrink) {
    BitArray ba(4, 0b1010); // 1010

    // Увеличение с нулями
    ba.resize(6, false); // 001010
    EXPECT_EQ(ba.to_string(), "001010");

    // Уменьшение
    ba.resize(3); // 010
    EXPECT_EQ(ba.to_string(), "010");
}

TEST_F(BitArraySimpleTest, PushBackAndCount) {
    BitArray ba(2, 0b01); // 10
    ba.push_back(true);  // 101
    ba.push_back(false); // 0101

    EXPECT_EQ(ba.size(), 4);
    EXPECT_EQ(ba.to_string(), "0101");
    EXPECT_EQ(ba.count(), 2);
}

TEST_F(BitArraySimpleTest, AnyNoneAndEquality) {
    BitArray a(3, 0b001); // 100
    BitArray b(3, 0b000); // 000

    EXPECT_TRUE(a.any());
    EXPECT_FALSE(a.none());

    EXPECT_FALSE(b.any());
    EXPECT_TRUE(b.none());

    EXPECT_TRUE(a != b);
    EXPECT_FALSE(a == b);
}

// =================================================================================================

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
