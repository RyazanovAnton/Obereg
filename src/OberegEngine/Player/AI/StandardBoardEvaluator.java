package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Pieces.Piece;
import OberegEngine.Player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.slavPlayer(), depth) -
                scorePlayer(board, board.vikingPlayer(), depth);
    }

    private int scorePlayer(final Board board,
                            final Player player,
                            final int depth) {
        //System.out.println(pieceValue(player)+mobility(player) + captureEnemies(player));
        return pieceValue(player) + mobility(player) + captureEnemies(player);
    }

    private int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private int captureEnemies(Player player){
        int pieceEnemiesScore = 0;
        for(Piece piece : player.getOpponent().getActivePieces()){
            if(piece.getEnemies()){
               pieceEnemiesScore += 5000;
            }
        }
        return pieceEnemiesScore;
    }

    private static int pieceValue(final Player player){
        int pieceValueScore = 0;
        for(final Piece piece : player.getActivePieces()){
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
