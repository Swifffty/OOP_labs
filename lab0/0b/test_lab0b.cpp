#include <gtest/gtest.h>
#include <string>
#include <vector>
#include "Dictinory.h"
#include "fill_map.h"
#include "Dictionary_vector.h"
#include "Csv_file.h"

TEST(Dictionarytest, addmap) {
    Dictionary dict_test;

    dict_test.word = "";
    dict_test.add_word_to_map();
    EXPECT_EQ(dict_test.count_words, 0);

    dict_test.word = "hello";
    dict_test.add_word_to_map();
    EXPECT_EQ(dict_test.count_words, 1);
    EXPECT_EQ(dict_test.words["hello"], 1);

}

TEST(Dictionarytest, appletter) {
    Dictionary dict_test;

    EXPECT_EQ(dict_test.add_letter_to_word('a'), false);
    EXPECT_EQ(dict_test.add_letter_to_word('A'), false);
    EXPECT_EQ(dict_test.word[1], 'a');
    EXPECT_EQ(dict_test.add_letter_to_word('1'), false);
    EXPECT_EQ(dict_test.add_letter_to_word('?'), true);
}

TEST(Dictionarytest, wordempty) {
    Dictionary dict_test;

    dict_test.word = "";
    EXPECT_EQ(dict_test.word_empty(), true);

    dict_test.word = "a";
    EXPECT_EQ(dict_test.word_empty(), false);
}

TEST(Dictionarytest, maptosortvec) {
    std::map<string, unsigned int> test_map;
    double count_test = 0;
    Dictionary_vector dict_test(test_map, count_test);

    EXPECT_EQ(dict_test.words_in_vector.size(), 0);
    EXPECT_EQ(dict_test.count_words, 0);

    test_map["hello"] = 3;
    test_map["ai"] = 1;
    test_map["world"] = 6;
    test_map["hi"] = 2;
    count_test = 12;

    Dictionary_vector dict_test2(test_map, count_test);

    EXPECT_EQ(dict_test2.words_in_vector.size(), 4);
    EXPECT_EQ(dict_test2.words_in_vector[0].word, "world");
    EXPECT_EQ(dict_test2.words_in_vector[3].word, "ai");
    EXPECT_EQ(dict_test2.words_in_vector[1].count, 3);
    EXPECT_EQ(dict_test2.count_words, 12);

}

TEST(functest, fillmaptest) {
    Dictionary dict_test;

    string line_test = "Hello, world! This is simple test";
    fill_map(dict_test, line_test);
    EXPECT_EQ(dict_test.count_words, 6);

    line_test = "pur-";
    fill_map(dict_test, line_test);
    line_test = "ple";
    fill_map(dict_test, line_test);
    EXPECT_EQ(dict_test.count_words, 7);
}
