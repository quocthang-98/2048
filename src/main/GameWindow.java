package main;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    
    public GameWindow(GamePanel gamePanel) {

        this.setTitle("2048");
        this.setResizable(false);
        this.add(gamePanel);

        this.pack();

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
