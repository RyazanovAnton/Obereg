package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Tile;
import OberegEngine.Pieces.King;
import OberegEngine.Pieces.Piece;
import OberegEngine.Pieces.Warrior;
import OberegEngine.Player.Player;
import OberegEngine.Player.SlavPlayer;



public final class StandardBoardEvaluator implements BoardEvaluator {
    private final int MOBILITY_BONUS = 10;
@Override
public int evaluate(final Board board, final int depth) {

    int totalMoveScore = 0;
    totalMoveScore = pieceValue(board) + mobility(board) * MOBILITY_BONUS + kingCaptured(board) + kingEscaped(board);
    return totalMoveScore;
}
public int pieceValue(Board board){
    int pieceValueScore = 0;
    for(int i=0; i< BoardUtils.NUM_TILES; ++i){
        if(board.getTile(i).isTileOccupied()){
            if(board.getTile(i).getPiece() instanceof King){
                pieceValueScore += 5000;
            } else if (board.getTile(i).getPiece() instanceof Warrior){
                if(board.getTile(i).getPiece().getPieceAlliance().isSlavs()){
                    pieceValueScore += 1000;
                } else {
                    pieceValueScore -= 1000;
                }
            }
        }
    }
    return pieceValueScore;
}
    private int mobility(Board board) {
        int mobility = 0;
        if(board.currentPlayer().getAlliance().isSlavs()){
            mobility += board.currentPlayer().getLegalMoves().size();
        } else if(board.currentPlayer().getAlliance().isVikings()){
            mobility -= board.currentPlayer().getLegalMoves().size();
        }
        return mobility;
    }
    private int kingCaptured(Board board){
    int kingCapturedValue = 0;
    for(Piece piece : board.currentPlayer().getOpponent().getActivePieces()){
        if(piece.getPieceType().isKing() && piece.getEnemies()){
            kingCapturedValue = -100000;
        }
    }
    return kingCapturedValue;
    }
    private int kingEscaped(Board board){
        int kingEscaped = 0;
        for(Piece piece : board.currentPlayer().getActivePieces()){
            if(piece.getPieceType().isKing() &&
                        (piece.getPiecePosition()==0 ||
                        piece.getPiecePosition()==8 ||
                        piece.getPiecePosition() == 72 ||
                        piece.getPiecePosition() == 80)){
            kingEscaped = 10000;
            }
        }
        return kingEscaped;
    }
}
