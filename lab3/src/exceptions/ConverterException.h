#ifndef CONVERTER_EXCEPTION_H
#define CONVERTER_EXCEPTION_H

#include "SoundProcessorException.h"

class ConverterException : public SoundProcessorException {
public:
    explicit ConverterException(const std::string& message) 
        : SoundProcessorException("Processing Error: " + message) {}

};

#endif