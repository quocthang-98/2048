package abilities;

public class BoardStateStack {

    private int maxSize;
    private int top;
    private BoardState[] myStack;
    
    public BoardStateStack (int max) {
        this.maxSize = max;
        top = -1;
        myStack = new BoardState[maxSize];
    }

    public void push (BoardState b) {
        myStack[++top] = b;
    }

    public BoardState pop() {
        return myStack[top--];
    }

    public BoardState peek() {
        return myStack[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == maxSize - 1;
    }
}
