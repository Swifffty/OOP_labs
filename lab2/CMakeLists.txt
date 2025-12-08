cmake_minimum_required(VERSION 3.10.0)
project(lab2 VERSION 0.1.0 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

find_package(GTest REQUIRED)

add_executable(run_test
        src/strategy/Prisoner_strategy.cpp
        src/strategy/Always_C.cpp
        src/History_game.cpp
        src/Simulation_game.cpp
        src/strategy/Tit_for_Tat.cpp
        src/strategy/Always_D.cpp
        src/strategy/Bully.cpp
        tests/tests.cpp)

target_include_directories(run_test
        PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/src
        ${CMAKE_CURRENT_SOURCE_DIR}/src/strategy
)

target_link_libraries(run_test
        PRIVATE
        GTest::gtest_main
        pthread
)

set(SOURCE_DIR src)

set(PROJECT_SOURCES
    ${SOURCE_DIR}/main.cpp
    ${SOURCE_DIR}/History_game.cpp
    ${SOURCE_DIR}/Simulation_game.cpp
    ${SOURCE_DIR}/strategy/Always_C.cpp
    ${SOURCE_DIR}/strategy/Tit_for_Tat.cpp
    ${SOURCE_DIR}/strategy/Prisoner_strategy.cpp
    ${SOURCE_DIR}/strategy/Always_D.cpp
        src/strategy/Bully.cpp
        src/strategy/Bully.h
        src/strategy/Trigger.cpp
        src/strategy/Trigger.h
)

add_executable(lab2 ${PROJECT_SOURCES}
)

target_include_directories(lab2 PRIVATE
    ${SOURCE_DIR}            
    ${SOURCE_DIR}/strategy    
)

include(CTest)
enable_testing()
add_test(NAME GTest_Suite COMMAND run_test)
