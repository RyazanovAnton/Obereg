package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

// У класса 2 наследника SlavPlayer / VikingPlayer
public abstract class Player {
    protected final Board board;
    protected ArrayList<Move> legalMoves;
    Player(final Board board,
           final ArrayList<Move> playerLegals) {
        this.board = board;
        this.legalMoves = playerLegals;
    }
    // Коллекция доступных ходов для игрока
    public ArrayList<Move> getLegalMoves(){
        return this.legalMoves;
    }
    // Проверка доступности действия
    public boolean isLegalMove(final Move move){
        return this.legalMoves.contains(move);
    }
    // Расстановка фигур на доске по результатам хода
    public MoveTransition makeMove(final Move move){
        if(!isLegalMove(move)){
            System.out.println("Player.makeMove Illegal");
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transmitonBoard = move.execute();
        return new MoveTransition(transmitonBoard, move, MoveStatus.DONE);
    }
    public abstract ArrayList<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

}


