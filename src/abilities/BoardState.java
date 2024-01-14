package abilities;

import tiles.Gameboard;
import tiles.Tile;

public class BoardState {

    public int[][] tileArray;
    private Tile[][] board;
    
    public BoardState (Tile[][] board) {
        this.board = board;
        int boardRowCount = Gameboard.ROWS;
        int boardColCount = Gameboard.COLS;
        tileArray = new int[boardRowCount][boardColCount];

        for (int i = 0; i < Gameboard.ROWS; i++) {
            for (int j = 0; j < Gameboard.COLS; j++) {
                if (board[i][j] == null) {
                    tileArray[i][j] = 0;
                }
                else {
                    tileArray[i][j] = board[i][j].getValue();
                }
            }
        }
    }

    public BoardState duplicateLatestState() {
        BoardState b = new BoardState(board);
        
        for (int i = 0; i < Gameboard.ROWS; i++) {
            for (int j = 0; j < Gameboard.COLS; j++) {
                b.tileArray[i][j] = this.tileArray[i][j];
            }
        }

        return b;
    }
}
