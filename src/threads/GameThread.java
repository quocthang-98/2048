package threads;

import ExperienceBar.ExperienceBar;
import main.GamePanel;
import main.GameWindow;



public class GameThread implements Runnable {

    private ExperienceBar experienceBar;
    private GameWindow frame;

    private Thread gameThread;
    private final int FPS = 60;
    private GamePanel gamePanel;

    public GameThread (GamePanel gamePanel, GameWindow frame) {
        this.gamePanel = gamePanel;
        this.frame = frame;
        gameThread = new Thread(this);
        experienceBar = new ExperienceBar(gamePanel,frame);
        gameThread.start();
    }

    public void run() {

        double drawInterval = 1000000000 / FPS;
        double nextInterval = System.nanoTime() + drawInterval;

        int frame1 = 0;
        long lastFrameCheck = System.currentTimeMillis();

        while (true) {

            gamePanel.update();
            gamePanel.drawFrame();
            gamePanel.getAb1Thread().cooldownUpdate();
            gamePanel.getAb2Thread().cooldownUpdate();
            try {
                double remainingTime = nextInterval - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextInterval += drawInterval;
                frame1++;

                if (System.currentTimeMillis() - lastFrameCheck >= 1000) {
                    lastFrameCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frame1);
                    frame1 = 0;
                }

            } catch(InterruptedException e){
                e.printStackTrace();
            }

        }
    }
    public int getFPS () {
        return FPS;
    }
}
