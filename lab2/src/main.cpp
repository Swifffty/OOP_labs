#include <iostream>

#include "Simulation_game.hpp"

int main(int argc, char* argv[]) {
    if (argc < 4) {
        std::cout << "Недостаточно стратегий" << "\n";
        return 0;
    }

   try {
       Simulation_game game(argv, argc);
       game.start_game();
   } catch (const std::invalid_argument& error_message) {
       std::cerr << error_message.what() << "\n";
   }


    return 0;
}