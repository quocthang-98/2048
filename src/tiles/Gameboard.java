package tiles;

import static constants.AnimationConstants.MoveConstants.ATTACK;
import static constants.AnimationConstants.MoveConstants.BEING_HIT;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.Font;

import abilities.*;

import data.DataManager;
import main.GamePanel;
import main.GameState;
import threads.EnemyThread;
import inputs.Direction;

public class Gameboard {
    
    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final int startingTilesCount = 2;
    private Tile[][] board;
    private AbilityUndoStack st;

    private int boardPosX;
    private int boardPosY;

    private GamePanel gPanel;

    private BufferedImage gameBoardImage;
    private BufferedImage hudImage;
    private BufferedImage finalBoardImage;

    private BufferedImage ability1Image_on;
    private BufferedImage ability1Image_off;
    private BufferedImage ability2Image_on;
    private BufferedImage ability2Image_off;
    private BufferedImage ability3Image_on;
    private BufferedImage ability3Image_off;

    private BufferedImage attack_btn;
    private BufferedImage attack_btn_off;
    private BufferedImage atk_tile_icon;
    private BufferedImage heal_tile_icon;

    // define some size values for the interface
    public static final int SPACING = 10;
    public static final int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Tile.TILE_WIDTH;
    public static final int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Tile.TILE_HEIGHT;

    public static final int HUD_WIDTH = GamePanel.WIDTH / 5;
    public static final int HUD_HEIGHT = GamePanel.HEIGHT;
    public static final int SCORE_HUD_WIDTH = HUD_WIDTH;
    public static final int SCORE_HUD_HEIGHT = HUD_HEIGHT / 7;
    public static final int SCORE_X = SCORE_HUD_WIDTH / 7;
    public static final int SCORE_Y = SCORE_HUD_HEIGHT / 2;
    public static final int HSCORE_X = SCORE_X;
    public static final int HSCORE_Y = SCORE_Y + SCORE_Y / 2;
    
    public static Color boardBGColor = new Color (0xbdba8f);
    public static Color slotBGColor = new Color(0xccc897); 
    public static Color cornerSlotBGColor = new Color(208, 209, 192, 100);
    public static Color hudColor = new Color(0xe0d5ab);
    public static Color scoreHUDColor = new Color(0xfcf3cf);
    public static Color outlineColor = new Color (0x0c0c0c);
    public static Color abilitiesBoxColor = new Color (0xc1b894);

    public final int ABILITIES_ICON_SIZE = 80;
    public final int ATTACK_ICON_SIZE = 196;
    private final Font COOLDOWN_FONT = new Font("Helvetica Neue", Font.BOLD, 32);
    private int ab1IconPosX = HUD_WIDTH / 3;
    private int ab1IconPosY = HUD_HEIGHT / 3;
    private int ab2IconPosX = ab1IconPosX;
    private int ab2IconPosY = ab1IconPosY + ABILITIES_ICON_SIZE + SPACING;
    private int ab3IconPosX = ab2IconPosX;
    private int ab3IconPosY = ab2IconPosY + ABILITIES_ICON_SIZE + SPACING;

    // MANAGE THE COOLDOWN OF EACH ABILITY
    private PlayerBasicAttack playerAttack;

    private final int ABILITIES_COUNT = 3;
    private PlayerAbility1 player_ab1;
    private PlayerAbility2 player_ab2;
    private PlayerAbility3 player_ab3;

    public int abBoxPosX = ab1IconPosX - SPACING;
    public int abBoxPosY = ab1IconPosY - SPACING;
    public int abBoxWidth = ab1IconPosX + SPACING * 3;
    public int abBoxHeight = SPACING * (ABILITIES_COUNT + 1) + ABILITIES_ICON_SIZE * ABILITIES_COUNT;
    
    public int atkIconX = GamePanel.WIDTH - GamePanel.WIDTH / 4;
    public int atkIconY = Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 6;

    private int MAX_COMBINATION = 25;
    private int combinationCount;
    private boolean attackIsReady = false;

    public final int AB1_COOLDOWN = 15;
    public final int AB2_COOLDOWN = 10;
    public final int AB3_COOLDOWN = 15;

    // SCORE & HIGHEST SCORE
    private int score = 0;
    private int highScore = 0;

    public final int ULTIMATE_VALUE = 2048;

    public static boolean won = false;
    public static boolean lost = false;
    public static boolean found2048Tile = false;

    private DataManager dataManager; // for saving & loading high score

    public Gameboard (int x, int y, GamePanel gp) {

        dataManager = new DataManager(this);

        this.boardPosX = x;
        this.boardPosY = y;
        this.gPanel = gp;

        board = new Tile[ROWS][COLS];
        st = new AbilityUndoStack(board);
        playerAttack = new PlayerBasicAttack(this, board, 0, 1);
        player_ab1 = new PlayerAbility1(this, board, AB1_COOLDOWN, 1);
        player_ab2 = new PlayerAbility2(this, board, AB2_COOLDOWN, 1);
        player_ab3 = new PlayerAbility3(this, board, AB3_COOLDOWN, 1, st);

        combinationCount = 0;

        gameBoardImage = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        hudImage = new BufferedImage(HUD_WIDTH, HUD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        finalBoardImage = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        try {
            ability1Image_on = ImageIO.read(new FileInputStream("resources/img/ab1_on.png"));
            ability1Image_off = ImageIO.read(new FileInputStream("resources/img/ab1_off.png"));
            ability2Image_on = ImageIO.read(new FileInputStream("resources/img/ab2_on.png"));
            ability2Image_off = ImageIO.read(new FileInputStream("resources/img/ab2_off.png"));
            ability3Image_on = ImageIO.read(new FileInputStream("resources/img/ab3_on.png"));
            ability3Image_off = ImageIO.read(new FileInputStream("resources/img/ab3_off.png"));
            attack_btn = ImageIO.read(new FileInputStream("resources/img/atk_button2.png"));
            attack_btn_off = ImageIO.read(new FileInputStream("resources/img/atk_button2_off.png"));
            atk_tile_icon = ImageIO.read(new FileInputStream("resources/img/atk_tile.png"));
            heal_tile_icon = ImageIO.read(new FileInputStream("resources/img/heal_tile.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        dataManager.loadHighScore();
        drawGameboard();
        start();
    }

    public GamePanel getGamePanel() {
        return this.gPanel;
    }
   
    public void updateBoardState() {
        st.addState(board);
    }

    public void clearBoardStateStack() {
        st.clearAll();
    }

    // GAME DATA HANDLING
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getMAX_COMBINATION() {
        return MAX_COMBINATION;
    }

    public void setMAX_COMBINATION(int mAX_COMBINATION) {
        MAX_COMBINATION = mAX_COMBINATION;
    }

    public int getCombinationCount() {
        return combinationCount;
    }

    public void setCombinationCount(int combinationCount) {
        this.combinationCount = combinationCount;
    }

    public boolean attackIsReadyCheck() {
        return attackIsReady;
    }

    public void setAttackIsReady(boolean attackIsReady) {
        this.attackIsReady = attackIsReady;
    }

    public void updateCombinationCount(int value) {
        if (combinationCount + value > MAX_COMBINATION) return;
        
        combinationCount += value;
    }

    public void increaseMaxCombination (int value) {
        MAX_COMBINATION += value;
    }

    // Ability objects
    public PlayerAbility1 getAbility1() {
        return player_ab1;
    }

    public PlayerAbility2 getAbility2() {
        return player_ab2;
    }

    public PlayerAbility3 getAbility3() {
        return player_ab3;
    }
    public PlayerBasicAttack getPlayerBasicAttack() {
        return playerAttack;
    }

    public boolean abilityUnlocked(Ability a) {
        if (gPanel.getLevel() >= a.levelRequired)
            return true;
        else
            return false;
    }

    
    public void unlockAbility (Ability ab, int key) {

        switch (key) {
            case 1:
                player_ab1.timeCount = 3;
                player_ab1.isReady = false;
                break;
            case 2:
                player_ab2.timeCount = 3;
                player_ab2.isReady = false;
                break;
            case 3:
                player_ab3.timeCount = 3;
                player_ab3.isReady = false;
                break;
            default:
                break;
        }
    }


    // GAME MECHANIC
    // spawn 2 random tiles when starting a new game
    public void start() {
        for (int i = 0; i < startingTilesCount; i++) {
            spawnRandomTile();
        }

        updateBoardState();
    }

    // spawn a randomly (90% of 2 and 10% of 4)
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
                Tile newRandom = new Tile(value, row, col, getTileDrawX(col), getTileDrawY(row), this);
                board[row][col] = newRandom;
                break;
            }
        }
    }

    public int getTileDrawX(int col) {
        return SPACING + (SPACING + Tile.TILE_WIDTH) * col;
    }

    public int getTileDrawY(int row) {
        return SPACING + (SPACING + Tile.TILE_HEIGHT) * row;
    }

    public void drawGameboard() {
        
        Graphics2D g = (Graphics2D) gameBoardImage.getGraphics();
        // draw the background for the board (let it match the panel's color)
        g.setColor(GamePanel.bgColor);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        // draw the board's background
        g.setColor(boardBGColor);
        g.fillRoundRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT, 20, 20);

        // draw the board's slots
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (checkAttackingTile(i, j)) {
                    g.setColor(cornerSlotBGColor);
                    int drawX = SPACING + (SPACING + Tile.TILE_WIDTH) * j;
                    int drawY = SPACING + (SPACING + Tile.TILE_HEIGHT) * i;
                    g.drawImage(atk_tile_icon, drawX, drawY, null);
                    g.fillRoundRect(drawX, drawY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
                }
                else if (checkHealingTile(i, j)) {
                    g.setColor(cornerSlotBGColor);
                    int drawX = SPACING + (SPACING + Tile.TILE_WIDTH) * j;
                    int drawY = SPACING + (SPACING + Tile.TILE_HEIGHT) * i;
                    g.drawImage(heal_tile_icon, drawX, drawY, null);
                    g.fillRoundRect(drawX, drawY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
                }
                else {
                    g.setColor(slotBGColor);
                    int drawX = SPACING + (SPACING + Tile.TILE_WIDTH) * j;
                    int drawY = SPACING + (SPACING + Tile.TILE_HEIGHT) * i;
                    g.fillRoundRect(drawX, drawY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
                }
            }
        }
        
        g.dispose();
    }

    // update all coordinates of all available number tiles
    public void update() {

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null) {
                    continue;
                }
                current.updateImage();
                current.updateAnimation();                               // decide the animation for the number tile
                resetPosition(current, row, col);

                if (current.getValue() == ULTIMATE_VALUE) {
                    gPanel.pauseAllAbilitiesTimer();
                    Tile.TILE_SPEED = Tile.DEFAULT_TILE_SPEED / 20;
                    found2048Tile = true;
                    gPanel.setGameState(GameState.SLOW_MOTION);

                }
            }
        }

        if (boardIsFull()) {
            gPanel.setGameState(GameState.SLOW_MOTION);
            EnemyThread.resetIdleCharge();
        }
        

        if (boardIsEmpty()) {
            gPanel.setGameState(GameState.PLAY);
            this.clearBoardStateStack();
            start();
        }
    }

    public void update2048Attack () {
        if (found2048Tile == false) {
            return;
        }
        gPanel.setGameState(GameState.PLAY);

        gPanel.damageEnemy(GamePanel.ENEMY_STARTING_HP);

        // restart with a new board
        Tile.TILE_SPEED = Tile.DEFAULT_TILE_SPEED;

        this.clearAllTiles();

        gPanel.startAllAbilitiesTimer();
        found2048Tile = false;

        gPanel.setPlayerAnimation(ATTACK);
        gPanel.setEnemyAnimation(BEING_HIT);
    }

    public void updateFullBoardAttack() {

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile current = board[row][col];
                if (current == null) continue;

                current.setVanishingAnimation(true);
            }
        }
        gPanel.setGameState(GameState.PLAY);
        gPanel.damagePlayer(GamePanel.PLAYER_STARTING_HP, true);
        
    }

    public boolean boardIsEmpty() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearAllTiles () {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = null;
            }
        }
    }

    public int sumAllTiles() {
        int totalSum = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == null) {
                    continue;
                }
                totalSum += board[row][col].getValue();
            }
        }
        return totalSum;
    }

    public int sumAllLargeTiles() {
        int totalSum = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == null) {
                    continue;
                }
                if (board[row][col].getValue() >= 8)
                    totalSum += board[row][col].getValue();
            }
        }
        return totalSum;
    }

    public Tile findLargestTile() {
        int maxValue = 2;
        Tile maxTile = null;

        for (int row = 0; row < Gameboard.ROWS; row++) {
            for (int col = 0; col < Gameboard.COLS; col++) {
                Tile current = board[row][col];
                if (current == null) continue;
                if (current.getValue() >= maxValue) {
                    maxTile = board[row][col];
                }            
            }
        }

        return maxTile;
    }

    public static boolean checkAttackingTile(int row, int col) {
        int sumRowCol = row + col;

        switch (sumRowCol) {
            case 0:
            case ((Gameboard.ROWS - 1) * 2):
                return true;
            default:
                return false;
        }
    }

    public static boolean checkHealingTile(int row, int col) {
        int sumRowCol = row + col;

        switch (sumRowCol) {
            case (Gameboard.ROWS - 1):
                if (row != 0 && col != 0) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void updateRemoval () {
        for (int row = 0; row < Gameboard.ROWS; row++) {
            for (int col = 0; col < Gameboard.COLS; col++) {
                Tile current = board[row][col];
                if (current == null) continue;
                if (current.isVanished()) {
                    board[row][col] = null;
                }            
            }
        }
    }


    // calculate the postion of the tile.
    // This is for the animation purpose.
    // (a tile can slide frame-by-frame until it reaches its final destination)
    private void resetPosition(Tile current, int row, int col) {

        if (current == null) return;          // if this is an empty slot, perform no animation (do nothing)

        int x = getTileDrawX(col);                  // get the drawing position (x coordinate)
        int y = getTileDrawY(row);                  // get the drawing position (y coordinate)

        int distX = current.getX() - x;
        int distY = current.getY() - y;

        if (Math.abs(distX) < Tile.TILE_SPEED) {
            current.setX(current.getX() - distX);
        }
        if (Math.abs(distY) < Tile.TILE_SPEED) {
            current.setY(current.getY() - distY);
        }

        if (distX < 0) { // left
            current.setX(current.getX() + Tile.TILE_SPEED);
        }
        if (distY < 0) { // up
            current.setY(current.getY() + Tile.TILE_SPEED);
        }
        if (distX > 0) { // right
            current.setX(current.getX() - Tile.TILE_SPEED);
        }
        if (distY > 0) { // down
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
            canMove = false; // this "else" case is actually unnecessary
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
            
            // if lost here
            if (lost) {
                
            }
        }
    }

    // check if the player has lost the game
    public boolean boardIsFull() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == null)                        // if there is still an empty cell,
                    return false;                                         // the game continues
                if (checkSurrounding(row, col, board[row][col])) {  // if there is still an empty slot next to this number tile,
                    return false;                                         // the game continues
                }
            }
        }

        return true;
    }

    public boolean checkSurrounding (int row, int col, Tile tile) {
        if (row > 0) {  // check the tile above
            Tile check = board[row - 1][col];
            if (check == null) return true;
            if (tile.getValue() == check.getValue()) return true;
        }
        if (row < ROWS - 1) {   // check the tile below
            Tile check = board[row + 1][col];
            if (check == null) return true;
            if (tile.getValue() == check.getValue()) return true;
        }
        if (col > 0) {  // check the tile on the left
            Tile check = board[row][col - 1];
            if (check == null) return true;
            if (tile.getValue() == check.getValue()) return true;
        }
        if (col < COLS - 1) {   // check the tile on the right
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
        if (current == null) {              // if there is no tile here
            return false;                   // don't move
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

                this.combinationCount++;
                gPanel.setEXP(gPanel.getEXP() + 1);

                score += board[newRow][newCol].getValue();
                if (score >= highScore) {
                    highScore = score;
                }
                dataManager.saveHighScore();
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
                if (tile == null)        // if this tile is empty or if it is the maxTile,
                    continue;                               // skip
                tile.drawTile(g2);
            }
        }

        g2.dispose();

        // finally, render the finalBoardImage graphics
        g.drawImage(finalBoardImage, boardPosX, boardPosY, null);

        // render the HUD
        this.renderHUD(); 
        g.drawImage(hudImage, 0, 0, null);

        if (attackIsReady) {
            g.drawImage(attack_btn, atkIconX, atkIconY, null);
        }
        else {
            g.drawImage(attack_btn_off, atkIconX, atkIconY, null);
        }
        g.dispose();
    }

    public void renderHUD () {

        Graphics2D g = (Graphics2D) hudImage.getGraphics();

        // draw the HUD
        g.setColor(hudColor);
        g.fillRect(0, 0, HUD_WIDTH, HUD_HEIGHT);


        g.setColor(scoreHUDColor);
        g.fillRect(0, 0, SCORE_HUD_WIDTH, SCORE_HUD_HEIGHT);

        g.setColor(abilitiesBoxColor);
        g.fillRoundRect(abBoxPosX, abBoxPosY, abBoxWidth, abBoxHeight, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
        
        // write scores
        printScore(g);

        // update & draw abilities graphics

        updateAbilitiesGraphics();
        renderAbilitiesGraphics(g);

        // cooldown clocks
        drawAbility1Cooldown(g);
        drawAbility2Cooldown(g);
        drawAbility3Cooldown(g);

        g.dispose();
    }

    private void printScore(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Helvetica Neue", Font.BOLD,28));
        g.drawString("Score: " + score, SCORE_X, SCORE_Y);
        g.setFont(new Font("Helvetica Neue", Font.BOLD,20));
        g.setColor(Color.GRAY);
        g.drawString("Best: " + highScore, HSCORE_X, HSCORE_Y);
    }

    private void drawAbility1Cooldown (Graphics2D g) {
        if (gPanel.getLevel() >= player_ab1.levelRequired) {
            if (player_ab1.timeCount > 0) {
                g.setColor(Color.WHITE);
                g.setFont(COOLDOWN_FONT);
                int drawX = ab1IconPosX + ((ab1IconPosX)
                                - DrawUtilz.getMessageWidth("" + player_ab1.timeCount, COOLDOWN_FONT, g)) / 2 + ABILITIES_ICON_SIZE / 12;
                int drawY = ab1IconPosY + ABILITIES_ICON_SIZE / 2 + 12;
                g.drawString("" + player_ab1.timeCount, drawX, drawY);
            }
        }
    }

    private void drawAbility2Cooldown (Graphics2D g) {
        if (gPanel.getLevel() >= player_ab2.levelRequired) {
            if (player_ab2.timeCount > 0) {
                g.setColor(Color.WHITE);
                g.setFont(COOLDOWN_FONT);
                int drawX = ab2IconPosX + ((ab2IconPosX)
                                - DrawUtilz.getMessageWidth("" + player_ab2.timeCount, COOLDOWN_FONT, g)) / 2 + ABILITIES_ICON_SIZE / 12;
                int drawY = ab2IconPosY + ABILITIES_ICON_SIZE / 2 + 12;
                g.drawString("" + player_ab2.timeCount, drawX, drawY);
            }
        }
    }

    private void drawAbility3Cooldown (Graphics2D g) {
        if (gPanel.getLevel() >= player_ab3.levelRequired) {
            if (player_ab3.timeCount > 0) {
                g.setColor(Color.WHITE);
                g.setFont(COOLDOWN_FONT);
                int drawX = ab3IconPosX + ((ab3IconPosX)
                                - DrawUtilz.getMessageWidth("" + player_ab3.timeCount, COOLDOWN_FONT, g)) / 2 + ABILITIES_ICON_SIZE / 12;
                int drawY = ab3IconPosY + ABILITIES_ICON_SIZE / 2 + 12;
                g.drawString("" + player_ab3.timeCount, drawX, drawY);
            }
        }
    }

    public void updateAbilitiesGraphics() {
        //
    }

    public void renderAbilitiesGraphics(Graphics2D g) {

        if (gPanel.getLevel() >= player_ab1.levelRequired) {
            if (player_ab1.isReady) {
                g.drawImage(ability1Image_on, ab1IconPosX, ab1IconPosY,  null);
            }
            else {
            g.drawImage(ability1Image_off, ab1IconPosX, ab1IconPosY, null);  

            }
        }

        if (gPanel.getLevel() >= player_ab2.levelRequired) {
            if (player_ab2.isReady) {
                g.drawImage(ability2Image_on, ab2IconPosX, ab2IconPosY,  null);
            }
            else {
                g.drawImage(ability2Image_off, ab2IconPosX, ab2IconPosY,  null);
            }
        }

        if (gPanel.getLevel() >= player_ab3.levelRequired) {
            if (player_ab3.isReady) {
                g.drawImage(ability3Image_on, ab3IconPosX, ab3IconPosY,  null);
            }
            else {
                g.drawImage(ability3Image_off, ab3IconPosX, ab3IconPosY,  null);
            }
        }
    }
}
