#pragma once
#include "Prisoner_strategy.hpp"

class Trigger : private Prisoner_strategy{
public:
    char step(History_game& history, int& step, int& index_strategy) override;
private:
    bool trigger = false;
};
