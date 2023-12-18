package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import tiles.Gameboard;

public class KeyboardInputs implements KeyListener {

    private int keysUsing = 4;

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
    }

    public boolean typed (int e) {
        return keyState[e] && !prevState[e];
    }   
    
}
