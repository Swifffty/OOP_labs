package model.figure;

public interface IFigure {
    public int[][] getCoords();
    public IFigure getCopy();
    public int[][] getRevertCoords();
    public void changeCoords(int[][] newCoords);
}
