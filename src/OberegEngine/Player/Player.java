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

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> playerLegals,
           final Collection<Move> opponentLegals) {
        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
        this.legalMoves = Collections.unmodifiableCollection(playerLegals);
    }

    public King getPlayerKing(){
        return this.playerKing;
    }
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Shouldn't reach here! not a valid board!!!");
    }

    public boolean isLegalMove(final Move move){
        return this.legalMoves.contains(move);
    }


    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }
    //TODO!!!
    //Не можешь сделать такой ход, что король не попадет под контроль
    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }
    //Чтобы вычислить сможет ли Король сбежать, рассмотрим все допустимые ходы каждого из игороков
    public MoveTransition makeMove(final Move move){
        if(!isLegalMove(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transmitonBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transmitonBoard.currentPlayer().
                        getOpponent().getPlayerKing().getPiecePosition(),
                transmitonBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transmitonBoard, move, MoveStatus.DONE);
    }

    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }



    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

}


