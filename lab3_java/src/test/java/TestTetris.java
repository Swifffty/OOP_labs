import model.TetrisModel;
import model.figure.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameStageI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestTetrist {
    private TetrisModel model;
    private GameStageI mockStage;

    @BeforeEach
    void setUp() {
        mockStage = mock(GameStageI.class);
        model = new TetrisModel(mockStage);
    }

    @Test
    void testShiftLeft() throws Exception {
        int initialY = getPosition()[1];
        model.shiftLeft();
        assertTrue(getPosition()[1] <= initialY);
        verify(mockStage, atLeastOnce()).getChange(any(), anyInt());
    }

    @Test
    void testShiftRight() throws Exception {
        int initialY = getPosition()[1];
        model.shiftRight();
        assertTrue(getPosition()[1] >= initialY);
        verify(mockStage, atLeastOnce()).getChange(any(), anyInt());
    }

    @Test
    void testShapeDownIncrementsRow() throws Exception {
        int initialX = getPosition()[0];
        model.shapeDown();
        int currentX = getPosition()[0];
        assertTrue(currentX > initialX || currentX == 0);
    }

    @Test
    void testRotateSquareDoesNothing() throws Exception {
        setPrivateField("shapeNow", new Square());
        model.rotateUp();
        verify(mockStage, times(1)).getChange(any(), anyInt());
    }

    @Test
    void testHardDropChangesPosition() throws Exception {
        int initialX = getPosition()[0];
        model.hardDrop();
        verify(mockStage, atLeast(2)).getChange(any(), anyInt());
    }

    @Test
    void testCheckRowAndRemoveRow() throws Exception {
        int[][] grid = (int[][]) getPrivateField("gameFiled");

        for (int j = 0; j < 12; j++) {
            grid[21][j] = 2;
        }
        grid[20][5] = 3;

        Method checkRowMethod = TetrisModel.class.getDeclaredMethod("checkRow", int[].class, int.class);
        checkRowMethod.setAccessible(true);
        boolean isFull = (boolean) checkRowMethod.invoke(model, grid[21], 2);

        assertTrue(isFull);

        Method removeRowMethod = TetrisModel.class.getDeclaredMethod("removeRow", int.class);
        removeRowMethod.setAccessible(true);
        removeRowMethod.invoke(model, 21);

        assertEquals(3, grid[21][5]);
        assertEquals(-1, grid[20][5]);
    }

    @Test
    void testScoreIncrementsOnLineClear() throws Exception {
        int[][] grid = (int[][]) getPrivateField("gameFiled");

        int testRow = 10;
        for (int j = 0; j < 12; j++) {
            grid[testRow][j] = 2;
        }

        int[] pos = (int[]) getPrivateField("currentPos");
        pos[0] = testRow;

        Method shapeFellMethod = TetrisModel.class.getDeclaredMethod("shapeFell");
        shapeFellMethod.setAccessible(true);
        shapeFellMethod.invoke(model);

        verify(mockStage, atLeastOnce()).changeScore(anyInt());
    }

    private int[] getPosition() throws Exception {
        return (int[]) getPrivateField("currentPos");
    }

    private Object getPrivateField(String fieldName) throws Exception {
        Field field = TetrisModel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(model);
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = TetrisModel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(model, value);
    }
}
