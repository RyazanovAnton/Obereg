package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;

import java.util.Collection;

public class VikingPlayer extends Player{
    public VikingPlayer(final Board board,
                       final Collection<Move> vikingStandardLegalMoves) {
        super(board, vikingStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getVikingPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.VIKINGS;
    }

    @Override
    public Player getOpponent() {
        return this.board.slavPlayer();
    }

}
