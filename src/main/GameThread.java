package main;

public class GameThread implements Runnable {
    
    private Thread gameThread;
    private final int FPS = 60;
    private GamePanel gamePanel;

    public GameThread (GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double nextInterval = System.nanoTime() + drawInterval;

        int frame = 0;
        long lastFrameCheck = System.currentTimeMillis();

        while (gameThread != null) {

            gamePanel.update();
            gamePanel.drawFrame();

            try {
                double remainingTime = nextInterval - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextInterval += drawInterval;
                frame++;

                if (System.currentTimeMillis() - lastFrameCheck >= 1000) {
                    lastFrameCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frame);
                    frame = 0;
                }

            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    
}
