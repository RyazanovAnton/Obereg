package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Player.Player;

// Игра с нулевой суммой, чем больше число - тем ближе выигрыш Варягов,
// чем меньше число - тем ближе выигрыш Викингов
public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
