package view;

public interface GameStageI {
    void getChange(int[][] field, int color);

    void setNextShape(int[][] coords, int color);

    void changeScore(int score);

    void GameOver(int score);
}
