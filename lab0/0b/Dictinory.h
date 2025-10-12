#ifndef INC_0B_DICTINORY_H
#define INC_0B_DICTINORY_H

#include <string>
#include <gtest/gtest.h>

using std::string;

class Dictionary {
private:
    std::map<string, unsigned int> words;
    string word;
    double count_words;

    FRIEND_TEST(Dictionarytest, addmap);
    FRIEND_TEST(Dictionarytest, appletter);
    FRIEND_TEST(Dictionarytest, wordempty);
    FRIEND_TEST(functest, fillmaptest);

public:

    Dictionary();

    std::map<string, unsigned int> &Get_map();
    double &Get_counts();
    void add_word_to_map();
    bool add_letter_to_word (char letter);
    bool word_empty();
};

#endif //INC_0B_DICTINORY_H