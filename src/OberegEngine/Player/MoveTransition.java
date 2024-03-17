package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;

public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;
    public MoveTransition(final Board transitiontBoard,
                          final Move move,
                          final MoveStatus moveStatus) {
        this.transitionBoard = transitiontBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
    // Получить информацию о статусе хода
    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
    // Сделать переход хода
    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
    // Метод для удаления фигур противника, которые были захвачены на текущем ходу.
    // Если игрок в текущем ходу сам встал под захват своей фигуры, то она не удаляется
    public Board deleteCapturedEnemies(Board board) {
        for(Piece piece : board.currentPlayer().getActivePieces()){
            if (piece.getEnemies()) {
                Board.Builder builder = new Board.Builder();
                for (final Piece piece2 : board.currentPlayer().getActivePieces()) {
                    builder.setPiece(piece2);
                }
                for (final Piece piece2 : board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece2);
                }
                builder.delPiece(piece.getPiecePosition());
                builder.setMoveMaker(board.currentPlayer().getAlliance());
                board = builder.build();
                break;
            }
        }
        return board;
    }
}

