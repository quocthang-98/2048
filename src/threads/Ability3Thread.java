package threads;

import main.GamePanel;
import main.GameState;

public class Ability3Thread extends AbilityThread {

    private GamePanel gp;

    public Ability3Thread (GamePanel gp) {
        this.gp = gp;

        resetDuration();
        startTimer();
    }

    @Override
    public void cooldownUpdate() {

        setDuration();
        if (gp.gameState == GameState.PLAY) {
            countDown();
        }
        else if (gp.gameState == GameState.PAUSE) {
            pauseTimer();
        }
    }

    private void countDown() {

        long sec = duration / 1000;
        gp.getGameboard().getAbility3().timeCount = gp.getGameboard().AB3_COOLDOWN - sec;

        if (gp.getGameboard().getAbility3().timeCount <= 0) {
            this.stopTimer();
            gp.getGameboard().getAbility3().timeCount = 0;
            gp.getGameboard().getAbility3().isReady = true;
        }
    }
}
