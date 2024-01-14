package main;

import threads.EnemyThread;
import threads.GameThread;

public class Game {

    private static Game instance;
    
    private Game() {
        GamePanel gamePanel = new GamePanel();
        new GameWindow(gamePanel);
        new GameThread(gamePanel);
        new EnemyThread(gamePanel);
        
        gamePanel.requestFocus();
    }

    public static Game getInstance(){
        if (instance == null){
            instance = new Game();
        }
        return instance;
    }
}
