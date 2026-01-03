cmake_minimum_required(VERSION 4.0)
project(test_bitarray)

set(CMAKE_CXX_STANDARD 14)

find_package(GTest REQUIRED)
include(CTest)

add_executable(test_bitarray tests.cpp)

target_include_directories(test_bitarray PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/../src
)

target_link_libraries(test_bitarray
        GTest::gtest_main
)

add_test(
        NAME BitArrayTests
        COMMAND bitarray_tests
)
