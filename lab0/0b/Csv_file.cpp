#include "Csv_file.h"
#include <iostream>
#include <iomanip>
#define PERCENT 100

Csv_file::Csv_file() {
    csv.open("table_words.csv");
}

Csv_file::~Csv_file() {
    csv.close();
}


void Csv_file::fill_csv(vector<type_vector> &words_from_vector, double &count_words) {
    csv << "Words;Count;Frequency\n";
    for (const type_vector &element : words_from_vector) {
        double freq = element.count * PERCENT / count_words;
        csv << element.word << ';' << element.count << ';' << std::fixed << std::setprecision(2) << freq << "%" << "\n";
    }
}
