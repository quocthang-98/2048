package main;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    
    public GameWindow(GamePanel gamePanel) {

        setTitle("2048");
        setResizable(false);
        add(gamePanel);

        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void disposeFrame(){
        dispose();
    } // dispose the frame
}
