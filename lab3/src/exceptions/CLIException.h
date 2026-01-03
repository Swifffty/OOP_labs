#ifndef CLI_EXCEPTION_H
#define CLI_EXCEPTION_H

#include "SoundProcessorException.h"

class CLIException : public SoundProcessorException {
public:
    explicit CLIException(const std::string& message) 
        : SoundProcessorException("CLI Error: " + message) {}

};

#endif