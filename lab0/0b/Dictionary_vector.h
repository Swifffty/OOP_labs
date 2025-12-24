#ifndef INC_0B_DICTIONARY_VECTOR_H
#define INC_0B_DICTIONARY_VECTOR_H
#include <string>
#include <vector>
#include <map>
#include <gtest/gtest_prod.h>

using std::string;
using std::vector;

typedef struct {
    string word;
    unsigned int count;
} type_vector;

class Dictionary_vector {
private:
    vector<type_vector> words_in_vector;
    double count_words;
    FRIEND_TEST(Dictionarytest, maptosortvec);
public:
    vector<type_vector> &Get_vector();
    double &Get_count();
    Dictionary_vector(std::map<string, unsigned int> &words, double &count_words_from_map);

};

#endif //INC_0B_DICTIONARY_VECTOR_H