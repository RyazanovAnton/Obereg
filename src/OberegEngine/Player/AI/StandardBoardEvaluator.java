package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Pieces.Piece;
import OberegEngine.Player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
    private static final int DEPTH_BONUS = 100;

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.slavPlayer(), depth) -
                scorePlayer(board, board.vikingPlayer(), depth);
    }

    private int scorePlayer(final Board board,
                            final Player player,
                            final int depth) {
        return pieceValue(player) + mobility(player);
    }


    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(final Player player){
        int pieceValueScore = 0;
        for(final Piece piece : player.getActivePieces()){
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
