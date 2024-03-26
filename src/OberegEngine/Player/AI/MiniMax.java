package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Player.MoveTransition;

import java.util.ArrayList;

public class MiniMax implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    public MiniMax(final int searchDepth){

        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "MinMax";
    }

    @Override
    public Move execute(Board board) {
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + " thinking with depth = " + this.searchDepth);
        ArrayList<Move> legals = new ArrayList<>(board.currentPlayer().getLegalMoves());
        Board tmpBoard = new Board(board);
        for (int i = 0; i<legals.size(); ++i){
            board.updateBoard(tmpBoard);
            Move move = legals.get(i);
            board.resetEnem();
//TODO
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            board.updateBoard(moveTransition.getTransitionBoard());
            if(moveTransition.getMoveStatus().isDone()){
                currentValue = board.currentPlayer().getAlliance().isSlavs() ?
                        min(moveTransition.getTransitionBoard(), this.searchDepth-1) :
                        max(moveTransition.getTransitionBoard(), this.searchDepth-1);
                if(board.currentPlayer().getOpponent().getAlliance().isSlavs() && currentValue > highestSeenValue){
                    highestSeenValue = currentValue;
                    bestMove = move;
                }else  if (board.currentPlayer().getOpponent().getAlliance().isVikings() && currentValue < lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        board.updateBoard(tmpBoard);
        System.out.println("AI move: " + bestMove.toString());
        System.out.println(highestSeenValue);
        //System.out.println(lowestSeenValue);
        return bestMove;
    }

    public int min(final Board board, final int depth){
        board.resetEnem();
        if(depth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board,  depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.currentPlayer().getLegalMoves()){
            board.resetEnem();
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBoard(), depth -1 );
                if(currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }
    public int max(final Board board, final int depth){
        board.resetEnem();
        if(depth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board,  depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.currentPlayer().getLegalMoves()){
            board.resetEnem();
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBoard(), depth -1 );
                if(currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    private static boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isKingCaptured() ||
                board.currentPlayer().isKingEscaped();
    }
}
