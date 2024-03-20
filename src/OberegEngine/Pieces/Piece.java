package OberegEngine.Pieces;

import OberegEngine.Player.Alliance;
import OberegEngine.Board.Board;
import OberegEngine.Board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    private final int cashedHashCode;

    private boolean enemies;
    Piece(final PieceType pieceType,
          final int piecePosition,
          final Alliance pieceAlliance
    ){
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.cashedHashCode = computeHashCode();
    }
    // TODO пока что не нужен
    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        return result;
    }
    // Переопределяем стандартный метод equals class Object с равенства ссылок, на равенство объектов
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() ;
    }
    // Получить порядковый номер координаты фигуры
    public int getPiecePosition(){
        return this.piecePosition;
    }
    // Определить принадлежность фигуры к альянсу Vikings / Slavs
    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
    // Получить тип фигуры WARRIOR / KING
    public PieceType getPieceType(){
        return this.pieceType;
    }
    // Получить ценность фигуры
    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }
    // Подсчет всех возможных ходов фигуры
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    // Служебный метод для перемещения фигуры
    public abstract Piece movePiece(Move move);
    // Фигура была захвачена противником
    public boolean setEnemies() {
        return this.enemies = true;
    }
    // Получить информацию о состоянии фигуры
    public boolean getEnemies(){
        return this.enemies;
    }

    public boolean resetEnem() {
        return this.enemies = false;
    }
}