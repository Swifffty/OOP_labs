#ifndef SOUND_PROCESSOR_EXCEPTION_H
#define SOUND_PROCESSOR_EXCEPTION_H

#include <stdexcept>
#include <string>

class SoundProcessorException : public std::runtime_error {
public:
    explicit SoundProcessorException(const std::string& message)
            : std::runtime_error(message)
    {}
};


#endif