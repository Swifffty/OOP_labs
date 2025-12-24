#pragma once

#include <memory>
#include <stdexcept>

#include "Tit_for_Tat.hpp"
#include "Always_C.hpp"
#include "Always_D.hpp"
#include "Bully.h"
#include <string>

class Factory_objects {
public:
    static std::unique_ptr<Prisoner_strategy> create_object(const std::string& name_strategy, const std::string config_dir) {
        
        if (name_strategy == "Tit_for_Tat") {
            return std::make_unique<Tit_for_Tat>();
        }
        
        if (name_strategy == "Always_C"){
            return std::make_unique<Always_C>(); 
        }        

        if (name_strategy == "Always_D") {
            return std::make_unique<Always_D>();
        }

        if (name_strategy == "Bully") {
            return std::make_unique<Bully>(config_dir);
        }
        throw std::invalid_argument("Неверное имя стратегии");
    }
};