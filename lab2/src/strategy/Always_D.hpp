#pragma once 

#include "Prisoner_strategy.hpp"

class Always_D : public Prisoner_strategy {
public:
    char step(History_game& history, int& step, int& index_strategy) override;
};