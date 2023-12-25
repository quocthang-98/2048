package main;

import threads.GameThread;

public class Game {
    
    public Game() {
        GamePanel gamePanel = new GamePanel();
        new GameWindow(gamePanel);
        new GameThread(gamePanel);
        
        gamePanel.requestFocus();
    }
}
