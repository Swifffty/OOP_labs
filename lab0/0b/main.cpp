#include <iostream>
#include <filesystem>
#include <string>
#include <fstream>
#include "fill_map.h"
#include "Dictinory.h"

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

    dict_f.fill_csv(csv);

    csv.close();
    return 0;
}
