package OberegEngine;

import OberegEngine.Player.Player;
import OberegEngine.Player.SlavPlayer;
import OberegEngine.Player.VikingPlayer;

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

    public abstract boolean isSlavs();
    public abstract boolean isVikings();

    public abstract Player choosePlayer(final SlavPlayer slavPlayer, final VikingPlayer vikingPlayer);
}
