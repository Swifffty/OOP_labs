#include "Trigger.h"

char Trigger::step(History_game& history, int& step, int& index_strategy) {
    if (step == 0) {
        return 'C';
    }

    if (trigger) {
        return 'D';
    }
    for (int i = 0; i < 3; i++) {
        if (i == index_strategy) {
            continue;
        }
        if (history.get_history_strategy(i, step) == 'D') {
            trigger = true;
            return 'D';
        }
    }
}

