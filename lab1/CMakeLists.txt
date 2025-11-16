cmake_minimum_required(VERSION 3.29)
project(BitArrayProject CXX)

set(CMAKE_CXX_STANDARD 20)

include(FetchContent)

FetchContent_Declare(
        googletest
        GIT_REPOSITORY https://github.com/google/googletest.git
        GIT_TAG        release-1.12.1
)
FetchContent_MakeAvailable(googletest)

set(BITARRAY_SOURCES
        src/BitArray.cpp
)
set(BITARRAY_INCLUDES
        ${CMAKE_CURRENT_SOURCE_DIR}/src
)

add_executable(lab1
        src/main.cpp
        ${BITARRAY_SOURCES}
)

target_include_directories(lab1 PRIVATE
        ${BITARRAY_INCLUDES}
)

add_executable(test_bitarray
        test/tests.cpp
        ${BITARRAY_SOURCES}
)

target_include_directories(test_bitarray PRIVATE
        ${BITARRAY_INCLUDES}
)

target_link_libraries(test_bitarray PRIVATE
        GTest::gtest_main
)

include(CTest)
add_test(NAME BitArrayTests COMMAND test_bitarray)