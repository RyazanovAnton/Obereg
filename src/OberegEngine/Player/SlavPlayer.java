package OberegEngine.Player;

import OberegEngine.Alliance;
import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.King;
import OberegEngine.Pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SlavPlayer extends Player {
    protected final King playerKing;
    private final boolean isInCheck;
    public SlavPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves){
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.playerKing = establishKing();
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), blackStandardLegalMoves).isEmpty();
    }
        public King getPlayerKing(){
        return this.playerKing;
    }
    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }
   //     TODO!!!
 //       Не можешь сделать такой ход, что король не попадет под контроль
    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Shouldn't reach here! not a valid board!!!");
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.SLAVS;
    }

    @Override
    public Player getOpponent() {
        return this.board.vikingPlayer();
    }

//    @Override
//    public MoveTransition makeMove(final Move move){
//        if(!isLegalMove(move)){
//            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
//        }
//        final Board transmitonBoard = move.execute();
//        final Collection<Move> kingAttacks = VikingPlayer.calculateAttacksOnTile(transmitonBoard.currentPlayer().
//                        getOpponent().getPlayerKing().getPiecePosition(),
//                transmitonBoard.currentPlayer().getLegalMoves());
//        if(!kingAttacks.isEmpty()){
//            return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_IN_CHECK);
//        }
//        return new MoveTransition(transmitonBoard, move, MoveStatus.DONE);
//    }

}

