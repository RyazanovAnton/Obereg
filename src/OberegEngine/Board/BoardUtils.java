package OberegEngine.Board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    // Массив булевых значений для проверки исключений по первому столбцу
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    // Массив булевых значений для проверки исключений по полсднему столбцу
    public static final boolean[] NINTH_COLUMN = initColumn(8);
    // Количество тайлов на игровом полу
    public static final int NUM_TILES = 81;
    // Количество тайлов в ряду (поле 9х9)
    public static final int NUM_TILES_PER_ROW = 9;
    // Константа для поиска соседних по ряду тайлов
    public static final int NEXT_ON_RAW = 1;
    // Константа для поиска соседних по столбцу тайлов
    public static final int NEXT_ON_COLUMN = 9;
    // Метод для занесения тайлов в массивы первого и последнего столбца
    // 0 9 18 27 36 45 54 63 72
    // 8 17 26 35 44 53 62 71 80
    private static boolean[] initColumn(int columnNumber){
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber] =true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber<NUM_TILES);

        return column;
    }
    // Проверка действительности тайла при обращении ему через порядковый номер
    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }
}
