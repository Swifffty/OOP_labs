#include "Dictinory.h"
#include "fill_map.h"


void fill_map(Dictionary &dict_f, string &line) {
    unsigned int size_line = size(line);

    for (int i = 0; i < size_line - 1; i++) {
        if (dict_f.add_letter_to_word(line[i])) {
            dict_f.add_word_to_map();
        }
    }

    if (line[size_line - 1] == '-') {
        return;
    }

    dict_f.add_letter_to_word(line[size_line - 1]);
    if (!dict_f.word_empty()) {
        dict_f.add_word_to_map();
    }
}
