#include <iostream>
#include <list>
#include <filesystem>
#include <string>
#include <fstream>
#include <cctype>
#include <vector>
#include <algorithm>


using std::string;
using std::list;

typedef struct {
    string word;
    unsigned int count;
} type_vector;

bool compare(const type_vector &element_a, const type_vector &element_b) {
    if (element_a.count > element_b.count) {
        return true;
    } else {
        return false;
    }
}

int find(std::vector<type_vector> words, string &word) {
    for (int i = 0; i < words.size(); i++) {
        if (words[i].word == word) {
            return i;
        }
    }
    return -1;
}

void add_vec(std::vector<type_vector> &words, string &word) {
    if (size(word) == 0) {
        return;
    }

    int res_index = find(words, word);

    if (res_index != -1) {
        words[res_index].count += 1;
    } else {
        words.push_back({word, 1});
    }
    word = "";
}

void fill_vec(std::vector<type_vector> &words, std::list<string> &text, string line, string &word, int &count_words) {
    text.push_back(line);
    unsigned int size_line = size(line);

    for (int i = 0; i < size_line; i++) {

        if (std::isalpha(line[i])) {
            word.push_back(tolower(line[i]));

        } else if (std::isdigit(line[i])) {
            word.push_back(line[i]);

        } else {
            if (i == size_line - 1 && line[i] == '-') {
                return;
            }
            add_vec(words, word);
            count_words++;
        }
    }
    if (!word.empty()) {
        add_vec(words, word);
        count_words++;
    }
}

void fill_csv(std::ofstream &csv, std::vector<type_vector> &words, int &count_words) {
    csv << "Words;Count;Frequency\n";
    for (const type_vector &element : words) {
        csv << element.word << ';' << element.count << ';' << element.count * 100 / count_words << "%" << "\n";
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

    list<std::string> text;

    std::vector<type_vector> words;
    string word;
    int count_words = 0;

    while (!f.eof()) {
        fill_vec(words, text, line, word, count_words);
        std::getline(f, line);
    }

    f.close();

    std::sort(words.begin(), words.end(), compare);

    std::ofstream csv("table_words.csv");
    if (!csv.is_open()) {
        std::cout << "no such csv file";
        return 0;
    }

    fill_csv(csv, words, count_words);
    csv.close();
    return 0;
}
