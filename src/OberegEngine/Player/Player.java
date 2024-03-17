package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;

import java.util.Collection;
import java.util.Collections;

public abstract class Player {
    protected final Board board;
    protected final Collection<Move> legalMoves;

    Player(final Board board,
           final Collection<Move> playerLegals) {
        this.board = board;
        this.legalMoves = Collections.unmodifiableCollection(playerLegals);
    }
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    public boolean isLegalMove(final Move move){
        return this.legalMoves.contains(move);
    }
    public MoveTransition makeMove(final Move move){
        if(!isLegalMove(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transmitonBoard = move.execute();
        return new MoveTransition(transmitonBoard, move, MoveStatus.DONE);
    }
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

}


