#ifndef CONVERTER_FACTORY_H
#define CONVERTER_FACTORY_H

#include <map>
#include <string>
#include <vector>
#include <memory>
#include <functional>
#include "../converter/Converter.h"

class ConverterFactory {
public:
    ConverterFactory();

    std::unique_ptr<Converter> createConverter(const std::string& cmd, const std::vector<std::string>& args, const std::vector<std::string>& inputFiles);

    void printAllHelp();

private:

    using CreatorFunc = std::function<std::unique_ptr<Converter>(const std::vector<std::string>&,const std::vector<std::string>&)>;

    std::map<std::string, CreatorFunc> _creators;

    void registerAll();
};

#endif