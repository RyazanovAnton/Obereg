package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;

public class SlavPlayer extends Player {
    public SlavPlayer(final Board board,
                       final ArrayList<Move> slavStandardLegalMoves){
        super(board, slavStandardLegalMoves);
    }
    @Override
    public ArrayList<Piece> getActivePieces() {
        return this.board.getSlavPieces();
    }
    @Override
    public Alliance getAlliance() {
        return Alliance.SLAVS;
    }
    @Override
    public Player getOpponent() {
        return this.board.vikingPlayer();
    }
}

