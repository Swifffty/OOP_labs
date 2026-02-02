#pragma once 

#include "Prisoner_strategy.hpp"

class Tit_for_Tat : public Prisoner_strategy {
public:
    char step(History_game& history, int& step, int& index_strategy) override;
    
};