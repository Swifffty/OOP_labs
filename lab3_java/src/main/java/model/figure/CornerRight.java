package model.figure;

public class CornerRight implements IFigure {
    private int[][] coords;

    @Override
    public int[][] getCoords() {
        int[][] copyCoords = new int[4][2];
        for (int i = 0; i < 4; i++) {
            copyCoords[i] = coords[i].clone();
        }
        return copyCoords;
    }

    public CornerRight() {
        coords = new int[][]{
                {0, 1},
                {0, 0},
                {1, 0},
                {2, 0}
        };
    }
    @Override
    public CornerRight getCopy() {
        CornerRight newObj = new CornerRight();
        return newObj;
    }

    @Override
    public int[][] getRevertCoords() {
        int[][] coordsCp = new int[4][2];
        for (int i = 0; i < 4; i++) {
            coordsCp[i] = coords[3 - i].clone();
        }
        return coordsCp;
    }

    @Override
    public void changeCoords(int[][] newCoords) {
        for (int i = 0; i < 4; i++) {
            coords[i] = newCoords[i].clone();
        }
    }
}
