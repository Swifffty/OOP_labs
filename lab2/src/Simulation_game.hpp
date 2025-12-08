#pragma once

#include "History_game.hpp"
#include "strategy/Prisoner_strategy.hpp"
#include "vector"
#include <string>
#include <map>
#include <memory>

struct SimulationGameTest;

class Simulation_game {
private:
    std::vector<std::string> strategy;
    History_game history;
    int current_step = 0;
    int steps_limit = 10;
    std::string mode = "non";
    std::string dirname = "non";
    std::string matrix = "non";
    std::map<std::string, int> rules;

    std::vector<int> do_step(std::vector<std::unique_ptr<Prisoner_strategy>>& strategies);

    void game_config();

   virtual std::vector<int> classic_mode(std::vector<int>& numbers_strategy);

    void tournament();

    void parser(std::string& arg, int& count_strategy);

    int string_to_vector(std::string& line, std::vector<std::string>& line_vector);

    void print_results(std::vector<int>& results, std::vector<int>& number_strategy);

    int max_score(std::vector<int>& results, int& max_result);

    void take_name_strategy(std::string& name_strategy, int& number_max, int& i, int& j, int& k);

    std::vector<int> scoring(std::vector<char>& choices);


    friend struct SimulationGameTest;

public:
    Simulation_game(char* argv[], int argc);
    void start_game();
};
