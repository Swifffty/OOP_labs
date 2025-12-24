cmake_minimum_required(VERSION 3.14)
project(lab2 VERSION 0.1.0 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_CXX_EXTENSIONS OFF)

add_subdirectory(libs/googletest)

include(GoogleTest)

include(CTest)
enable_testing()

set(SOURCE_DIR src)
set(TEST_DIR tests)

set(PROJECT_SOURCES_NO_HEADERS
        ${SOURCE_DIR}/main.cpp
        ${SOURCE_DIR}/History_game.cpp
        ${SOURCE_DIR}/Simulation_game.cpp
        ${SOURCE_DIR}/strategy/Always_C.cpp
        ${SOURCE_DIR}/strategy/Tit_for_Tat.cpp
        ${SOURCE_DIR}/strategy/Prisoner_strategy.cpp
        ${SOURCE_DIR}/strategy/Always_D.cpp
        ${SOURCE_DIR}/strategy/Bully.cpp
        ${SOURCE_DIR}/strategy/Trigger.cpp
)

add_executable(lab2 ${PROJECT_SOURCES_NO_HEADERS})

target_include_directories(lab2 PRIVATE
        ${SOURCE_DIR}
        ${SOURCE_DIR}/strategy
)

set(TEST_SOURCES
        ${TEST_DIR}/tests.cpp
        ${PROJECT_SOURCES_NO_HEADERS}
)

add_executable(run_test ${TEST_SOURCES})

target_include_directories(run_test PRIVATE
        ${SOURCE_DIR}
        ${SOURCE_DIR}/strategy
)

target_link_libraries(run_test PRIVATE
        GTest::gtest
        GTest::gtest_main
        GTest::gmock
)

gtest_discover_tests(run_test
        TEST_PREFIX "Tests_"
        TEST_SUFFIX ""
        PROPERTIES TIMEOUT 60
)

add_custom_target(test_all
        COMMAND ${CMAKE_CTEST_COMMAND} --output-on-failure
        DEPENDS run_test
        COMMENT "Running all tests"
)
