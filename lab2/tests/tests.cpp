#include "gtest/gtest.h"
#include "History_game.hpp"
#include "Simulation_game.hpp"
#include "strategy/Prisoner_strategy.hpp"
#include <sstream>
#include <iostream>
#include <vector>
#include <map>
#include <memory>
#include <functional>
#include <string>

// =========================================================================
// 1. МОКОВЫЕ СТРАТЕГИИ (Необходимы для тестирования Simulation_game)
// =========================================================================

// Предполагаем, что эти классы существуют в src/strategy
class Always_C : public Prisoner_strategy {
public:
    char step(History_game& history, int& my_score, int& opponent_score) override { return 'C'; }
};

class Always_D : public Prisoner_strategy {
public:
    char step(History_game& history, int& my_score, int& opponent_score) override { return 'D'; }
};

class Tit_for_Tat : public Prisoner_strategy {
public:
    char step(History_game& history, int& my_score, int& opponent_score) override {
        // Упрощенная заглушка для тестов
        return 'C';
    }
};

// =========================================================================
// 2. ТЕСТЫ ДЛЯ HISTORY_GAME (Только публичный интерфейс)
// =========================================================================

struct HistoryGamePublicTest : public ::testing::Test {
    History_game history;

    void SetUp() override {
        history.clear_history();
    }
};

TEST_F(HistoryGamePublicTest, InitializationAndClear) {
    // Проверяем, что get_history_strategy для несуществующего шага возвращает 'N' (если так реализовано)
    int step_zero = 0;
    int strategy_0 = 0;
    EXPECT_EQ(history.get_history_strategy(strategy_0, step_zero), 'N');

    std::vector<char> choices = {'C', 'D', 'C'};
    history.add_choices_strategy(choices);
    history.clear_history();

    EXPECT_EQ(history.get_history_strategy(strategy_0, step_zero), 'N');
}

TEST_F(HistoryGamePublicTest, AddAndGetChoices) {
    std::vector<char> choices_step1 = {'C', 'D', 'C'};
    history.add_choices_strategy(choices_step1);

    int step_index_1 = 1;
    int strategy_0 = 0;
    int strategy_1 = 1;

    EXPECT_EQ(history.get_history_strategy(strategy_0, step_index_1), 'C');
    EXPECT_EQ(history.get_history_strategy(strategy_1, step_index_1), 'D');

    int step_index_99 = 99;
    EXPECT_EQ(history.get_history_strategy(strategy_0, step_index_99), 'N');
}

TEST_F(HistoryGamePublicTest, AddAndGetAccumulatedPoints) {
    // Шаг 0: {5, 0, 3}
    std::vector<int> points_initial = {5, 0, 3};
    int step0 = 0;
    history.add_points_strategy(points_initial, step0);

    // Шаг 1: Добавляем {1, 2, 1}. Накоплено: {6, 2, 4}
    std::vector<int> points_step1 = {1, 2, 1};
    int step1 = 1;
    history.add_points_strategy(points_step1, step1);

    int strategy_1 = 1;
    std::vector<int> history_points_1 = history.get_history_points(strategy_1);

    // Проверяем накопленные баллы Стратегии 1 (0 -> 2)
    // ВАЖНО: зависит от того, как реализовано накопление в add_points_strategy
    // Если step=0 просто устанавливает начальный счет, а не добавляет:
    EXPECT_EQ(history_points_1.size(), 2);
    // EXPECT_EQ(history_points_1[0], 0); // Начальный счет (заглушка)
    EXPECT_EQ(history_points_1.back(), 2); // Последний накопленный счет
}

// =========================================================================
// 3. ТЕСТЫ ДЛЯ SIMULATION_GAME (Только публичные методы и проверка вывода)
// =========================================================================

struct SimulationGamePublicTest : public ::testing::Test {
    // Вспомогательная функция для перехвата stdout
    std::string capture_output(std::function<void()> func) {
        std::stringstream ss;
        std::streambuf* old_cout = std::cout.rdbuf();
        std::cout.rdbuf(ss.rdbuf());

        func();

        std::cout.rdbuf(old_cout);
        return ss.str();
    }

    // Helper для создания аргументов
    std::vector<char*> create_argv(const std::vector<std::string>& args) {
        std::vector<char*> argv_test;
        for (auto& arg : args) {
            argv_test.push_back(const_cast<char*>(arg.c_str()));
        }
        return argv_test;
    }
};

// Тест 1: Проверка турнира C vs C vs C (ожидаем равенство и победу первого)
TEST_F(SimulationGamePublicTest, TournamentAllCooperateOutput) {
    std::vector<std::string> args = {
            "./game_exec", "Always_C", "Always_C", "Always_C", "--steps=1"
    };
    std::vector<char*> argv_test = create_argv(args);
    int argc_test = argv_test.size();

    Simulation_game game(argv_test.data(), argc_test);

    std::string output = capture_output([&]() {
        game.start_game();
    });

    EXPECT_TRUE(output.find("Winner: Always_C") != std::string::npos)
                        << "Ожидалось, что победителем будет Always_C (при равенстве)";
    EXPECT_TRUE(output.find("Simulation finished") != std::string::npos);
}

// Тест 2: Проверка турнира D vs C vs C (ожидаем победу D)
TEST_F(SimulationGamePublicTest, TournamentDefectWinsOutput) {
    std::vector<std::string> args = {
            "./game_exec", "Always_D", "Always_C", "Tit_for_Tat", "--steps=1"
    };
    std::vector<char*> argv_test = create_argv(args);
    int argc_test = argv_test.size();

    Simulation_game game(argv_test.data(), argc_test);

    std::string output = capture_output([&]() {
        game.start_game();
    });

    EXPECT_TRUE(output.find("Winner: Always_D") != std::string::npos)
                        << "Ожидалось, что победителем будет Always_D";
}

// Тест 3: Проверка режима Fast (Полная матрица)
TEST_F(SimulationGamePublicTest, FullScaleModeOutput) {
    std::vector<std::string> args = {
            "./game_exec", "Always_D", "Always_C", "--steps=5", "--mode=fast"
    };
    std::vector<char*> argv_test = create_argv(args);
    int argc_test = argv_test.size();

    Simulation_game game(argv_test.data(), argc_test);

    std::string output = capture_output([&]() {
        game.start_game();
    });

    // В режиме F (fast) вывод должен содержать общую таблицу очков
    EXPECT_TRUE(output.find("Total Scores") != std::string::npos)
                        << "Вывод не содержит 'Total Scores' (ожидаемый заголовок для режима F)";

    // Проверяем, что обе стратегии упомянуты в результатах.
    EXPECT_TRUE(output.find("Always_D") != std::string::npos && output.find("Always_C") != std::string::npos)
                        << "Вывод не содержит результаты для всех стратегий";
}
