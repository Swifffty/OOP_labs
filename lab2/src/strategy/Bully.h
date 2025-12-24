#pragma once
#include "Prisoner_strategy.hpp"

class Bully : public Prisoner_strategy {
public:
    Bully(const std::string& config_dir);
    char step(History_game& history, int& step, int& index_strategy) override;
private:
    int n;
};
