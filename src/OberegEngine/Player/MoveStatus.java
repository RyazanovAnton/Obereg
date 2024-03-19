package OberegEngine.Player;
// Перечисление возможных ходов
public enum MoveStatus {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    GAME_OVER {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();
}
