package OberegEngine.Board;

import OberegEngine.Pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// Абстрактный класс Тайл и его два наследника: пустой и занятый тайл
public abstract class Tile {
    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
    // Создание мапы пустых (91) тайлов
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTileMap);
    }
    // Создание тайла. Если занят фигурой, то занятый,
    // в противном случе занимает место в мапе пустых тайлов
    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    // Конструктор класса тайл с доступом только из самого класса
    private Tile(final int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    // Абстрактный метод с возвратом логического значения о занятости тайла
    public abstract boolean isTileOccupied();
    // Абстрактный метода для получения доступу к фигуре, расположенной на тайле
    public abstract Piece getPiece();
    // Доступ к порядковму номеру тайла
    public int getTileCoordinate(){
        return this.tileCoordinate;
    }

    // Первый наследник - пустой тайл
    private static final class EmptyTile extends Tile{
        public EmptyTile(final int coordinate){
            super(coordinate);
        }
        @Override
        public boolean isTileOccupied(){
            return false;
        }
        @Override
        public Piece getPiece() {
            return null;
        }
    }
    // Второй наследник - занятый тайл
    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;
        private OccupiedTile(int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

    }
}
