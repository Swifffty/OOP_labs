#ifndef CONFIG_EXCEPTION_H
#define CONFIG_EXCEPTION_H

#include "SoundProcessorException.h"

class ConfigException : public SoundProcessorException {
public:
    explicit ConfigException(const std::string& message) 
        : SoundProcessorException("Config Error: " + message) {}

};

#endif