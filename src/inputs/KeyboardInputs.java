package inputs;

import static constants.AnimationConstants.MoveConstants.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import main.GameState;
import threads.EnemyThread;
import tiles.Gameboard;


public class KeyboardInputs implements KeyListener {

    private int keysUsing = 13;

    private boolean[] keyState = new boolean[256];
    private boolean[] prevState = new boolean[256];

    private GamePanel gPanel;

    public KeyboardInputs (GamePanel gp) {
        gPanel = gp;
        keyInits();
    }

    private void keyInits() {
        for (int i = 0; i < keyState.length; i++) {
            keyState[i] = false;
        }
    }

    public void updatePrevKeys () {
        for (int i = 0; i < keysUsing; i++) {
            if (i == 0) prevState[KeyEvent.VK_A] = keyState[KeyEvent.VK_A];
            if (i == 1) prevState[KeyEvent.VK_D] = keyState[KeyEvent.VK_D];
            if (i == 2) prevState[KeyEvent.VK_W] = keyState[KeyEvent.VK_W];
            if (i == 3) prevState[KeyEvent.VK_S] = keyState[KeyEvent.VK_S];
            if (i == 4) prevState[KeyEvent.VK_UP] = keyState[KeyEvent.VK_UP];
            if (i == 5) prevState[KeyEvent.VK_DOWN] = keyState[KeyEvent.VK_DOWN];
            if (i == 6) prevState[KeyEvent.VK_LEFT] = keyState[KeyEvent.VK_LEFT];
            if (i == 7) prevState[KeyEvent.VK_RIGHT] = keyState[KeyEvent.VK_RIGHT];
            if (i == 8) prevState[KeyEvent.VK_ESCAPE] = keyState[KeyEvent.VK_ESCAPE];
            if (i == 9) prevState[KeyEvent.VK_Q] = keyState[KeyEvent.VK_Q];
            if (i == 10) prevState[KeyEvent.VK_E] = keyState[KeyEvent.VK_E];
            if (i == 11) prevState[KeyEvent.VK_F] = keyState[KeyEvent.VK_F];
            if (i == 12) prevState[KeyEvent.VK_SPACE] = keyState[KeyEvent.VK_SPACE];
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyState[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyState[e.getKeyCode()] = false;
    }

    public void eventUpdate(Gameboard gameboard) {

        if (gPanel.gameState == GameState.PLAY) {
            if (typed(KeyEvent.VK_W) || typed(KeyEvent.VK_UP)) {
                gameboard.moveTiles(Direction.UP);
                gameboard.updateBoardState();

                gPanel.getEnemyObject().setAggressive(false);
                EnemyThread.resetIdleCharge();
            }
            if (typed(KeyEvent.VK_S) || typed(KeyEvent.VK_DOWN)) {
                gameboard.moveTiles(Direction.DOWN);
                gameboard.updateBoardState();

                gPanel.getEnemyObject().setAggressive(false);
                EnemyThread.resetIdleCharge();

            }
            if (typed(KeyEvent.VK_A) || typed(KeyEvent.VK_LEFT)) {
                gameboard.moveTiles(Direction.LEFT);
                gameboard.updateBoardState();

                gPanel.getEnemyObject().setAggressive(false);
                EnemyThread.resetIdleCharge();

            }
            if (typed(KeyEvent.VK_D) || typed(KeyEvent.VK_RIGHT)) {
                gameboard.moveTiles(Direction.RIGHT);
                gameboard.updateBoardState();

                gPanel.getEnemyObject().setAggressive(false);
                EnemyThread.resetIdleCharge();

            }

            if (typed(KeyEvent.VK_SPACE)) {
                if (gameboard.attackIsReadyCheck()) {

                    gameboard.getPlayerBasicAttack().castAbility();
                    gameboard.updateBoardState();

                    gameboard.setAttackIsReady(false);
                    gameboard.increaseMaxCombination(25);
                    gameboard.setCombinationCount(0);

                    gPanel.setPlayerAnimation(ATTACK);
                    gPanel.setEnemyAnimation(BEING_HIT);

                    gPanel.getEnemyObject().setAggressive(false);
                    EnemyThread.resetIdleCharge();
                }

                
            }

            if (typed(KeyEvent.VK_1)) {
                if (gameboard.getAbility1().isReady) {

                    gameboard.getAbility1().isReady = false;
                    gameboard.getAbility1().castAbility();
                    gameboard.updateBoardState();

                    gPanel.setPlayerAnimation(USING_ABILITY);
                    gPanel.getAb1Thread().resetCooldown();

                    gPanel.getEnemyObject().setAggressive(false);
                    EnemyThread.resetIdleCharge();


                }
            }
            if (typed(KeyEvent.VK_2)) {
                if (gameboard.getAbility2().isReady) {

                    gameboard.getAbility2().isReady = false;
                    gameboard.getAbility2().castAbility();
                    gameboard.updateBoardState();

                    gPanel.setPlayerAnimation(USING_ABILITY);
                    gPanel.getAb2Thread().resetCooldown();

                    gPanel.getEnemyObject().setAggressive(false);
                    EnemyThread.resetIdleCharge();

                }
            }

            if (typed(KeyEvent.VK_3)) {
                if (gameboard.getAbility3().isReady) {

                    gameboard.getAbility3().isReady = false;
                    gameboard.getAbility3().castAbility();
                    gameboard.updateBoardState();

                    gPanel.setPlayerAnimation(USING_ABILITY);
                    gPanel.getAb3Thread().resetCooldown();

                    gPanel.getEnemyObject().setAggressive(false);
                    EnemyThread.resetIdleCharge();
                }
            }


            if (typed(KeyEvent.VK_ESCAPE)) {
                gPanel.setGameState(GameState.PAUSE);

                gPanel.pauseAllAbilitiesTimer();
            }
        }

        else if (gPanel.gameState == GameState.PAUSE) {
            
            if (typed(KeyEvent.VK_ESCAPE)) {
                gPanel.setGameState(GameState.PLAY);
                
                gPanel.startAllAbilitiesTimer();
            }
        }

        this.updatePrevKeys();
    }

    public boolean typed (int e) {
        return keyState[e] && !prevState[e];
    }
    
    public boolean noKeyInput() {
        for (int i = 0; i < keyState.length; i++) {
            if (typed(i) == true) {
                return false;
            }
        }
        return true;
    }
    
}
