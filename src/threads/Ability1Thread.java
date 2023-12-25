package threads;

import main.GamePanel;
import main.GameState;

public class Ability1Thread extends AbilityThread{

    private GamePanel gp;

    public Ability1Thread (GamePanel gp) {
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
        gp.getGameboard().getAbility1().timeCount = gp.getGameboard().AB1_COOLDOWN - sec;

        if (gp.getGameboard().getAbility1().timeCount <= 0) {
            this.stopTimer();
            gp.getGameboard().getAbility1().timeCount = 0;
            gp.getGameboard().getAbility1().isReady = true;
        }
    }
}
