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

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }
    //Используется в Comparator in class TakenPiecesPanel
    public int getPieceValue(){
        //делегирование метода в перечисление pieceType
        return this.pieceType.getPieceValue();
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    public abstract Piece movePiece(Move move);

    public boolean setEnemies() {
        return this.enemies = true;
    }
    public boolean getEnemies(){
        return this.enemies;
    }
    public enum PieceType{
        WARRIOR("W", 500) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }
        };
        private String pieceName;
        private int pieceValue;
        PieceType(final String pieceName,
                  final int pieceValue){
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }
        @Override
        public String toString(){
            return this.pieceName;
        }
        public int getPieceValue(){
            return this.pieceValue;
        }
        public abstract boolean isKing();
    }
}

