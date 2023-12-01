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
    public static boolean canMove(int[][] board, char move) {
        // Create a temporary board to check the move
        int[][] tempBoard = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(board[i], 0, tempBoard[i], 0, 4);
        }

        // Try the move on the temporary board
        if (move == 'w') {
            // Try to move upwards
            for (int j = 0; j < 4; j++) {
                int[] column = new int[4];
                for (int i = 0; i < 4; i++) {
                    column[i] = tempBoard[i][j];
                }
                column = slideLine(column);
                for (int i = 0; i < 4; i++) {
                    tempBoard[i][j] = column[i];
                }
            }
        } else if (move == 'a') {
            // Try to move left
            for (int i = 0; i < 4; i++) {
                int[] row = new int[4];
                System.arraycopy(tempBoard[i], 0, row, 0, 4);
                row = slideLine(row);
                System.arraycopy(row, 0, tempBoard[i], 0, 4);
            }
        } else if (move == 's') {
            // Try to move downwards
            for (int j = 0; j < 4; j++) {
                int[] column = new int[4];
                for (int i = 0; i < 4; i++) {
                    column[i] = tempBoard[3 - i][j];
                }
                column = slideLine(column);
                for (int i = 0; i < 4; i++) {
                    tempBoard[3 - i][j] = column[i];
                }
            }
        } else if (move == 'd') {
            // Try to move right
            for (int i = 0; i < 4; i++) {
                int[] row = new int[4];
                for (int j = 0; j < 4; j++) {
                    row[j] = tempBoard[i][3 - j];
                }
                row = slideLine(row);
                for (int j = 0; j < 4; j++) {
                    tempBoard[i][3 - j] = row[j];
                }
            }
        }

        // Check if the move changed the board
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != tempBoard[i][j]) {
                    return true;
                }
            }
        }

        return false;
    }



