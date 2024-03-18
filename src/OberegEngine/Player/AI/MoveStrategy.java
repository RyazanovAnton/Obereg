package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Board.Move;

public interface MoveStrategy {
    Move execute(Board board);
}
