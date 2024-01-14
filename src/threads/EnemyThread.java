package threads;

import main.GamePanel;
import main.GameState;

public class EnemyThread implements Runnable {
    
    private Thread gameThread;
    private GamePanel gamePanel;

    private int normalLoop = 5000;
    private int aggressiveLoop = 1000;

    public static int idleCharge;
    public static final int idleLimit = 500;

    public EnemyThread (GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / GameThread.getFPS();
        double nextInterval = System.nanoTime() + drawInterval;
        idleCharge = 0;

        long lastFrameCheck = System.currentTimeMillis();
        long lastAggressiveFrameCheck = System.currentTimeMillis();

        while (gameThread != null) {

            try {
                double remainingTime = nextInterval - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextInterval += drawInterval;


                if (gamePanel.getGameState() == GameState.PLAY) {
                    if (System.currentTimeMillis() - lastAggressiveFrameCheck >= 1000 && !gamePanel.getEnemyObject().getAggressive()) {
                        lastAggressiveFrameCheck = System.currentTimeMillis();

                        idleCharge++;
                        if (idleCharge >= idleLimit) {
                            gamePanel.getEnemyObject().setAggressive(true);
                        }
                    }

                    if (gamePanel.getEnemyObject().getAggressive()) {
                        lastAggressiveFrameCheck = System.currentTimeMillis();

                        if (System.currentTimeMillis() - lastFrameCheck >= aggressiveLoop) {
                            lastFrameCheck = System.currentTimeMillis();
                            gamePanel.damagePlayer(1000, true);
                        }
                    }

                    else {
                        if (System.currentTimeMillis() - lastFrameCheck >= normalLoop) {
                            lastFrameCheck = System.currentTimeMillis();

                            gamePanel.damagePlayer(gamePanel.calculateEnemyLoopDamage(), false);
                        }
                    }
                }

            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void resetIdleCharge() {
        idleCharge = 0;
    }
}
