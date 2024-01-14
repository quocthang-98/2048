package main;

import javax.swing.JFrame;
import java.awt.Image;
import java.awt.Toolkit;

public class GameWindow extends JFrame {
    
    public GameWindow(GamePanel gamePanel) {

        Image iconImg = Toolkit.getDefaultToolkit().getImage("resources/img/icon.png");

        this.setTitle("2048: ULTIMATE");

        this.setIconImage(iconImg);
        this.setResizable(false);
        this.add(gamePanel);

        this.pack();

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
