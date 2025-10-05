#include <iostream>
#include <filesystem>
#include <string>
#include <fstream>
#include <cctype>
#include <vector>
#include <algorithm>
#include <map>
#include <iomanip>
#include "Dictinory.h"

bool compare(const type_vector &element_a, const type_vector &element_b) {
    if (element_a.count > element_b.count) {
        return true;
    }
    return false;
}

Dictionary::Dictionary() {
    count_words = 0;
    word = "";
}


void Dictionary::add_map() {
    if (word.empty() == 1) {
        return;
    }
    if (words.find(word) == words.end()) {
        words[word] = 1;
    } else {
        words[word] += 1;
    }
    word = "";
    count_words++;
}

bool Dictionary::app_letter(char letter) {
    if (std::isalpha(letter) || std::isdigit(letter)) {
        word.push_back(tolower(letter));
        return false;
    }
    return true;
}

bool Dictionary::word_empty() {
    if (word.empty()) {
        return true;
    }
    return false;
}

void Dictionary::map_to_sort_vec() {
    type_vector tmp;
    for (const auto& [key, value] : words) {
        tmp.word = key;
        tmp.count = value;
        words_vec.push_back(tmp);
    }
    std::sort(words_vec.begin(), words_vec.end(), compare);
    words.clear();
}

void Dictionary::fill_csv(std::ofstream &csv) {
    csv << "Words;Count;Frequency\n";
    for (const type_vector &element : words_vec) {
        double freq = element.count * 100 / count_words;
        csv << element.word << ';' << element.count << ';' << std::fixed << std::setprecision(2) << freq << "%" << "\n";
    }
}

