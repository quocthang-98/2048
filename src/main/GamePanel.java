package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import tiles.Gameboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class GamePanel extends JPanel {

    private final int WIDTH = 1024;
    private final int HEIGHT = 720;

    // font + bg color
    public static Font font = new Font("Helvetica Neue", Font.BOLD,28);
    public static Color bgColor = Color.WHITE;

    KeyboardInputs keyboardInputs;

    private Gameboard gBoard;
    private int drawX = WIDTH / 2 - Gameboard.BOARD_WIDTH / 2;
    private int drawY = HEIGHT - Gameboard.BOARD_HEIGHT - 150;

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

    private int targetDisplayTime = 8;

    private final int boardOffset = drawX / 2;
    
    public GamePanel() {

        Dimension panelSize = new Dimension(WIDTH, HEIGHT);

        this.setPreferredSize(panelSize);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        keyboardInputs = new KeyboardInputs(this);
        addKeyListener(keyboardInputs);

        try {
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
    }

    public int getDrawX() {
        return drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public void update() {
        keyboardInputs.eventUpdate(gBoard);
        gBoard.update();
        keyboardInputs.updatePrevKeys();
    }

    public void drawFrame() {
        // background
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(bgColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, boardOffset, HEIGHT);

        arrowUpdate();
        arrowRender(g);

        gBoard.renderBoard(g);

        g.dispose();

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

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

    public void arrowRender (Graphics2D g) {
        if (goingUp) {
            g.drawImage(up_arrow_on, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY - Gameboard.BOARD_HEIGHT / 2 + 120, null);
            g.drawImage(down_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY + Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 2 + 220, null);
            g.drawImage(left_arrow_off, drawX - 60, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
            g.drawImage(right_arrow_off, drawX + Gameboard.BOARD_WIDTH + 30, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
        }
        else if (goingLeft) {
            g.drawImage(up_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY - Gameboard.BOARD_HEIGHT / 2 + 120, null);
            g.drawImage(down_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY + Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 2 + 220, null);
            g.drawImage(left_arrow_on, drawX - 60, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
            g.drawImage(right_arrow_off, drawX + Gameboard.BOARD_WIDTH + 30, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
        }
        else if (goingRight) {
            g.drawImage(up_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY - Gameboard.BOARD_HEIGHT / 2 + 120, null);
            g.drawImage(down_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY + Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 2 + 220, null);
            g.drawImage(left_arrow_off, drawX - 60, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
            g.drawImage(right_arrow_on, drawX + Gameboard.BOARD_WIDTH + 30, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
        }
        else if (goingDown) {
            g.drawImage(up_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY - Gameboard.BOARD_HEIGHT / 2 + 120, null);
            g.drawImage(down_arrow_on, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY + Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 2 + 220, null);
            g.drawImage(left_arrow_off, drawX - 60, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
            g.drawImage(right_arrow_off, drawX + Gameboard.BOARD_WIDTH + 30, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
        }
        else {
            g.drawImage(up_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY - Gameboard.BOARD_HEIGHT / 2 + 120, null);
            g.drawImage(down_arrow_off, drawX + Gameboard.BOARD_WIDTH / 2 - 10, drawY + Gameboard.BOARD_HEIGHT - Gameboard.BOARD_HEIGHT / 2 + 220, null);
            g.drawImage(left_arrow_off, drawX - 60, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
            g.drawImage(right_arrow_off, drawX + Gameboard.BOARD_WIDTH + 30, drawY + Gameboard.BOARD_HEIGHT / 2 - 10, null);
        }
    }
}
