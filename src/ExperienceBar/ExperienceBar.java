package ExperienceBar;


import main.GamePanel;
import main.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ExperienceBar extends Thread {
    private static int exp;

    private int newValue;
    private GamePanel gamePanel;
    private Thread temp_thread;
    private Thread temp_Thread2;
    private JProgressBar expBar;
    private JFrame frame;
    private JPanel panel;
    private static int level;
    private int i =0;
    private int[] maxExpLevels = {100, 1000, 10000};
    private Random random = new Random();

    public ExperienceBar(GamePanel gamePanel, GameWindow frame){
        level =1;
        exp = 0;
        this.gamePanel = gamePanel;
         this.frame = frame;
        expBar = new JProgressBar();
           expBar.setStringPainted(true);
           expBar.setLocation(100,0);
            expBar.setPreferredSize(new Dimension(200, 30));
            gamePanel.add(expBar);
            frame.add(gamePanel);
            frame.setVisible(true);
            SwingUtilities.invokeLater(() ->this.start());
    }
    public void run() {

        while (true) {

            gainExp();

            try {
                Thread.sleep(1000/60); // Sleep for approximately 16.67 milliseconds to achieve 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        }
    public int getExp(){
        return exp;
    }
    public static void setExp(int tempExp){
        exp= exp + tempExp;
    }
    public static int getLevel(){
        return level;
    }

    public void gainExp() {
        newValue = getExp();
        if (newValue >= maxExpLevels[level - 1]) {
            if (level == 3) {
                expBar.setValue(maxExpLevels[level - 1]);
                expBar.setString("Max");
            } else {
                levelUp();
            }
        } else {
            expBar.setValue(newValue);
        }
    }

    private void levelUp() {
        level++;
        expBar.setMaximum(maxExpLevels[level - 1]);
        if (newValue > maxExpLevels[level-1]){
            newValue = newValue- maxExpLevels[level-1];
            expBar.setValue(newValue);
        } else {
            expBar.setValue(0);
        }
        expBar.setString("Level"+ level);
    }
}

