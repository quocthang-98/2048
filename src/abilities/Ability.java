package abilities;

import tiles.Gameboard;
import tiles.Tile;

public abstract class Ability {

    public Gameboard gb;
    public Tile[][] board;
    
    public long ABILITY_COOLDOWN;
    public long timeCount;
    public boolean isReady;

    public Ability (Gameboard gb, Tile[][] board, long cooldown) {

        this.gb = gb;
        this.board = board;
        this.ABILITY_COOLDOWN = cooldown;
        this.timeCount = cooldown;
        isReady = false;
    }

    public abstract void castAbility ();
}
