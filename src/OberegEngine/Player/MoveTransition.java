package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
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
        for (int i = 1; i < BoardUtils.NUM_TILES; i++) {
            if (board.getTile(i).isTileOccupied()) {
                if (board.getTile(i).getPiece().getHorizontalEnemies() ||
                        board.getTile(i).getPiece().getVerticalEnemies()) {
                    Board.Builder builder = new Board.Builder();
                    for (final Piece piece : board.currentPlayer().getActivePieces()) {
                        builder.setPiece(piece);
                    }
                    for (final Piece piece : board.currentPlayer().getOpponent().getActivePieces()) {
                        builder.setPiece(piece);
                    }

                    builder.delPiece(i);
                    builder.setMoveMaker(board.currentPlayer().getAlliance());
                    board = builder.build();

                    break;
                }
            }
        }
        return board;
    }
}

