#include <gtest/gtest.h>
#include <string>
#include <vector>
#include "Dictinory.h"
#include "fill_map.h"

TEST(Dictionarytest, addmap) {
    Dictionary dict_test;

    dict_test.word = "";
    dict_test.add_map();
    EXPECT_EQ(dict_test.count_words, 0);

    dict_test.word = "hello";
    dict_test.add_map();
    EXPECT_EQ(dict_test.count_words, 1);
    EXPECT_EQ(dict_test.words["hello"], 1);

}

TEST(Dictionarytest, appletter) {
    Dictionary dict_test;

    EXPECT_EQ(dict_test.app_letter('a'), false);
    EXPECT_EQ(dict_test.app_letter('A'), false);
    EXPECT_EQ(dict_test.word[1], 'a');
    EXPECT_EQ(dict_test.app_letter('1'), false);
    EXPECT_EQ(dict_test.app_letter('?'), true);
}

TEST(Dictionarytest, wordempty) {
    Dictionary dict_test;

    dict_test.word = "";
    EXPECT_EQ(dict_test.word_empty(), true);

    dict_test.word = "a";
    EXPECT_EQ(dict_test.word_empty(), false);
}

TEST(Dictionarytest, maptosortvec) {
    Dictionary dict_test;

    dict_test.map_to_sort_vec();
    EXPECT_EQ(dict_test.words_vec.size(), 0);

    dict_test.words["hello"] = 3;
    dict_test.words["world"] = 6;
    dict_test.words["ai"] = 1;

    dict_test.map_to_sort_vec();

    EXPECT_EQ(dict_test.words_vec.size(), 3);
    EXPECT_EQ(dict_test.words_vec[0].word, "world");
    EXPECT_EQ(dict_test.words_vec[2].word, "ai");
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
