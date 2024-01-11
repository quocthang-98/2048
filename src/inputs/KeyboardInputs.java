package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import main.GameState;

import tiles.Gameboard;

public class KeyboardInputs implements KeyListener {

    private int keysUsing = 11;

    private boolean[] keyState = new boolean[100];
    private boolean[] prevState = new boolean[100];
    // VK_A:65
    //VK_W:87
    //VK_S:83
    //VK_D:68
    // VK_UP:38
    //VK_DOWN:40
    //VK_LEFT: 37
    // VK_RIGHT:39
    // set up the array length about 100
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
                gPanel.setGoingUp(true);
                gameboard.moveTiles(Direction.UP);
            }
            if (typed(KeyEvent.VK_S) || typed(KeyEvent.VK_DOWN)) {
                gPanel.setGoingDown(true);
                gameboard.moveTiles(Direction.DOWN);
            }
            if (typed(KeyEvent.VK_A) || typed(KeyEvent.VK_LEFT)) {
                gPanel.setGoingLeft(true);
                gameboard.moveTiles(Direction.LEFT);
            }
            if (typed(KeyEvent.VK_D) || typed(KeyEvent.VK_RIGHT)) {
                gPanel.setGoingRight(true);
                gameboard.moveTiles(Direction.RIGHT);
            }
            if (typed(KeyEvent.VK_Q)) {
                if (gameboard.getAbility1().isReady) {

                    gameboard.getAbility1().isReady = false;
                    gameboard.getAbility1().castAbility();

                    gPanel.getAb1Thread().resetDuration();
                    gPanel.getAb1Thread().startTimer();
                }
            }
            if (typed(KeyEvent.VK_E)) {
                if (gameboard.getAbility2().isReady) {

                    gameboard.getAbility2().isReady = false;
                    gameboard.getAbility2().castAbility();

                    gPanel.getAb2Thread().resetDuration();
                    gPanel.getAb2Thread().startTimer();
                }
            }

            if (typed(KeyEvent.VK_ESCAPE)) {
                gPanel.setGameState(GameState.PAUSE);

                gPanel.getAb1Thread().pauseTimer();
                gPanel.getAb2Thread().pauseTimer();
            }
        }

        else if (gPanel.gameState == GameState.PAUSE) {


            if (typed(KeyEvent.VK_ESCAPE)) {
                gPanel.setGameState(GameState.PLAY);

                gPanel.getAb1Thread().startTimer();
                gPanel.getAb2Thread().startTimer();

            }
        }
    }

    public boolean typed (int e) {
        return keyState[e] && !prevState[e];
    }
    // true + true -> true
    // ortherwise, false
}
