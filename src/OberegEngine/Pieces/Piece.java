package OberegEngine.Pieces;

import OberegEngine.Board.BoardUtils;
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
          final Alliance pieceAlliance){
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.cashedHashCode = computeHashCode();
        this.enemies = false;
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
    // Поиск врагов вокруг воина
    public void searchPieceEnemies(Board board) {
        for(Piece piece : board.currentPlayer().getActivePieces()){
            // Если князь расположен на троне, то захватить его могут только 4 врага
            if (this.getPieceType().isKing() && this.getPiecePosition() == 40){
                if(board.isEnemyOnTheRight(board.getTile(40)) &&
                        board.isEnemyOnTheLeft(board.getTile(40)) &&
                        board.isEnemyOnTheTop(board.getTile(40)) &&
                        board.isEnemyOnTheBottom(board.getTile(40))
                ){
                    this.setEnemies();
                }
            }
            // Если князь расположен рядом с троном, то захватить его могут только 3 врага
            if (this.getPieceType().isKing() && this.getPiecePosition() != 40){
                if(     (this.getPiecePosition() == 39 &&
                        board.isEnemyOnTheLeft(board.getTile(39)) &&
                        board.isEnemyOnTheTop(board.getTile(39)) &&
                        board.isEnemyOnTheBottom(board.getTile(39))) ||
                        (this.getPiecePosition() == 31 &&
                                board.isEnemyOnTheLeft(board.getTile(31)) &&
                                board.isEnemyOnTheTop(board.getTile(31)) &&
                                board.isEnemyOnTheRight(board.getTile(31))) ||
                        (this.getPiecePosition() == 41 &&
                                board.isEnemyOnTheRight(board.getTile(41)) &&
                                board.isEnemyOnTheTop(board.getTile(41)) &&
                                board.isEnemyOnTheBottom(board.getTile(41))) ||
                        (this.getPiecePosition() == 49 &&
                                board.isEnemyOnTheLeft(board.getTile(49)) &&
                                board.isEnemyOnTheRight(board.getTile(49)) &&
                                board.isEnemyOnTheBottom(board.getTile(49)))
                ){
                    this.setEnemies();
                }
            }
            if( this.getPieceType().isKing() &&
                    (this.getPiecePosition() != 40 &&
                            this.getPiecePosition() != 31 &&
                            this.getPiecePosition() != 39 &&
                            this.getPiecePosition() != 41 &&
                            this.getPiecePosition() != 49)
            ){
                // Проверка не зажат ли Князь на поле между двумя врагами по горизонтали
                if (BoardUtils.isValidTileCoordinate(this.getPiecePosition() - BoardUtils.NEXT_ON_RAW) &&
                        BoardUtils.isValidTileCoordinate(this.getPiecePosition() + BoardUtils.NEXT_ON_RAW)) {
                    if (board.isEnemyOnTheLeft(board.getTile(this.getPiecePosition())) &&
                            board.isEnemyOnTheRight(board.getTile(this.getPiecePosition()))) {
                        this.setEnemies();
                    }
                }
                // Проверка не зажат ли Князь на поле двумя врагами по вертикали
                if (BoardUtils.isValidTileCoordinate(this.getPiecePosition() - BoardUtils.NEXT_ON_COLUMN) &&
                        BoardUtils.isValidTileCoordinate(this.getPiecePosition() + BoardUtils.NEXT_ON_COLUMN)) {
                    if (board.isEnemyOnTheTop(board.getTile(this.getPiecePosition())) &&
                            board.isEnemyOnTheBottom(board.getTile(this.getPiecePosition()))) {
                        this.setEnemies();
                    }
                }
            }
            if(this.getPiecePosition() != 40 && (!this.getPieceType().isKing())){
                // Проверка не зажат ли Воин на поле между двумя врагами по горизонтали
                if (BoardUtils.isValidTileCoordinate(this.getPiecePosition() - BoardUtils.NEXT_ON_RAW) &&
                        BoardUtils.isValidTileCoordinate(this.getPiecePosition() + BoardUtils.NEXT_ON_RAW)) {
                    if (board.isEnemyOnTheLeft(board.getTile(this.getPiecePosition())) &&
                            board.isEnemyOnTheRight(board.getTile(this.getPiecePosition()))) {
                        this.setEnemies();
                        System.out.println("find horiz enemies");
                    }
                }
                // Проверка не зажат ли Воин на поле двумя врагами по вертикали
                if (BoardUtils.isValidTileCoordinate(this.getPiecePosition() - BoardUtils.NEXT_ON_COLUMN) &&
                        BoardUtils.isValidTileCoordinate(this.getPiecePosition() + BoardUtils.NEXT_ON_COLUMN)) {
                    if (board.isEnemyOnTheTop(board.getTile(this.getPiecePosition())) &&
                            board.isEnemyOnTheBottom(board.getTile(this.getPiecePosition()))) {
                        this.setEnemies();
                        System.out.println("find vert enemies");
                    }
                }
            }
            // Проверка не зажата ли фишка между точкой побега и одним врагом
            if(
                    (this.getPiecePosition() == 1 && board.isEnemyOnTheRight(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 9 && board.isEnemyOnTheBottom(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 7 && board.isEnemyOnTheLeft(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 17 && board.isEnemyOnTheBottom(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 63 && board.isEnemyOnTheTop(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 73 && board.isEnemyOnTheRight(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 71 && board.isEnemyOnTheTop(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 79 && board.isEnemyOnTheLeft(board.getTile(this.getPiecePosition())))
            ){
                this.setEnemies();
            }
            // Проверка не зажата ли фишка между пустым троном и одним врагом
            if((!this.getPieceType().isKing())){
                if(!board.getTile(40).isTileOccupied()){
                    if((this.getPiecePosition() == 31 && board.isEnemyOnTheTop(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 39 && board.isEnemyOnTheLeft(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 41 && board.isEnemyOnTheRight(board.getTile(this.getPiecePosition()))) ||
                            (this.getPiecePosition() == 49 && board.isEnemyOnTheBottom(board.getTile(this.getPiecePosition()))) ){
                        this.setEnemies();
                    }
                }
            }
        }
    }




}