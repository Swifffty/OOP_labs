#ifndef FILE_EXCEPTION_H
#define FILE_EXCEPTION_H

#include "SoundProcessorException.h"

class FileException : public SoundProcessorException {
public:
    explicit FileException(const std::string& message) 
        : SoundProcessorException("File Error: " + message) {}

};

#endif