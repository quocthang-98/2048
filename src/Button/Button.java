package Button;

import main.Game;
import main.GamePanel;
import main.GameThread;
import main.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import static java.lang.System.exit;


public class Button extends JFrame implements ActionListener {
    private int BUTTON_WIDTH = 40;
    private int BUTTON_HEIGHT = 40;
    private JButton tryAgainButton;
    private JButton exitButton;


    public Button() {

        tryAgainButton = new JButton("Try Again");
        tryAgainButton.setBounds(50,50,200,30);
        tryAgainButton.setLocation(0,0);
        tryAgainButton.addActionListener(this);
        add(tryAgainButton);
        exitButton = new JButton("Exit");
        exitButton.setBounds(100,100,200,30);
        exitButton.setLocation(200,40);
        exitButton.addActionListener(this);
        add(exitButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(210,70);
        setLayout(null);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == tryAgainButton){
            Game.mainFrame.disposeFrame();
            dispose();
            Game.setInstanceNull();
            Game.getInstance();
        } else {
            Game.mainFrame.disposeFrame();
            dispose();
        }
    }




}
