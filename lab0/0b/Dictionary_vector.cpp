#include <algorithm>
#include "Dictionary_vector.h"
#include "Dictinory.h"

vector<type_vector> &Dictionary_vector::Get_vector() {
    return words_in_vector;
}

double &Dictionary_vector::Get_count() {
    return count_words;
}


bool compare(const type_vector &element_a, const type_vector &element_b) {
    if (element_a.count > element_b.count) {
        return true;
    }
    return false;
}

Dictionary_vector::Dictionary_vector(std::map<string, unsigned int> &words, double &count_words_from_map) {
    count_words = count_words_from_map;

    type_vector tmp;
    for (const auto& [key, value] : words) {
        tmp.word = key;
        tmp.count = value;
        words_in_vector.push_back(tmp);
    }
    std::sort(words_in_vector.begin(), words_in_vector.end(), compare);
}

