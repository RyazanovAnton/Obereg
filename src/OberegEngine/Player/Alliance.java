package OberegEngine.Player;

// Перечисление возможных альянсов
public enum Alliance {
    SLAVS {
        @Override
        public boolean isSlavs() {
            return true;
        }

        @Override
        public boolean isVikings() {
            return false;
        }
        @Override
        public Player choosePlayer(final SlavPlayer slavPlayer, final VikingPlayer vikingPlayer) {
            return slavPlayer;
        }
    },
    VIKINGS {
        @Override
        public boolean isSlavs() {
            return false;
        }

        @Override
        public boolean isVikings() {
            return true;
        }
        @Override
        public Player choosePlayer(final SlavPlayer slavPlayer, final VikingPlayer vikingPlayer){
            return vikingPlayer;
        }

    };
    // Абстрактные методы перечисления
    public abstract boolean isSlavs();
    public abstract boolean isVikings();
    public abstract Player choosePlayer(final SlavPlayer slavPlayer, final VikingPlayer vikingPlayer);
}
