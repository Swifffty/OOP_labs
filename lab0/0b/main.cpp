#include <iostream>
#include <fstream>
#include "fill_map.h"
#include "Dictinory.h"
#include "Dictionary_vector.h"
#include "Csv_file.h"

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

    Dictionary_vector dict_in_vector(dict_f.Get_map(), dict_f.Get_counts());

    Csv_file file_for_write;

    file_for_write.fill_csv(dict_in_vector.Get_vector(), dict_in_vector.Get_count());
    return 0;
}


