cmake_minimum_required(VERSION 3.29)
project(0b)

set(CMAKE_CXX_STANDARD 20)

if(CMAKE_BUILD_TYPE STREQUAL "Coverage")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fprofile-arcs -ftest-coverage")
    set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -fprofile-arcs -ftest-coverage")
endif()

add_executable(0b main.cpp
        Dictinory.cpp
        Dictinory.h
        fill_map.cpp
        fill_map.h
)

enable_testing()

find_package(GTest REQUIRED)

add_executable(run_tests test_lab0b.cpp Dictinory.cpp fill_map.cpp)

target_link_libraries(run_tests GTest::gtest GTest::gtest_main)

add_test(NAME test_lab COMMAND run_tests)