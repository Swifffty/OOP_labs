#ifndef INC_0B_FILL_CSV_H
#define INC_0B_FILL_CSV_H
#include <fstream>
#include <vector>
#include <string>
#include "Dictionary_vector.h"

using std::string;
using std::vector;

class Csv_file {
private:
    std::ofstream csv;
public:
    Csv_file();
    ~Csv_file();
    void fill_csv(vector<type_vector> &words_from_vector, double &count_words);
};

#endif //INC_0B_FILL_CSV_H