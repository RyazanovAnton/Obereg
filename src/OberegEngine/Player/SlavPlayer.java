package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;

import java.util.Collection;

public class SlavPlayer extends Player {
    public SlavPlayer(final Board board,
                       final Collection<Move> slavStandardLegalMoves){
        super(board, slavStandardLegalMoves);
    }
    @Override
    public Collection<Piece> getActivePieces() {
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

