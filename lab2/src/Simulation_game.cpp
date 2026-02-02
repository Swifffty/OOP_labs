#include "Simulation_game.hpp"
#include "Factory_objects.hpp"
#include <algorithm>
#include <iostream>
#include <fstream>
#include <sstream>


void Simulation_game::game_config() {
    if (matrix == "non") {
        rules["CDD"] = 0;
        rules["DDD"] = 1;
        rules["CCD"] = 3;
        rules["DCD"] = 5;
        rules["CCC"] = 7;
        rules["DCC"] = 9;
    }
    else {
        std::ifstream matrix_file(matrix);
        if (!matrix_file.is_open()) {
            throw std::invalid_argument("Файл с матрицей игры не был найден");
        }
        std::string line;
        while (std::getline(matrix_file, line)) {
            std::stringstream ss(line);
            std::string combination;

            ss >> combination;
            ss >> line;

            int score = std::stoi(line);
            rules[combination] = score;
        }
    }
}

void Simulation_game::parser(std::string& arg, int &count_strategy) {
    if (arg[0] != '-') {
        strategy.push_back(arg);
        count_strategy++;
    } else {
        int pos_eq = arg.find('=');

        if (arg.find("mode") != std::string::npos) {
            mode = arg.substr(pos_eq + 1, arg.size() - pos_eq);
            return;
        }
        if (arg.find("steps") != std::string::npos) {
            steps_limit = std::stoi(arg.substr(pos_eq + 1, arg.size() - pos_eq));
            return;
        }
        if (arg.find("configs") != std::string::npos) {
            dirname = arg.substr(pos_eq + 1, arg.size() - pos_eq);
            return;
        }
        if (arg.find("matrix") != std::string::npos) {
            matrix = arg.substr(pos_eq + 1, arg.size() - pos_eq);
            return;
        }
        throw std::invalid_argument("Неизвестный параметр");
    }
}

Simulation_game::Simulation_game(char* argv[], int argc) {
    int count_strategy = 0;
    std::string argument;
    for (int i = 1; i < argc; i++) {
        argument = argv[i];
        parser(argument, count_strategy);
    }

    if (count_strategy < 3) {
        throw std::invalid_argument("Недостаточно стратегий для игры");
    }

    if (mode == "non") {
        if (count_strategy > 3) {
            mode = "tournament";
        } else {
            mode = "detailed";
        }
    }
    game_config();
}

void Simulation_game::start_game() {
    if (mode == "tournament") {
        tournament();
        return;
    }
    if (mode == "detailed" || mode == "fast") {
        std::vector<int> numbers_strategy {0,1,2};
        classic_mode(numbers_strategy);
        return;
    }
    throw std::invalid_argument("Ошибка в аргументе --mode");
}

std::vector<int> Simulation_game::scoring(std::vector<char>& choices) {
    std::vector<int> results;
    std::string combination = "";

    for (int i = 0; i < choices.size(); i++) {
        for (int j = 0; j < choices.size(); j++) {
            if (i != j) {
                combination += choices[j];
            }
        }
        std::sort(combination.begin(), combination.end());
        combination.insert(combination.begin(), choices[i]);
        results.push_back(rules[combination]);
        combination.clear();
    }
    return results;
}

void Simulation_game::print_results(std::vector<int>& results, std::vector<int>& number_strategy){

    for (int i = 0; i < number_strategy.size(); i++) {
        std::cout << strategy[number_strategy[i]] << ": " << results[i] << "\n";
    }
    std::cout << "-------------------------------------------" << "\n";
}


std::vector<int> Simulation_game::do_step(std::vector<std::unique_ptr<Prisoner_strategy>>& strategies) {

    std::vector<char> choices;
    char current_choice;
    for (int i = 0; i < strategies.size(); i++) {
        current_choice = strategies[i] -> step(history, current_step, i);
        choices.push_back(current_choice);
    }

    history.add_choices_strategy(choices);

    std::vector<int> results = scoring(choices);

    history.add_points_strategy(results,current_step);

    return results;
}


std::vector<int> Simulation_game::classic_mode(std::vector<int>& number_strategy) {

    std::vector<std::unique_ptr<Prisoner_strategy>> strategies;

    for (int i = 0; i < number_strategy.size(); i++) {
        strategies.push_back(Factory_objects::create_object(strategy[number_strategy[i]], dirname));
    }

    std::vector<int> results;

    while (current_step < steps_limit) {
        results = do_step(strategies);
        current_step++;
        if (mode == "detailed") {
            print_results(results, number_strategy);
        }
    }
    if (mode == "fast" || mode == "tournament") {
        print_results(results, number_strategy);
    }
    return results;
}

int Simulation_game::max_score(std::vector<int> &results, int &max_result) {
    int number_strategy = -1;
    for (int i = 0; i < 3; i++) {
        if (results[i] > max_result) {
            max_result = results[i];
            number_strategy = i;
        }
    }
    return number_strategy;
}

void Simulation_game::take_name_strategy(std::string& name_strategy, int &number_max, int &i, int &j, int &k) {
    switch (number_max) {
        case -1:
            break;
        case 0:
            name_strategy = strategy[i];
            break;
        case 1:
            name_strategy = strategy[j];
            break;
        case 2:
            name_strategy = strategy[k];
            break;
    }
}


void Simulation_game::tournament() {
    int max_result = -1;
    std::vector<int> results;
    std::string name_strategy;

    for (int i = 0; i < strategy.size() - 2; i++) {
        for (int j = i + 1; j < strategy.size() - 1; j++) {
            for (int k = j + 1; k < strategy.size(); k++) {
                std::vector<int> numbers_strategy {i, j, k};
                results = classic_mode(numbers_strategy);
                int number_max = max_score(results, max_result);
                take_name_strategy(name_strategy, number_max, i, j, k);
                current_step = 0;
                history.clear_history();
                numbers_strategy.clear();
            }
        }
    }
    std::cout << "Лучшая стратегия: " << name_strategy << " набрала " << std::to_string(max_result) << " очков" << "\n";
}

