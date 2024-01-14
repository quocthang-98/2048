package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

import constants.ExpConstants;
import constants.AnimationConstants.MoveConstants;

import entities.Enemy;
import entities.Player;
import inputs.KeyboardInputs;
import threads.*;
import tiles.Gameboard;
import threads.GameThread;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static constants.AnimationConstants.MoveConstants.*;


public class GamePanel extends JPanel {

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 720;

    // font + bg color
    public static Font font = new Font("Helvetica Neue", Font.BOLD, 28);
    public static Font barFont = new Font("Arial", Font.BOLD, 18);
    public static Font combiningBarFont = new Font("Helvetica Neue", Font.BOLD, 16);

    public static Color bgColor = Color.WHITE;

    public Dimension panelSize;

    KeyboardInputs keyboardInputs;

    private Gameboard gBoard;
    private int drawX = WIDTH / 2 - Gameboard.BOARD_WIDTH / 2;
    private int drawY = HEIGHT - Gameboard.BOARD_HEIGHT - HEIGHT / 8;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private Ability1Thread ab1Thread;
    private Ability2Thread ab2Thread;
    private Ability3Thread ab3Thread;

    private Player player;
    private Enemy enemy;
    public static final int PLAYER_STARTING_HP = 10000;
    public static final int ENEMY_STARTING_HP = 8000;
    private int playerCurrentHP;
    private int enemyCurrentHP;
    private final int hpDropRate = 100;

    private JProgressBar expBar;
    private JProgressBar playerHealthBar;
    private JProgressBar enemyHealthBar;
    private JProgressBar combiningBar;

    private int level;
    private int exp;

    private int difficulty = 10;

    // Player animation
    private BufferedImage playerSprite;
    private BufferedImage enemySprite;

    private BufferedImage[][] playerAnimations;
    private BufferedImage[][] enemyAnimations;
    private final int playerSpriteRows = 6;
    private final int playerSpriteCols = 6;
    private final int playerIconWidth = 128;
    private final int playerIconHeight = 128;
    private final int enemySpriteRows = 6;
    private final int enemySpriteCols = 6;
    private final int enemyIconWidth = 128;
    private final int enemyIconHeight = 128;

    private int playerState = IDLE;
    private int enemyState = IDLE;

    private int animationTick = 0;
    private int animationIndex = 0;
    private int nextIndexSpeed = 6; // increase this value for faster animations
    private int animationSpeed = GameThread.getFPS() / nextIndexSpeed;

    private static final int SLOW_MOTION_DURATION = 96;
    private int slowMotionTimeCount = 0;

    public GameState gameState;
    
    public GamePanel() {

        panelSize = new Dimension(WIDTH, HEIGHT);
        
        this.setLayout(null);
        this.setPreferredSize(panelSize);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        keyboardInputs = new KeyboardInputs(this);
        addKeyListener(keyboardInputs);

        level = 1;
        exp = 0;

        gBoard = new Gameboard(drawX, drawY, this);

        ab1Thread = new Ability1Thread(this);
        ab2Thread = new Ability2Thread(this);
        ab3Thread = new Ability3Thread(this);

        player = new Player(PLAYER_STARTING_HP);
        enemy = new Enemy(ENEMY_STARTING_HP, difficulty);
        playerCurrentHP = PLAYER_STARTING_HP;
        enemyCurrentHP = ENEMY_STARTING_HP;

        this.loadBars();

        this.spriteImport();
        this.loadAnimation();

        this.gameState = GameState.PLAY;

    }

    public Gameboard getGameboard () {
        return gBoard;
    }

    public void setGameState (GameState s) {
        this.gameState = s;
    }

    public void setPlayerState (int s) {
        playerState = s;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public int getDifficultyLevel() {
        return difficulty;
    }

    public int getDrawX() {
        return drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public Player getPlayerObject() {
        return player;
    }

    public Enemy getEnemyObject() {
        return enemy;
    }

    public KeyboardInputs getKeyboardInputs () {
        return keyboardInputs;
    }

    public Ability1Thread getAb1Thread() {
        return ab1Thread;
    }

    public Ability2Thread getAb2Thread() {
        return ab2Thread;
    }

    public Ability3Thread getAb3Thread() {
        return ab3Thread;
    }

    public int getEXP() {
        return exp;
    }

    public void setEXP(int exp) {
        this.exp = exp;

        if (this.exp == ExpConstants.Constants.getRequiredEXP(level)) {
            this.exp = 0;
            level++;
            levelUpdate();
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void startAllAbilitiesTimer() {
        this.ab1Thread.startTimer();
        this.ab2Thread.startTimer();
        this.ab3Thread.startTimer();
    }

    public void pauseAllAbilitiesTimer() {
        this.ab1Thread.pauseTimer();
        this.ab2Thread.pauseTimer();
        this.ab3Thread.pauseTimer();
    }

    public void stopAllAbilitiesTimer() {
        this.ab1Thread.stopTimer();
        this.ab2Thread.stopTimer();
        this.ab3Thread.stopTimer();
    }

    private void loadBars() {

        expBar = new JProgressBar();
        expBar.setStringPainted(true);
        expBar.setFont(barFont);
        expBar.setBackground(Color.WHITE);
        expBar.setForeground(Color.CYAN);
        expBar.setMaximum(ExpConstants.Constants.getRequiredEXP(level));
        expBar.setValue(0);
        expBar.setBounds(gBoard.abBoxPosX / 2 - Gameboard.SPACING + 18, gBoard.abBoxPosY - gBoard.abBoxPosY / 8,
                            140, 20);

        playerHealthBar = new JProgressBar();
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setFont(barFont);
        playerHealthBar.setBackground(new Color(0, 0, 0, 220));
        playerHealthBar.setForeground(Color.cyan);
        playerHealthBar.setMaximum(PLAYER_STARTING_HP);
        playerHealthBar.setValue(PLAYER_STARTING_HP);
        playerHealthBar.setBounds(drawX, drawY - drawY / 10, Gameboard.BOARD_WIDTH / 2, 20);

        enemyHealthBar = new JProgressBar();
        enemyHealthBar.setStringPainted(true);
        enemyHealthBar.setFont(barFont);
        enemyHealthBar.setBackground(new Color(0, 0, 0, 220));
        enemyHealthBar.setForeground(Color.red);
        enemyHealthBar.setMaximum(ENEMY_STARTING_HP);
        enemyHealthBar.setValue(ENEMY_STARTING_HP);
        enemyHealthBar.setBounds(drawX + Gameboard.BOARD_WIDTH / 2, drawY - drawY / 10, Gameboard.BOARD_WIDTH / 2, 20);

        combiningBar = new JProgressBar();
        combiningBar.setStringPainted(true);
        combiningBar.setFont(combiningBarFont);
        combiningBar.setBackground(Color.BLACK);
        combiningBar.setForeground(Color.DARK_GRAY);
        combiningBar.setMaximum(gBoard.getMAX_COMBINATION());
        combiningBar.setValue(gBoard.getCombinationCount());
        combiningBar.setBounds(gBoard.atkIconX, gBoard.atkIconY + gBoard.ATTACK_ICON_SIZE, 190, 20);

        BasicProgressBarUI ui1 = new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.white;
            }
            protected Color getSelectionForeground() {
                return Color.black;
            }
        };

        BasicProgressBarUI ui2 = new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.white;
            }
            protected Color getSelectionForeground() {
                return Color.white;
            }
        };

        BasicProgressBarUI ui3 = new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.white;
            }
            protected Color getSelectionForeground() {
                if (combiningBar.getForeground() == Color.DARK_GRAY) {
                    return Color.white;
                }
                else return Color.black;
            }
        };

        BasicProgressBarUI ui4 = new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.black;
            }
            protected Color getSelectionForeground() {
                return Color.black;
            }
        };

        playerHealthBar.setUI(ui1);
        enemyHealthBar.setUI(ui2);
        combiningBar.setUI(ui3);
        expBar.setUI(ui4);

        this.add(expBar);
        this.add(playerHealthBar);
        this.add(enemyHealthBar);
        this.add(combiningBar);
    }

    private void spriteImport() {
        try {
            FileInputStream fileInputStream = new FileInputStream("resources/img/sprite_player.png");
            playerSprite = ImageIO.read(fileInputStream);

            fileInputStream = new FileInputStream("resources/img/sprite_enemy.png");
            enemySprite = ImageIO.read(fileInputStream);

            fileInputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }   
    }

    private void loadAnimation() {
        playerAnimations = new BufferedImage[playerSpriteRows][playerSpriteCols];
        enemyAnimations = new BufferedImage[enemySpriteRows][enemySpriteCols];

        for (int i = 0; i < playerAnimations.length; i++) {
            for (int j = 0; j < playerAnimations[i].length; j++) {
                playerAnimations[i][j] = playerSprite.getSubimage(j * playerIconWidth, i * playerIconHeight, playerIconWidth, playerIconHeight);
            }
        }

        for (int i = 0; i < enemyAnimations.length; i++) {
            for (int j = 0; j < enemyAnimations[i].length; j++) {
                enemyAnimations[i][j] = enemySprite.getSubimage(j * enemyIconWidth, i * enemyIconHeight, enemyIconWidth, enemyIconHeight);
            }
        }
    }

    public void update() {
        if (gameState == GameState.PLAY || gameState == GameState.SLOW_MOTION) {
            gBoard.update();
        }

        keyboardInputs.eventUpdate(gBoard); // read the input keys

        gBoard.updateRemoval();
        healthBarsUpdate();
        attackBarUpdate();
        expBarUpdate();
        playerHealthBar.setString("" + player.getHP());
        enemyHealthBar.setString("" + enemy.getHP());
        
        if (gBoard.attackIsReadyCheck()) {
            combiningBar.setForeground(new Color(0xdddbb8));
            combiningBar.setString("READY");
        }
        else {
            combiningBar.setForeground(Color.DARK_GRAY);
            combiningBar.setString(gBoard.getCombinationCount() + "/" + gBoard.getMAX_COMBINATION());
        }

        if (Gameboard.found2048Tile) {
            slowMotionTimeCount++;
            if (slowMotionTimeCount >= SLOW_MOTION_DURATION) {
                gBoard.update2048Attack();
                slowMotionTimeCount = 0;
            }

        }

        if (gBoard.boardIsFull()) {
            slowMotionTimeCount++;
            if (slowMotionTimeCount >= SLOW_MOTION_DURATION) {
                gBoard.updateFullBoardAttack();
                slowMotionTimeCount = 0;
            }
        }

        checkWinLoss();
    }


    public void attackBarUpdate() {
        gBoard.setMAX_COMBINATION(ExpConstants.Constants.getRequiredMaxCombination(level));
        combiningBar.setMaximum(gBoard.getMAX_COMBINATION());
        combiningBar.setValue(gBoard.getCombinationCount());

        if (gBoard.getCombinationCount() >= gBoard.getMAX_COMBINATION()) {
            gBoard.setAttackIsReady(true);
        }
    }

    public void healthBarsUpdate() {
        if (playerCurrentHP > player.getHP()) {
            playerCurrentHP -= hpDropRate;
        }
        else if (playerCurrentHP < player.getHP()) {
            playerCurrentHP = player.getHP();
        }

        if (enemyCurrentHP > enemy.getHP()) {
            enemyCurrentHP -= hpDropRate;
        }
        else if (enemyCurrentHP < enemy.getHP()) {
            enemyCurrentHP = enemy.getHP();
        }

        playerHealthBar.setValue(playerCurrentHP);
        enemyHealthBar.setValue(enemyCurrentHP);
    }

    public void levelUpdate() {

        if (level == gBoard.getAbility1().levelRequired) {
            gBoard.unlockAbility(gBoard.getAbility1(), 1);
        }

        else if (level == gBoard.getAbility2().levelRequired) {
            gBoard.unlockAbility(gBoard.getAbility2(), 2);
        }

        else if (level == gBoard.getAbility3().levelRequired) {
            gBoard.unlockAbility(gBoard.getAbility3(), 3);
        }
    }

    public void expBarUpdate() {

        expBar.setMaximum(ExpConstants.Constants.getRequiredEXP(level));
        expBar.setValue(exp);
        expBar.setString("Level " + level);
    }

    private boolean pauseScreendisplayed = false;     // if the Pause menu displayed, set this to true (avoid overlaying)

    public void drawFrame() {
        // generate a Graphics2D object to work with the background
        Graphics2D g = (Graphics2D) image.getGraphics();
        super.paintComponents(g);

        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(image, 0, 0, null);

        if (gameState == GameState.PLAY || gameState == GameState.SLOW_MOTION) {
            pauseScreendisplayed = false;

            g.setColor(bgColor);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            
            g.drawImage(playerAnimations[playerState][animationIndex], drawX + drawX / 6 - 20, 50, 164, 164, null);
            g.drawImage(enemyAnimations[enemyState][animationIndex], drawX + drawX / 6 + 140, 86,null);

            updateAnimationTick();
            // render the current state of the arrow

            // render the board frame-by-frame
            gBoard.renderBoard(g);
        }

        if (gameState == GameState.PAUSE) {
            // draw pause screen
            if (pauseScreendisplayed == false) {
                g.setColor(new Color(0, 0, 0, 127));
                g.fillRect(0, (int)(drawY * 1.5), WIDTH, HEIGHT / 8);
                pauseScreendisplayed = true;
            }
        }

        if (gameState == GameState.WON) {
            // draw pause screen
            if (pauseScreendisplayed == false) {
                g.setColor(new Color(0, 0, 0, 127));
                g.fillRect(0, (int)(drawY * 1.5), WIDTH, HEIGHT / 8);
                pauseScreendisplayed = true;
            }
        }

        if (gameState == GameState.LOST) {

        }

        // finish drawing
        g.dispose();
        g2.dispose();

    }

    private void updateAnimationTick() {

        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= getSpriteAmount(playerState)) {
                checkPlayerState();
                checkEnemyState();
            }
        }
        
    }

    public void setPlayerAnimation(int code) {
        animationIndex = 0;
        playerState = code;
    }

    public void setEnemyAnimation(int code) {
        animationIndex = 0;
        enemyState = code;
    }

    private void checkPlayerState() {
        if (!MoveConstants.isLoop(playerState)) {
            if (playerState == DYING) {
                playerState = DEAD;
            }
            else {
                playerState = IDLE;
            }
        }
        animationIndex = 0;
    }

    private void checkEnemyState() {
        if (!MoveConstants.isLoop(enemyState)) {
            if (enemyState == DYING) {
                enemyState = DEAD;
            }
            else {
                enemyState = IDLE;
            }
        }
        animationIndex = 0;
    }

    public void damageEnemy(int dmg) {
        this.player.dealDamage(enemy, dmg);

        if (enemy.getHP() < 0) {
            enemy.setHP(0);
        }
        this.checkEnemyAfterHit();

    }

    public void damagePlayer(int dmg, boolean isDirectDamge) {

        boolean canAttack = enemy.tryToAttack();

        if (canAttack || isDirectDamge) {
            this.enemy.dealDamage(player, dmg);
            setEnemyAnimation(ATTACK);
            this.checkPlayerAfterHit();
            if (player.getHP() < 0) {
                player.setHP(0);
            }
        }
    }

    public void healPlayer(int amount) {
        int value = player.getHP() + amount;
        if (value > PLAYER_STARTING_HP) {
            player.setHP(PLAYER_STARTING_HP);
        }
        else player.setHP(player.getHP() + amount);
    }

    public int calculateEnemyLoopDamage() {
        return Math.abs(difficulty * 50 - gBoard.sumAllTiles());
    }

    public void checkEntitiesStatus () {
        if (player.getHP() <= 0) {
            player.setAliveStatus(false);
            Gameboard.lost = true;
            return;
        }
        else {
            setPlayerAnimation(BEING_HIT);
        }

        if (enemy.getHP() <= 0) {
            enemy.setAliveStatus(false);
            Gameboard.won = true;
            return;
        }
    }

    private void checkPlayerAfterHit() {
        if (!player.isAlive()) {
            setPlayerAnimation(DYING);
        }
        else {
            setPlayerAnimation(BEING_HIT);
        }
    }

    private void checkEnemyAfterHit() {
        if (!enemy.isAlive()) {
            setEnemyAnimation(DYING);
        }
        else {
            setEnemyAnimation(BEING_HIT);
        }
    }

    private void checkWinLoss () {
        if (Gameboard.won) {
            gameState = GameState.WON;
        }

        if (Gameboard.lost) {
            gameState = GameState.LOST;
        }
    }
}
