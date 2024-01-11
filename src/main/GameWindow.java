package main;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    // create a main frame to insert the main panel into
    public GameWindow(GamePanel gamePanel) {

        setTitle("2048");
        setResizable(false);
        add(gamePanel);
        pack();
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    public void disposeFrame(){
        dispose();
    } // dispose the frame
}
