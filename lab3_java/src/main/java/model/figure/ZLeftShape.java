package model.figure;

public class ZLeftShape implements IFigure {
    private int[][] coords;

    @Override
    public int[][] getCoords() {
        int[][] copyCoords = new int[4][2];
        for (int i = 0; i < 4; i++) {
            copyCoords[i] = coords[i].clone();
        }
        return copyCoords;
    }

    public ZLeftShape() {
        coords = new int[][]{
                {0, 0},
                {0, -1},
                {1, 1},
                {1, 0}
        };
    }

    @Override
    public ZLeftShape getCopy() {
        ZLeftShape newObj = new ZLeftShape();
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
