#pragma once
#include "../History_game.hpp"

class Prisoner_strategy {
public:
    virtual char step(History_game& history, int& step, int& index_strategy) = 0;

    virtual ~Prisoner_strategy() = 0;

};