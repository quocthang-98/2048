package abilities;

import tiles.Gameboard;
import tiles.Tile;

public class PlayerAbility2 extends Ability{

    public PlayerAbility2 (Gameboard gb, Tile[][] board, long cd, int levelRequired) {
        super(gb, board, cd, levelRequired);
    }

    @Override
    public void castAbility() {

        for (int row = 0; row < Gameboard.ROWS; row++) {
            for (int col = 0; col < Gameboard.COLS; col++) {
                Tile current = board[row][col];
                if (current == null) {
                    continue;
                }
                if (current.getValue() == 2) {
                    current.setVanishingAnimation(true);
                }           
            }
        }  
    }
}
