package main;

import javax.swing.JFrame;

public class GameWindow {

    private JFrame jFrame;
    
    public GameWindow(GamePanel gamePanel) {
        jFrame = new JFrame();
        jFrame.setTitle("2048");
        jFrame.setResizable(false);
        jFrame.add(gamePanel);

        jFrame.pack();

        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setVisible(true);
    }
}
