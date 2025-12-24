#include "Bully.h"
#include <fstream>

Bully::Bully(const std::string &config_dir) {

    std::string full_name = config_dir + "/Bully.txt";
    std::ifstream config(full_name);
    if (!config.is_open()) {
        throw std::invalid_argument("Неверная директория с конфигурационными файлами для стратегий");
    }
    config >> n;
}


char Bully::step(History_game& history, int& step, int& index_strategy) {
    if (step == 0 || step < n) {
        return 'D';
    }

    std::vector<int> points = history.get_history_points(index_strategy);

    if (points[step - 1] - points[step - 2] < 0) {
        return 'C';
    }

    char last_choice;
    int count = 0;
    for (int i = 0; i < 3; i++) {
        if (i == index_strategy) {
            continue;
        }
        last_choice = history.get_history_strategy(i, step);
        if (last_choice == 'D') {
            count++;
        }
    }

    if (count == 2) {
        return 'C';
    }
    return 'D';
}
