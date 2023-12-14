package main;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import tiles.Gameboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private final int WIDTH = 1024;
    private final int HEIGHT = 720;

    // font + bg color
    public static Font font = new Font("Helvetica Neue", Font.BOLD,28);
    public static Color bgColor = Color.WHITE;

    KeyboardInputs keyboardInputs;

    private Gameboard gBoard;
    private int drawX = WIDTH / 2 - Gameboard.BOARD_WIDTH / 2;
    private int drawY = HEIGHT - Gameboard.BOARD_HEIGHT - 100;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    
    public GamePanel() {

        Dimension panelSize = new Dimension(WIDTH, HEIGHT);

        this.setPreferredSize(panelSize);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        keyboardInputs = new KeyboardInputs();
        addKeyListener(keyboardInputs);

        gBoard = new Gameboard(drawX, drawY);
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
        gBoard.renderBoard(g);
        g.dispose();

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }
}
