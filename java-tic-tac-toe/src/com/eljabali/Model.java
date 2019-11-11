package com.eljabali;

public class Model {

    public final static String GAME_RESULT_MESSAGE = "The winner is player:" + "\n";
    public final static String GAME_RESULT_MESSAGE_DRAW = "We have NO winner: \n DRAW";
    public final static String DO_YOU_WANT_TO_RESTART = "Do you want to play again?(Y for yes & N for no)" + "\n";
    public final static String DO_YOU_WANT_TO_RESTART_PT_2 = "Please enter Y for yes & N for no" + "\n";
    public final static String GAME_IS_OVER = "Thank you for playing." + "\n" + "Goodbye!";
    public final static String ENTER_GRID_SIZE_MESSAGE = "Enter the size of the grid(EX: 3x3 or 9x9) The minimum is 3";
    public final static String ENTER_ROW = "Enter row player ";
    public final static String ENTER_COLUMN = "Enter column player ";
    public final static String INPUT_ANSWER_YES = "y";
    public final static String INPUT_ANSWER_NO = "n";

    public int gridSize;

    public boolean enterRestartGame;
    public boolean enterGridSize;
    public boolean enterRow;
    public boolean enterColumn;

    public Player currentPlayer = Player.X;
    public int currentPlayerRow;
    public int currentPlayerColumn;
    public Player[][] gridPlayer;

    public void initializeGrid() {
        gridPlayer = new Player[gridSize][gridSize];
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                gridPlayer[row][column] = Player.NA;
            }
        }
    }
}
