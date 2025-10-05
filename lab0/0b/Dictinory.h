#ifndef INC_0B_DICTINORY_H
#define INC_0B_DICTINORY_H

#include <string>
#include <gtest/gtest.h>

using std::string;


typedef struct {
    string word;
    unsigned int count;
} type_vector;


class Dictionary {
private:
    std::map<string, unsigned int> words;
    string word;
    std::vector<type_vector> words_vec;
    double count_words;
    FRIEND_TEST(Dictionarytest, addmap);
    FRIEND_TEST(Dictionarytest, appletter);
    FRIEND_TEST(Dictionarytest, wordempty);
    FRIEND_TEST(Dictionarytest, maptosortvec);
    FRIEND_TEST(functest, fillmaptest);

public:

    Dictionary();
    void add_map();
    bool app_letter(char letter);
    bool word_empty();
    void map_to_sort_vec();
    void fill_csv(std::ofstream &csv);
};

#endif //INC_0B_DICTINORY_H