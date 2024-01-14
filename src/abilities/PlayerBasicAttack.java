package abilities;

import tiles.Gameboard;
import tiles.Tile;

public class PlayerBasicAttack extends Ability {

    public PlayerBasicAttack (Gameboard gb, Tile[][] board, long cd, int levelRequired) {
        super(gb, board, cd, levelRequired);
    }

    @Override
    public void castAbility() {

        int damage = 0;
        int heal = 0;
       
        for (int row = 0; row < Gameboard.ROWS; row++) {
            for (int col = 0; col < Gameboard.COLS; col++) {
                Tile current = board[row][col];
                if (current == null) continue;
                if (Gameboard.checkAttackingTile(row, col)) {
                    damage += current.getValue();
                    current.setVanishingAnimation(true);
                }
                if (Gameboard.checkHealingTile(row, col)) {
                    heal += current.getValue();
                    current.setVanishingAnimation(true);
                }     
            }
        }

        gb.getGamePanel().damageEnemy(damage * 8);
        gb.getGamePanel().healPlayer(heal * 100);
    }
}
