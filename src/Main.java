import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[][] board = new int[4][4];
        Random rand = new Random();

        // Initialize game / board
        for (int i = 0; i < 2; i++) {
            int r, c;
            do {
                r = rand.nextInt(4);
                c = rand.nextInt(4);
            } while (board[r][c] != 0);
            board[r][c] = (rand.nextInt(2) + 1) * 2;
        }

        // Output initial game board
        printBoard(board);

        while (true) {
            char move = getMove();
            if (canMove(board, move)) {
                makeMove(board, move);
                int r, c;
                do {
                    r = rand.nextInt(4);
                    c = rand.nextInt(4);
                } while (board[r][c] != 0);
                board[r][c] = (rand.nextInt(2) + 1) * 2;

                // Output current game board
                printBoard(board);

                // Check for win
                if (checkWin(board)) {
                    System.out.println("You've won!");
                    break;
                }
            } else if (!hasMoves(board)) {
                System.out.println("Game over. No moves left");
                break;
            }
        }
    }

    // Check for win
    public static boolean checkWin(int[][] board) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    // Check for no more moves
    public static boolean hasMoves(int[][] board) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // Print the game board
    public static void printBoard(int[][] board) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                System.out.printf("%5d", board[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Get the player's move
    public static char getMove() {
        Scanner scan = new Scanner(System.in);
        String move = scan.nextLine().toLowerCase();
        return move.charAt(0);
    }




