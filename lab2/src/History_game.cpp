#include "History_game.hpp"

History_game::History_game() {
    std::vector<char> filling;
    for (int i = 0; i < 3; i++) {
        choices_strategy.push_back(filling);
    }

    std::vector<int> fill;
    for (int i = 0; i < 3; i++) {
        points_strategy.push_back(fill);
    }
}

void History_game::add_choices_strategy(std::vector<char> choice) {
    for (int i = 0; i < 3; i++) {
        choices_strategy[i].push_back(choice[i]);
    }
}

void History_game::clear_history() {
    for (int i = 0; i < choices_strategy.size(); i++) {
        choices_strategy[i].clear();
        points_strategy[i].clear();
    }
}


char History_game::get_history_strategy(int& number_strategy, int& step) {

    if (step == 0) {
        return 'N';
    }
    return choices_strategy[number_strategy].back();
}

void History_game::add_points_strategy(std::vector<int>& points, int& step) {
    if (step == 0) {
        for (int i = 0; i < points_strategy.size(); i++) {
            points_strategy[i].push_back(points[i]);
        }
        return;
    }
    for (int i = 0; i < points_strategy.size(); i++) {
        points[i] += points_strategy[i][step - 1];
        points_strategy[i].push_back(points[i]);
    }
}

std::vector<int> History_game::get_history_points(int number_strategy) {
    return points_strategy[number_strategy];
}




