package main;

import ExperienceBar.ExperienceBar;
import threads.GameThread;


public class Game {
    private static Game instance;
    private static GameWindow mainFrame;
    private static GamePanel gamePanel;


    private Game() {
        gamePanel = new GamePanel();
        mainFrame = new GameWindow(gamePanel);
        new GameThread(gamePanel, mainFrame);
        gamePanel.requestFocus();// ensure game panel to receive the signal of button from the keyboard immediately
    }
    public static Game getInstance(){
        if (instance == null){
            instance = new Game();
        }
        return instance;
    }
    public static GameWindow getframe(){
        return mainFrame;
    }
    public static void setInstanceNull(){
        instance =null;
    }
}
