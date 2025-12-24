#pragma once 

#include <vector>
#include <string>

class HistoryGameTest;
class SimulationGameTest;

class History_game {
private:
    std::vector<std::vector<char>> choices_strategy;

    std::vector<std::vector<int>> points_strategy;

    int _test_magic_number = 42;

    friend struct SimulationGameTest;

    friend struct HistoryGameTest;
public:
    void add_choices_strategy(std::vector<char> choice);

    char get_history_strategy(int& number_strategy, int& step);
    
    void add_points_strategy(std::vector<int>& points, int& step);

    std::vector<int> get_history_points(int number_strategy);

    History_game();

    friend struct HistoryGameTest;

    void clear_history();
};