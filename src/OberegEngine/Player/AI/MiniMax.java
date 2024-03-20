package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;
import OberegEngine.Pieces.Piece;
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

        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + " thinking with depth = " + this.searchDepth);
        int numMoves = board.currentPlayer().getLegalMoves().size();
        ArrayList<Move> legals = new ArrayList<>(board.currentPlayer().getLegalMoves());
        Board tmpBoard = new Board(board);
        for (int i = 0; i<legals.size(); ++i){
            board.updateBoard(tmpBoard);
            Move move = legals.get(i);
            board.resetEnem();
            //for (Piece piece : board.)
//TODO
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            board.updateBoard(moveTransition.getTransitionBoard());
            if(moveTransition.getMoveStatus().isDone()){
                //currentValue = 1;

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
        System.out.println(bestMove.toString());
        final long executionTime = System.currentTimeMillis() - startTime;
        return bestMove;
    }

    public int min(final Board board, final int depth){
        // depth == 0 -> gameOver
        if(depth == 0 ){
            return this.boardEvaluator.evaluate(board,  depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.currentPlayer().getLegalMoves()){
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
        // depth == 0 -> gameOver
        if(depth == 0){
            return this.boardEvaluator.evaluate(board,  depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.currentPlayer().getLegalMoves()){
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
    // Сценарий, когда условие победы достигнуто,
    // не на полной глубине. Можно отсечь дерево
//    private static boolean isEndGameScenario(final Board board) {
//        return board.checkSlavWinConditions() ||
//                board.checkVikingWinConditions();
//    }
}
