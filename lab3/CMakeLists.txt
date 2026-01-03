cmake_minimum_required(VERSION 3.10)
project(lab3)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

file(GLOB_RECURSE LIB_SOURCES
        "src/*.cpp"
        "converter/*.cpp"
)
list(FILTER LIB_SOURCES EXCLUDE REGEX "src/main.cpp")

add_executable(lab3 ${LIB_SOURCES} src/main.cpp
        src/creaters/Creater_mute.h
        src/creaters/Creater_echo.h
        src/creaters/Creater_mix.h
)

target_include_directories(lab3 PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}/src
)

if(MSVC)
    target_compile_options(lab3 PRIVATE /W4)
else()
    target_compile_options(lab3 PRIVATE -Wall -Wextra -Wpedantic)
endif()

find_package(GTest QUIET)

if(GTest_FOUND)
    enable_testing()

    add_executable(unit_tests
            ${LIB_SOURCES}
            tests/test.cpp
            src/creaters/Creater_mute.h
            src/creaters/Creater_mix.h
    )

    target_include_directories(unit_tests PRIVATE
            ${CMAKE_CURRENT_SOURCE_DIR}
            ${CMAKE_CURRENT_SOURCE_DIR}/src
    )
    target_link_libraries(unit_tests PRIVATE GTest::GTest GTest::Main)

    add_test(NAME AllProcessorTests COMMAND unit_tests)

    message(STATUS "Google Test found. Target 'unit_tests' created.")
else()
    message(WARNING "Google Test not found! Please install it to run tests.")
endif()