#include "Tit_for_Tat.hpp"

 char Tit_for_Tat::step(History_game& history, int& step, int& index_strategy) {
    if (step == 0) {
        return 'C';
    }

     char last_choice;
     for (int i = 0; i < 3; i++) {
         if (i == index_strategy) {
             continue;
         }
         last_choice = history.get_history_strategy(i, step);
         if (last_choice == 'D') {
             return 'D';
         }
     }
     return 'C';
 }