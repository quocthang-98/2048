package abilities;

import tiles.Gameboard;
import tiles.Tile;

public class PlayerAbility3 extends Ability{

    private AbilityUndoStack st;
    
    public PlayerAbility3 (Gameboard gb, Tile[][] board, long cd, int levelRequired, AbilityUndoStack st) {
        super(gb, board, cd, levelRequired);
        this.st = st;
    }

    @Override
    public void castAbility() {

        if (st.getStack().isEmpty()) {
            return;
        }

        BoardState state = new BoardState(board);

        while (!st.getStack().isEmpty()) {
            state = st.getStack().pop();
        }

        for (int i = 0; i < Gameboard.ROWS; i++) {
            for (int j = 0; j < Gameboard.COLS; j++) {
                if (state.tileArray[i][j] == 0) {
                    board[i][j] = null;
                }
                else {
                    board[i][j] = new Tile(state.tileArray[i][j], i, j, gb.getTileDrawX(j), gb.getTileDrawY(i), gb);
                }
            }
        }
    }
}
