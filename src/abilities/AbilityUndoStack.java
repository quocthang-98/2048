package abilities;

import tiles.Tile;

public class AbilityUndoStack {
    
    private BoardStateStack undoStack;
    private int maxSize = 4;

    public AbilityUndoStack(Tile[][] board) {
        undoStack = new BoardStateStack(maxSize);
    }

    public void addState(Tile[][] board) {
        BoardState b = new BoardState(board);
        
        if (!undoStack.isFull()) {
            undoStack.push(b);
        }
        else {
            BoardStateStack temp = new BoardStateStack(maxSize);
            while (!undoStack.isEmpty()) {
                BoardState state = undoStack.pop();
                temp.push(state);
            }
            temp.pop();
            while (!temp.isEmpty()) {
                BoardState state = temp.pop();
                undoStack.push(state);
            }
            undoStack.push(b);
        }
    }

    public BoardStateStack getStack() {
        return this.undoStack;
    }

    public void clearAll() {
        while (!undoStack.isEmpty()) {
            undoStack.pop();
        }
    }
}
