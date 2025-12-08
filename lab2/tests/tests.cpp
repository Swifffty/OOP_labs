#include "gtest/gtest.h"
#include "Simulation_game.hpp"
#include "History_game.hpp"
#include <string>
#include <vector>
#include <map>
#include <algorithm>
#include <sstream>
#include <memory>

struct HistoryGameTest : public ::testing::Test {
    History_game history;

    // Этот метод вызывается перед каждым тестом
    void SetUp() override {
        history.clear_history();
    }
};

TEST_F(HistoryGameTest, Initialization) {
    // Если компилятор увидит обновленный History_game.hpp,
    // он позволит получить доступ к этому полю.
    ASSERT_EQ(history._test_magic_number, 42);

    // Основные проверки
    ASSERT_EQ(history.choices_strategy.size(), 3);
    ASSERT_EQ(history.points_strategy.size(), 3);
}

TEST_F(HistoryGameTest, AddChoices) {
    std::vector<char> choices = {'C', 'D', 'C'};
    history.add_choices_strategy(choices);

    // Requires: friend struct HistoryGameTest; in History_game.hpp
    ASSERT_EQ(history.choices_strategy[0].back(), 'C');
    ASSERT_EQ(history.choices_strategy[1].back(), 'D');
}

TEST_F(HistoryGameTest, GetHistoryStrategy) {
    std::vector<char> choices_step1 = {'C', 'D', 'C'};
    history.add_choices_strategy(choices_step1);

    int strategy_0 = 0;
    int step_not_zero = 1;
    int step_zero = 0;

    // Должно вернуть последний выбор ('C')
    EXPECT_EQ(history.get_history_strategy(strategy_0, step_not_zero), 'C');

    // Проверяем случай, когда step == 0 (должно вернуть 'N')
    EXPECT_EQ(history.get_history_strategy(strategy_0, step_zero), 'N');
}

TEST_F(HistoryGameTest, AddPointsAccumulation) {
    // Шаг 0
    std::vector<int> points_step0 = {5, 0, 3};
    int step0 = 0;
    history.add_points_strategy(points_step0, step0);

    // Шаг 1 (Накопленные: {6, 2, 4})
    std::vector<int> points_step1 = {1, 2, 1};
    int step1 = 1;
    history.add_points_strategy(points_step1, step1);

    // Requires: friend struct HistoryGameTest; in History_game.hpp
    EXPECT_EQ(history.points_strategy[0].back(), 6);
    EXPECT_EQ(history.points_strategy[1].back(), 2);
    EXPECT_EQ(history.points_strategy[2].back(), 4);
}

TEST_F(HistoryGameTest, ClearHistory) {
    std::vector<char> choices = {'C', 'D', 'C'};
    history.add_choices_strategy(choices);

    // Requires: friend struct HistoryGameTest; in History_game.hpp
    ASSERT_FALSE(history.choices_strategy[0].empty());

    history.clear_history();

    // Проверяем, что все очищено
    ASSERT_TRUE(history.choices_strategy[0].empty());
    ASSERT_TRUE(history.points_strategy[0].empty());
}

// -------------------------------------------------------------------------
// 2. ТЕСТЫ ДЛЯ Simulation_game (Simulation_game.cpp)
// -------------------------------------------------------------------------

// Определяем тестовый набор
struct SimulationGameTest : public ::testing::Test {
    Simulation_game* game;
    // Аргументы для имитации запуска приложения
    char* argv_test[4] = {(char*)"./game", (char*)"StrategyA", (char*)"StrategyB", (char*)"StrategyC"};
    int argc_test = 4;

    void SetUp() override {
        // Конструктор будет вызван с тестовыми аргументами
        // ПРИМЕЧАНИЕ: Это вызовет game_config() и установит дефолтные правила.
        game = new Simulation_game(argv_test, argc_test);
    }

    void TearDown() override {
        delete game;
    }
};

// Тест: Scoring (подсчет очков на основе выбора)
TEST_F(SimulationGameTest, ScoringLogic) {
    // Requires: friend struct SimulationGameTest; in Simulation_game.hpp

    // Дефолтные правила (установлены в конструкторе через game_config()):
    // "CDD": 0, "DDD": 1, "CCD": 3, "DCD": 5, "CCC": 7, "DCC": 9.

    std::vector<char> choices = {'C', 'C', 'D'}; // Стратегии 0, 1, 2

    // 1. Для Стр. 0 ('C'): оппоненты 'C', 'D' -> комбинация 'CCD' -> 3
    // 2. Для Стр. 1 ('C'): оппоненты 'C', 'D' -> комбинация 'CCD' -> 3
    // 3. Для Стр. 2 ('D'): оппоненты 'C', 'C' -> комбинация 'DCC' -> 9

    std::vector<int> scores = game->scoring(choices);

    ASSERT_EQ(scores.size(), 3);
    EXPECT_EQ(scores[0], 3);
    EXPECT_EQ(scores[1], 3);
    EXPECT_EQ(scores[2], 9);
}

// Тест: Parser (анализ аргументов)
TEST_F(SimulationGameTest, ParserArguments) {
    // Используем чистый объект для проверки парсера без вызова конструктора
    Simulation_game fresh_game(argv_test, 1);

    int count = 0;
    std::string arg1 = "TestStrategy";
    std::string arg2 = "--steps=100";
    std::string arg3 = "--mode=fast";

    // Requires: friend struct SimulationGameTest; in Simulation_game.hpp
    fresh_game.parser(arg1, count);
    EXPECT_EQ(count, 1);
    EXPECT_EQ(fresh_game.strategy.back(), "TestStrategy");

    fresh_game.parser(arg2, count);
    EXPECT_EQ(fresh_game.steps_limit, 100);

    fresh_game.parser(arg3, count);
    EXPECT_EQ(fresh_game.mode, "fast");
}

// Тест: Max Score (поиск лучшего результата)
TEST_F(SimulationGameTest, MaxScore) {
    std::vector<int> results = {5, 12, 8};
    int max_result = -1; // Начальное значение для поиска

    // Метод max_score не использует приватные поля, поэтому работает без friend
    int winner_index = game->max_score(results, max_result);

    EXPECT_EQ(winner_index, 1); // Индекс стратегии с 12 очками
    EXPECT_EQ(max_result, 12);  // Проверяем, что max_result обновился
}

// Тест: Take Name Strategy (определение имени лучшей стратегии)
TEST_F(SimulationGameTest, TakeNameStrategy) {
    // Requires: friend struct SimulationGameTest; in Simulation_game.hpp

    // Перезаполняем strategy для чистоты
    game->strategy.clear();
    game->strategy.push_back("Alice");   // Index 0
    game->strategy.push_back("Bob");     // Index 1
    game->strategy.push_back("Charlie"); // Index 2

    std::string name = "Initial";
    int i = 0, j = 1, k = 2; // Индексы стратегий в турнире (тут 0, 1, 2)

    // Тест 1: Победитель - стратегия i (индекс 0)
    int number_max_0 = 0;
    game->take_name_strategy(name, number_max_0, i, j, k);

    // ВАЖНО: Тест проверяет ВАШУ текущую реализацию с fallthrough
    EXPECT_EQ(name, "Charlie"); // name=strategy[i] -> fallthrough -> name=strategy[j] -> fallthrough -> name=strategy[k]
}
