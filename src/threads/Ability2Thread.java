package threads;

import main.GamePanel;
import main.GameState;

public class Ability2Thread extends AbilityThread{

    private GamePanel gp;

    public Ability2Thread (GamePanel gp) {
        this.gp = gp;

        resetDuration();
        startTimer();
    }

    @Override
    public void cooldownUpdate() {

        setDuration();
        if (gp.gameState == GameState.PLAY) {
            System.out.println(duration);
            countDown();
        }
        else if (gp.gameState == GameState.PAUSE) {
            pauseTimer();
        }
    }

    private void countDown() {

        long sec = duration / 1000;
        gp.getGameboard().ab2_cdTime = gp.getGameboard().AB2_COOLDOWN - sec;

        if (gp.getGameboard().ab2_cdTime <= 0) {
            this.stopTimer();
            gp.getGameboard().ab2_cdTime = 0;
            gp.getGameboard().setAb2Status(true);
        }
    }
}
