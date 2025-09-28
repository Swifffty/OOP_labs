#include <iostream>
#include <filesystem>
#include <string>
#include <fstream>
#include <cctype>
#include <vector>
#include <algorithm>
#include <map>


using std::string;

typedef struct {
    string word;
    unsigned int count;
} type_vector;

bool compare(const type_vector &element_a, const type_vector &element_b) {
    if (element_a.count > element_b.count) {
        return true;
    }
    return false;
}

class Dictionary {
private:
    std::map<string, unsigned int> words;
    string word;
public:
    std::vector<type_vector> words_vec;
    unsigned int count_words;

    Dictionary() {
        count_words = 0;
        word = "";
    }
    void add_map() {
        if (word.size() == 0) {
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

    bool app_letter(char letter) {
        if (std::isalpha(letter) || std::isdigit(letter)) {
            word.push_back(tolower(letter));
            return false;
        }
        return true;
    }

    bool word_empty() {
        if (word.empty()) {
            return true;
        }
        return false;
    }

    void map_to_sort_vec() {
        type_vector tmp;
        for (const auto& [key, value] : words) {
            tmp.word = key;
            tmp.count = value;
            words_vec.push_back(tmp);
        }
        std::sort(words_vec.begin(), words_vec.end(), compare);
        words.clear();
    }

};

void fill_map(Dictionary &dict_f, string &line) {
    unsigned int size_line = size(line);

    for (int i = 0; i < size_line - 1; i++) {
        if (dict_f.app_letter(line[i])) {
          dict_f.add_map();
        }
    }
    if (line[size_line - 1] == '-') {
        return;
    }

    dict_f.app_letter(line[size_line - 1]);
    if (!dict_f.word_empty()) {
        dict_f.add_map();
    }
}

void fill_csv(std::ofstream &csv, Dictionary &dict_f) {
    csv << "Words;Count;Frequency\n";
    for (const type_vector &element : dict_f.words_vec) {
        csv << element.word << ';' << element.count << ';' << element.count * 100 / dict_f.count_words << "%" << "\n";
    }
}

int main() {
    string line;

    std::ifstream f("text.txt");

    if (!f.is_open()) {
        std::cout << "no such file";
        return 0;
    }

    std::getline(f, line);
    if (line.empty()) {
        std::cout << "empty file";
        return 0;
    }

    Dictionary dict_f;

    while (!f.eof()) {
        fill_map(dict_f, line);
        std::getline(f, line);
    }

    f.close();

    dict_f.map_to_sort_vec();

    std::ofstream csv("table_words.csv");
    if (!csv.is_open()) {
        std::cout << "no such csv file";
        return 0;
    }

    fill_csv(csv, dict_f);
    csv.close();
    return 0;
}
