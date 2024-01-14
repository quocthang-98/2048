package threads;

import main.GamePanel;
import main.GameState;

public class Ability2Thread extends AbilityThread {

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
            countDown();
        }
        else if (gp.gameState == GameState.PAUSE) {
            pauseTimer();
        }
    }

    private void countDown() {

        long sec = duration / 1000;
        gp.getGameboard().getAbility2().timeCount = gp.getGameboard().AB2_COOLDOWN - sec;

        if (gp.getGameboard().getAbility2().timeCount <= 0) {
            this.stopTimer();
            gp.getGameboard().getAbility2().timeCount = 0;
            gp.getGameboard().getAbility2().isReady = true;
        }
    }
}
