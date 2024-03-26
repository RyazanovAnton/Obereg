package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;

import java.util.ArrayList;

public class VikingPlayer extends Player{
    public VikingPlayer(final Board board,
                       final ArrayList<Move> vikingStandardLegalMoves) {
        super(board, vikingStandardLegalMoves);
    }
    @Override
    public ArrayList<Piece> getActivePieces() {
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
