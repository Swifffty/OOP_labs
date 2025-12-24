#include <iostream>
#include <filesystem>
#include <string>
#include <cctype>
#include <map>
#include "Dictinory.h"

Dictionary::Dictionary() {
    count_words = 0;
    word = "";
}

std::map<string, unsigned int>& Dictionary::Get_map() {
    return words;
}

double &Dictionary::Get_counts() {
    return count_words;
}


void Dictionary::add_word_to_map() {
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

bool Dictionary::add_letter_to_word(char letter) {
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

