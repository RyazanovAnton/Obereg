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

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

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

