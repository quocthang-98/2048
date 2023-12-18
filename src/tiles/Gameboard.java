package tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;
import inputs.Direction;

public class Gameboard {
    
    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final int startingTilesCount = 2;
    private Tile[][] board;

    private BufferedImage gameBoardImage;
    private BufferedImage finalBoardImage;

    private int boardPosX;
    private int boardPosY;
    
    public static final int SPACING = 10;

    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.TILE_WIDTH;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.TILE_HEIGHT;

    public static Color boardBGColor = new Color (0xbdba8f);
    public static Color slotBGColor = new Color(0xd4d1ab);

    private boolean won = false;
    private boolean lost = false;

    public Gameboard (int x, int y) {
        this.boardPosX = x;
        this.boardPosY = y;

        board = new Tile[ROWS][COLS];
        gameBoardImage = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        finalBoardImage = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        drawGameboard();
        start();
    }

    public void start() {
        for (int i = 0; i < startingTilesCount; i++) {
            spawnRandomTile();
        }
    }

    public void spawnRandomTile() {
        Random random = new Random();

        while (true) {
            int position = random.nextInt(ROWS * COLS);
            int row = position / ROWS;
            int col = position % COLS;

            Tile tile = board[row][col];
            if (tile == null) {
                int value = random.nextInt(10);
                if (value < 9) {
                    value = 2;
                }
                else value = 4;
                Tile newRandom = new Tile(value, getTileDrawX(col), getTileDrawY(row));
                board[row][col] = newRandom;
                break;
            }
        }
    }

    public int getTileDrawX(int col) {
        return SPACING + (SPACING + Tile.TILE_WIDTH) * col;
    }

    public int getTileDrawY(int row) {
        return SPACING + (SPACING + Tile.TILE_WIDTH) * row;
    }

    public void drawGameboard() {
        
        Graphics2D g = (Graphics2D) gameBoardImage.getGraphics();
        g.setColor(GamePanel.bgColor);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        // draw the board's background
        g.setColor(boardBGColor);
        g.fillRoundRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT, 20, 20);

        // draw the board's slots
        g.setColor(slotBGColor);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int drawX = SPACING + (SPACING + Tile.TILE_WIDTH) * j;
                int drawY = SPACING + (SPACING + Tile.TILE_HEIGHT) * i;
                g.fillRoundRect(drawX, drawY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
            }
        }
        
        g.dispose();
    }

    public void update() {

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null) {
                    continue;
                }
                current.update();
                resetPosition(current, row, col);
                if (current.getValue() == 2048) {
                    won = true;
                }
            }
        }
    }

    // calculate the postion of the tile.
    // This is for the animation purpose.
    // (a tile can slide frame-by-frame until it reaches its final destination)
    private void resetPosition(Tile current, int row, int col) {

        if (current == null) return;

        int x = getTileDrawX(col);
        int y = getTileDrawY(row);

        int distX = current.getX() - x;
        int distY = current.getY() - y;

        if (Math.abs(distX) < Tile.TILE_SPEED) {
            current.setX(current.getX() - distX);
        }
        if (Math.abs(distY) < Tile.TILE_SPEED) {
            current.setY(current.getY() - distY);
        }

        if (distX < 0) {
            current.setX(current.getX() + Tile.TILE_SPEED);
        }
        if (distY < 0) {
            current.setY(current.getY() + Tile.TILE_SPEED);
        }
        if (distX > 0) {
            current.setX(current.getX() - Tile.TILE_SPEED);
        }
        if (distY > 0) {
            current.setY(current.getY() - Tile.TILE_SPEED);
        }

    }

    // this is used to move the tiles, based on the player's direction instruction
    public void moveTiles(Direction dir) {

        // this define if at least 1 cell has moved 
        // (if no single cell moved, that means the player has lost)
        boolean canMove = false;

        int xDir = 0;
        int yDir = 0;

        if (dir == Direction.LEFT) {
            xDir = -1;  // move one cell to the left

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {                                 // if no cell has moved
                        canMove = move(row, col, xDir, yDir, dir);  // try moving this cell. If this cell can move to somewhere else, then canMove = true
                    }
                    else {                                          // if canMove has been set to true by other moved previous cells
                        move(row, col, xDir, yDir, dir);            // just simply move this cell
                                                                    // this moving mechanic is the same for other 3 directions
                    }
                }
            }
        }

        else if (dir == Direction.RIGHT) {
            xDir = 1;

            for (int row = 0; row < ROWS; row++) {
                for (int col = COLS - 1; col >= 0; col--) {
                    if (!canMove) {
                        canMove = move(row, col, xDir, yDir, dir);
                    }
                    else {
                        move(row, col, xDir, yDir, dir);
                    }
                }
            }
        }

        else if (dir == Direction.UP) {
            yDir = -1;

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, xDir, yDir, dir);
                    }
                    else {
                        move(row, col, xDir, yDir, dir);
                    }
                }
            }
        }

        else if (dir == Direction.DOWN) {
            yDir = 1;

            for (int row = ROWS - 1; row >= 0; row--) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, xDir, yDir, dir);
                    }
                    else {
                        move(row, col, xDir, yDir, dir);
                    }
                }
            }
        }

        else {
            canMove = false; // this case rarely (or no) happens
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null) {
                    continue;
                }
                current.setCombinable(true);           // set the tile with the new positions to be combinable
            }
        }

        if (canMove) {          // if at least 1 tile has moved
            spawnRandomTile();  // spawn another tile
            checkLoss();        // check if the player has lost the game
            
            // if lost here
            if (lost) {
                
            }
        }
    }

    // check if the player has lost the game
    public void checkLoss() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == null)                        // if there is still an empty cell,
                    return;                                         // the game continues
                if (checkSurrounding(row, col, board[row][col])) {  // if there is still an empty slot next to this number tile,
                    return;                                         // the game continues
                }
            }
        }

        lost = true;                                                // if reaches this point, the game ends
    }

    public boolean checkSurrounding (int row, int col, Tile tile) {
        if (row > 0) {
            Tile check = board[row - 1][col];
            if (check == null) return true;
            if (tile.getValue() == check.getValue()) return true;
        }
        if (row < ROWS - 1) {
            Tile check = board[row + 1][col];
            if (check == null) return true;
            if (tile.getValue() == check.getValue()) return true;
        }
        if (col > 0) {
            Tile check = board[row][col - 1];
            if (check == null) return true;
            if (tile.getValue() == check.getValue()) return true;
        }
        if (col < COLS - 1) {
            Tile check = board[row][col + 1];
            if (check == null) return true;
            if (tile.getValue() == check.getValue()) return true;
        }
        return false;
    }

    // move a tile
    public boolean move (int row, int col, int horDir, int verDir, Direction dir) {
        boolean canMove = false;

        Tile current = board[row][col];     // fetch the cell data
        if (current == null) {
            return false;
        }

        boolean move = true;
        int newRow = row;
        int newCol = col;

        while (move) {                                  // start moving
            newCol += horDir;                           // move as directed
            newRow += verDir;                           // move as directed
            if (isOutOfBound(dir, newRow, newCol))      // if the cell try to go out of bound,
                break;                                  // don't let it do it
            if (board[newRow][newCol] == null) {                                            // if the next cell has no other cell (empty)
                board[newRow][newCol] = current;                                            // set that empty slot to be this number
                board[newRow - verDir][newCol - horDir] = null;                             // the previous cell will become empty
                board[newRow][newCol].setNewPos(new Destination(newRow, newCol));           // this cell has new address
                canMove = true;                                                             // return true for the moveDir() method (a tile has moved)
            }
            else if ((board[newRow][newCol].getValue() == current.getValue()) && (board[newRow][newCol].isCombinable())) {
                board[newRow][newCol].setCombinable(false);
                board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2); // double the value
                canMove = true;
                board[newRow - verDir][newCol - horDir] = null;
                board[newRow][newCol].setNewPos(new Destination(newRow, newCol));
                board[newRow][newCol].setCombiningAnimation(true);
            }
            else {
                move = false;
            }
        }

        return canMove;
    }

    // this isOutOfBound receive the NEXT row and col.
    // if the cell want to go outside the board's area, this method will return false.
    public boolean isOutOfBound(Direction dir, int row, int col) {
        if (dir == Direction.LEFT) {
            return col < 0;
        }
        else if (dir == Direction.RIGHT) {
            return col > COLS - 1;
        }
        else if (dir == Direction.DOWN) {
            return row > ROWS - 1;
        }
        else if (dir == Direction.UP) {
            return row < 0;
        }
        return false;
    }

    // render the board frame-by-frame
    public void renderBoard(Graphics2D g) {
        // g2 will work with the finalBoardImage image
        Graphics2D g2 = (Graphics2D) finalBoardImage.getGraphics();    

        // draw the gameBoardImage on the finalBoardImage graphics
        g2.drawImage(gameBoardImage, 0, 0, null);          

        // draw the number tiles
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Tile tile = board[i][j];
                if (tile == null)       // if this tile is empty,
                    continue;           // skip
                tile.drawTile(g2);      // else, draw
            }
        }

        // finally, render the finalBoardImage graphics
        g.drawImage(finalBoardImage, boardPosX, boardPosY, null);

        g.dispose();
        g2.dispose();
    }
}
