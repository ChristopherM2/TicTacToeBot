import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int board[][] = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
        printBoard(board);
        Scanner scanner = new Scanner(System.in);
        game(scanner, board);
        while (true) {
            System.out.println("Do you want to play again? (Y/N)");
            String answer = scanner.nextLine();
            answer = answer.toUpperCase();
            if (answer.equals("N")) {
                System.out.println("Goodbye!");
                break;
            }
            if (answer.equals("Y")) {
                board = new int[3][3];
                printBoard(board);
                game(scanner, board);
            }
        }



    }
    public static void game(Scanner scanner, int[][] board) {
        while (true) {

            System.out.println("Please make a move such as A1");
            String move = scanner.nextLine();
            move = move.toUpperCase();
            int row = move.charAt(1) - '0'-1;
            int col = move.charAt(0) - 'A';
            while (board[row][col] != 0) {
                System.out.println("Invalid move, please try again");
                move = scanner.nextLine();
                move = move.toUpperCase();
                row = move.charAt(1) - '0'-1;
                col = move.charAt(0) - 'A';
            }


                board[row][col] = 1;


            printBoard(board);
            if (evaluate(board) == -10 ) {
                System.out.println("You won!");
                break;
            }
            if (isMovesLeft(board)) {
                System.out.println("It's a tie!");
                break;
            }
            System.out.println("Please wait for the computer to make a move");

            String s = computerMove(board);
            row = s.charAt(1) - '0'-1;
            col = s.charAt(0) - 'A';
            board[row][col] = 2;
            printBoard(board);
            if (evaluate(board) == 10) {
                System.out.println("Computer won!");
                break;
            }
        }
    }

    public static void printBoard(int board[][]) {
        System.out.println("---A---B---C--");
        for (int i = 0; i < 3; i++) {
            System.out.print(i+1+"| ");
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == 0) {
                    System.out.print("  | ");
                } else if (board[i][j] == 1) {
                    System.out.print("X | ");
                } else {
                    System.out.print("O | ");
                }

            }
            System.out.println();
            System.out.println("-------------");
        }
    }
    public static String computerMove(int[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        // Traverse all cells, evaluate minimax for each empty cell
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = 2;  // Make the move
                    int score = minimax(board, 0, false);  // Call minimax with player to move
                    board[i][j] = 0;  // Undo the move
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        // Convert the best move to 'A1' format
        return (char)(bestCol + 'A') + "" + (bestRow + 1);
    }


    public static int minimax(int[][] board, int depth, boolean isMaximizing) {
        int score = evaluate(board);

        // If the computer has won, return the score
        if (score == 10) return score - depth;  // Computer win (favor quicker wins)
        if (score == -10) return score + depth; // Player win (favor delaying loss)


        // If there are no more moves and no winner, it's a tie
        if (isMovesLeft(board)) {
            return 0;
        }

        // If this is the computer's move
        if (isMaximizing) {
            int best = Integer.MIN_VALUE;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (board[i][j] == 0) {
                        // Make the move
                        board[i][j] = 2;

                        // Call minimax recursively and choose the maximum value
                        best = Math.max(best, minimax(board, depth + 1, false));

                        // Undo the move (cant play twice)
                        board[i][j] = 0;
                    }
                }
            }
            return best;
        } else {
            // If this is the player's move
            int best = Integer.MAX_VALUE;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (board[i][j] == 0) {
                        // Make the move
                        board[i][j] = 1;

                        // Call minimax recursively and choose the minimum value
                        best = Math.min(best, minimax(board, depth + 1, true));

                        // Undo the move
                        board[i][j] = 0;
                    }
                }
            }
            return best;
        }
    }

    public static boolean isMovesLeft(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int evaluate(int[][] board) {
        // Winning conditions
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == 1) return -10; // Player 1 win
                if (board[i][0] == 2) return 10;  // Computer win
            }
        }
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == 1) return -10;
                if (board[0][i] == 2) return 10;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == 1) return -10;
            if (board[0][0] == 2) return 10;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == 1) return -10;
            if (board[0][2] == 2) return 10;
        }
        return 0; // No winner yet
    }

}
