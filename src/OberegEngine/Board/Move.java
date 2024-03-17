package OberegEngine.Board;

import OberegEngine.Pieces.Piece;

import static OberegEngine.Board.Board.*;

public class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();

    // Конструктор основного хода
    public Move(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    // Конструктор для наследнка NullMove
    private Move(final Board board,
                 final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;

    }
    // Получить доступ к игровому полю
    public Board getBoard(){
        return this.board;
    }
    // Получить порядковый номер координаты тайла, на котором расположена фигура
    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }
    // Получить порядковый номер места назначения фигуры
    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }
    // Получить доступ к передвигаемой фигуре
    public Piece getMovedPiece(){
        return this.movedPiece;
    }
    // Создание новой доски
    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()){
            // Расстановка всех фигур альянса, которые не ходили в этот ход (сравнение по хешу)
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        //перемещение самой фигуры
        builder.setPiece(this.movedPiece.movePiece(this));
        for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            // Расстановка всех фигур противника, если они не были захвачены в этот ход
            if(!piece.getEnemies()){
                builder.setPiece(piece);
            }
        }
        // Переход хода к противнику
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
    // Вспомогательный класс, как наследник основного для определения недействительного хода
    public static final class NullMove extends Move{
        public NullMove() {
            super(null, 93);  // неверное местоназначение
        }
        @Override
        public Board execute(){
            throw new RuntimeException("Can't execute the NULL move");
        }
        @Override
        public int getCurrentCoordinate(){
            return -1; // Возвращаем несуществующую координату для любой фигуры
        }
    }
    // Вспомогательный класс для определения действителен ход или нет
    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("Not instantiable");
        }
        public static Move createMove(final Board board,
                                      final  int currentCoordinate,
                                      final  int destinationCoordinate){
            for (final Move move : board.getAllLegalMoves()){
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}

