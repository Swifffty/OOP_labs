cmake_minimum_required(VERSION 3.10)

project(lab3 CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

file(GLOB_RECURSE SOURCES "src/*.cpp" "src/*.h")

add_executable(lab3 ${SOURCES}
        src/WavStruct.h
        src/WavReader.cpp
        src/WavReader.h
        src/WavWriter.cpp
        src/WavWriter.h)

target_compile_options(lab3 PRIVATE
        -Wall
        -Wextra
        -pedantic
)

