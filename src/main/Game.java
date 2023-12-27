package main;

import threads.GameThread;

public class Game {
    private static Game instance;
    public static GameWindow mainFrame;
    
    public Game() {
        GamePanel gamePanel = new GamePanel();
        mainFrame = new GameWindow(gamePanel);
        new GameThread(gamePanel);
        
        gamePanel.requestFocus();
    }
    public static Game getInstance(){
        if (instance == null){
            instance = new Game();
        }
        return instance;
    }
    public static void setInstanceNull(){
        instance =null;
    }
}
