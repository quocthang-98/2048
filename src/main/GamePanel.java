package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ExperienceBar.ExperienceBar;
import inputs.KeyboardInputs;
import threads.*;
import tiles.Gameboard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class GamePanel extends JPanel {

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 720;

    // font + bg color
    public static Font font = new Font("Helvetica Neue", Font.BOLD,28);
    public static Color bgColor = Color.WHITE;

    private Dimension panelSize;
    private ExperienceBar experienceBar;

    private KeyboardInputs keyboardInputs;

    private Gameboard gBoard;
    private int drawX = WIDTH / 2 - Gameboard.BOARD_WIDTH / 2;
    //the value of x to set up initial point for drawing the game board
    private int drawY = HEIGHT - Gameboard.BOARD_HEIGHT - 150;
    //the value of y to set up initial point for drawing the game board

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private BufferedImage up_arrow_off;
    private BufferedImage up_arrow_on;
    private BufferedImage down_arrow_off;
    private BufferedImage down_arrow_on;
    private BufferedImage left_arrow_off;
    private BufferedImage left_arrow_on;
    private BufferedImage right_arrow_off;
    private BufferedImage right_arrow_on;

    private boolean goingUp = false;
    private boolean goingLeft = false;
    private boolean goingDown = false;
    private boolean goingRight = false;

    private int displayArrowUpTime = 0;
    private int displayArrowLeftTime = 0;
    private int displayArrowDownTime = 0;
    private int displayArrowRightTime = 0;

    private Ability1Thread ab1Thread;
    private Ability2Thread ab2Thread;

    private int targetDisplayTime = 8;

    public GameState gameState;
    
    public GamePanel() {

        panelSize = new Dimension(WIDTH, HEIGHT); // save the size of panel
        this.setPreferredSize(panelSize);// load the size into this panel
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        // set up the panel to draw on unvisible buffer
        // in order to enable the panel to display more smoothly
        this.setFocusable(true);// allow this panel to receive the button from keyboard

        keyboardInputs = new KeyboardInputs(this);
        addKeyListener(keyboardInputs);// set up the spot to receive the button from keyboard


        ab1Thread = new Ability1Thread(this);
        ab2Thread = new Ability2Thread(this);

        try { // save images of 4 arrows indicating the moving directions
            up_arrow_on = ImageIO.read(new FileInputStream("resources/img/up_arrow_on.png"));
            up_arrow_off = ImageIO.read(new FileInputStream("resources/img/up_arrow_off.png"));
            down_arrow_on = ImageIO.read(new FileInputStream("resources/img/down_arrow_on.png"));
            down_arrow_off = ImageIO.read(new FileInputStream("resources/img/down_arrow_off.png"));
            left_arrow_on = ImageIO.read(new FileInputStream("resources/img/left_arrow_on.png"));
            left_arrow_off = ImageIO.read(new FileInputStream("resources/img/left_arrow_off.png"));
            right_arrow_on = ImageIO.read(new FileInputStream("resources/img/right_arrow_on.png"));
            right_arrow_off = ImageIO.read(new FileInputStream("resources/img/right_arrow_off.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        gBoard = new Gameboard(drawX, drawY);
        gameState = GameState.PLAY;
//        experienceBar = new ExperienceBar(this);

    }

    public Gameboard getGameboard () {
        return gBoard;
    }

    public void setGameState (GameState s) {
        gameState = s;
    }

    public int getDrawX() {
        return drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public Ability1Thread getAb1Thread() {
        return ab1Thread;
    }

    public Ability2Thread getAb2Thread() {
        return ab2Thread;
    }

    public void update() {
        if (gameState == GameState.PLAY) {
            gBoard.update();
        }
        keyboardInputs.eventUpdate(gBoard); // read the input keys
        keyboardInputs.updatePrevKeys();    // prevent the case of holding down keys
    }

    private boolean pauseScreendisplayed = false;     // if the Pause menu displayed, set this to true (avoid overlaying)


    public void drawFrame() {

        // generate a Graphics2D object to work with the background
        Graphics2D g = (Graphics2D) image.getGraphics();

        if (gameState == GameState.PLAY) {
            pauseScreendisplayed = false;

            g.setColor(bgColor);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.drawImage(up_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY - Gameboard.BOARD_HEIGHT / 2 + 120, null);
            g.drawImage(down_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY + Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 2 + 220, null);
            g.drawImage(left_arrow_off, drawX - 60, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
            g.drawImage(right_arrow_off, drawX + Gameboard.BOARD_WIDTH + 30, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);

            // update & render the current state of the arrow
            arrowUpdate();
            arrowRender(g);

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

        // finish drawing the background object
        g.dispose();
//
        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(image, 0, 40, null);
        g2.setColor(Gameboard.scoreHUDColor);
        g2.fillRect(0,0,205,40);
        g2.dispose();

    }

    // these are for the animations of the arrows
    public void setGoingUp (boolean b) {
        goingUp = b;
    }

    public void setGoingLeft (boolean b) {
        goingLeft = b;
    }

    public void setGoingDown (boolean b) {
        goingDown = b;
    }

    public void setGoingRight (boolean b) {
        goingRight = b;
    }

    // count the display time of each arrow key
    public void arrowUpdate () {
        if (goingUp) {
            displayArrowUpTime++;
            if (displayArrowUpTime >= targetDisplayTime) {
                displayArrowUpTime = 0;
                goingUp = false;
            }
        }
        if (goingLeft) {
            displayArrowLeftTime++;
            if (displayArrowLeftTime >= targetDisplayTime) {
                displayArrowLeftTime = 0;
                goingLeft = false;
            }
        }
        if (goingDown) {
            displayArrowDownTime++;
            if (displayArrowDownTime >= targetDisplayTime) {
                displayArrowDownTime = 0;
                goingDown = false;
            }
        }
        if (goingRight) {
            displayArrowRightTime++;
            if (displayArrowRightTime >= targetDisplayTime) {
                displayArrowRightTime = 0;
                goingRight = false;
            }
        }
    }

    // render the arrows
    public void arrowRender (Graphics2D g) {
        if (goingUp) {
            g.drawImage(up_arrow_on, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY - Gameboard.BOARD_HEIGHT / 2 + 120, null);
        }
        else if (goingLeft) {
            g.drawImage(left_arrow_on, drawX - 60, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
        }
        else if (goingRight) {
            g.drawImage(right_arrow_on, drawX + Gameboard.BOARD_WIDTH + 30, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
        }
        else if (goingDown) {
            g.drawImage(down_arrow_on, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY + Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 2 + 220, null);
        }
    }
}
