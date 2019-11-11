package com.eljabali;

import static com.eljabali.Model.*;
import static com.eljabali.Player.*;

public class ViewModel implements ViewListener {

    private ViewState viewState;
    private View view;
    private Model model;

    public static void main(String[] args) {
        new ViewModel().start();
    }

    private void start() {
        view = new View(this);
        viewState = new ViewState();
        model = new Model();
        viewState.askForInput = true;
        viewState.displayOutput = true;
        model.enterGridSize = true;
        viewState.output = ENTER_GRID_SIZE_MESSAGE;
        model.enterRow = true;
        model.enterColumn = false;
        invalidateView();
    }

    private void invalidateView() {
        view.setNewViewState(viewState);
    }

    public String getDisplayGrid() {
        final String padding = "  ";
        StringBuilder grid = new StringBuilder(padding);
        for (int i = 0; i < model.gridSize; i++) {
            grid.append(i).append(padding);
        }
        grid.append("\n");
        for (int row = 0; row < model.gridSize; row++) {
            grid.append(row);
            for (int column = 0; column < model.gridSize; column++) {
                grid.append("[").append(model.gridPlayer[row][column].displayValue).append("]");
            }
            grid.append("\n");
        }
        return grid.toString();
    }

    @Override
    public void enteredInput(String input) {
        if (model.enterGridSize) {
            int gridSize;

            try {
                gridSize = Integer.valueOf(input);
            } catch (NumberFormatException exception) {
                gridSize = -1;
            }
            if (!isGridSizeWithinRange(gridSize)) {
                viewState.askForInput = true;
                model.enterGridSize = true;
                viewState.displayOutput = true;
                viewState.output = ENTER_GRID_SIZE_MESSAGE;
                invalidateView();
                return;
            }
            viewState.askForInput = true;
            model.gridSize = gridSize;
            model.initializeGrid();
            model.enterGridSize = false;
            viewState.output = getDisplayGrid();
            viewState.output += ENTER_ROW + model.currentPlayer;
            invalidateView();
            return;
        }
        if (model.enterRow || model.enterColumn) {
            int enteredRow = 0;
            int enteredColumn = 0;
            try {
                if (model.enterRow) {
                    enteredRow = Integer.valueOf(input);
                }
                if (model.enterColumn) {
                    enteredColumn = Integer.valueOf(input);
                }
            } catch (NumberFormatException exception) {
                if (model.enterRow) {
                    enteredRow = -1;
                }
                if (model.enterColumn) {
                    enteredColumn = -1;
                }
            }
            if (model.enterRow) {
                if (!isXOrYWithinGridRange(enteredRow)) {
                    viewState.askForInput = true;
                    model.enterRow = true;
                    viewState.displayOutput = true;
                    viewState.output = ENTER_ROW + model.currentPlayer;
                    invalidateView();
                    return;
                }
            }
            if (model.enterColumn) {
                if (!isXOrYWithinGridRange(enteredColumn)) {
                    viewState.askForInput = true;
                    model.enterColumn = true;
                    viewState.displayOutput = true;
                    viewState.output = ENTER_COLUMN + model.currentPlayer;
                    invalidateView();
                    return;
                }
            }
            if (model.enterRow) {
                model.currentPlayerRow = enteredRow;
                model.enterRow = false;
                model.enterColumn = true;
                viewState.output = ENTER_COLUMN + model.currentPlayer;
                viewState.displayOutput = true;
                viewState.askForInput = true;
                invalidateView();
                return;
            }
            if (model.enterColumn) {
                model.currentPlayerColumn = enteredColumn;
                model.enterColumn = false;
                model.enterRow = true;
                viewState.displayOutput = true;
                viewState.askForInput = true;
                enteredCoordinate();
            }
            return;
        }
        if (model.enterRestartGame) {
            char index_0 = input.charAt(0);
            if (!(Character.toString(index_0).equalsIgnoreCase(INPUT_ANSWER_YES) || Character.toString(index_0).equalsIgnoreCase(INPUT_ANSWER_NO))) {
                viewState.output = DO_YOU_WANT_TO_RESTART_PT_2;
                viewState.askForInput = true;
                viewState.displayOutput = true;
                invalidateView();
                return;
            }
            if (Character.toString(index_0).equalsIgnoreCase(INPUT_ANSWER_NO)) {
                viewState.output = GAME_IS_OVER;
                viewState.askForInput = false;
                viewState.displayOutput = true;
                invalidateView();
                return;
            }
            start();
        }
    }


    private void enteredCoordinate() {
        if (!model.gridPlayer[model.currentPlayerRow][model.currentPlayerColumn].equals(NA)) {
            model.enterRow = true;
            viewState.output = getDisplayGrid();
            viewState.output += ENTER_ROW + model.currentPlayer;
            viewState.displayOutput = true;
            viewState.askForInput = true;
            invalidateView();
            return;
        }
        model.gridPlayer[model.currentPlayerRow][model.currentPlayerColumn] = model.currentPlayer;
        if (model.currentPlayer == X) {
            model.currentPlayer = O;
        } else {
            model.currentPlayer = X;
        }

        if (isThereWinner()) {
            model.enterColumn = false;
            model.enterRow = false;
            model.enterRestartGame = true;
            viewState.output = getDisplayGrid();
            viewState.output += GAME_RESULT_MESSAGE + model.currentPlayer.displayValue
                    + "\n" + DO_YOU_WANT_TO_RESTART;
            viewState.askForInput = true;
            viewState.displayOutput = true;
        } else if (isThereDraw()) {
            model.enterRestartGame = true;
            viewState.output = getDisplayGrid();
            viewState.output += GAME_RESULT_MESSAGE_DRAW + "\n" + DO_YOU_WANT_TO_RESTART;
            viewState.askForInput = true;
            viewState.displayOutput = true;
        } else {
            viewState.output = getDisplayGrid();
            viewState.output += ENTER_ROW + model.currentPlayer;
            viewState.askForInput = true;
            viewState.displayOutput = true;
        }
        invalidateView();
    }

    private boolean isThereDraw() {
        for (int row = 0; row < model.gridSize; row++) {
            for (int column = 0; column < model.gridSize; column++) {
                if (model.gridPlayer[row][column] == NA) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isThereWinner() {
        return isThereRowWinner() || isThereColumnWinner() || isThereDiagonalWinner();
    }

    private boolean isThereRowWinner() {
        for (int row = 0; row < model.gridSize; row++) {
            boolean isThereWinInRow = true;
            for (int column = 0; column < model.gridSize; column++) {
                if (model.gridPlayer[row][column] == Player.NA) {
                    isThereWinInRow = false;
                    break;
                }
                if (column - 1 < 0) {
                    continue;
                }
                if (model.gridPlayer[row][column] != model.gridPlayer[row][column - 1]) {
                    isThereWinInRow = false;
                    break;
                }
            }
            if (isThereWinInRow) {
                return true;
            }
        }
        return false;
    }

    private boolean isThereColumnWinner() {
        for (int column = 0; column < model.gridSize; column++) {
            boolean isThereWinInColumn = true;
            for (int row = 0; row < model.gridSize; row++) {
                if (model.gridPlayer[row][column] == Player.NA) {
                    isThereWinInColumn = false;
                    break;
                }
                if (row - 1 < 0) {
                    continue;
                }
                if (model.gridPlayer[row][column] != model.gridPlayer[row - 1][column]) {
                    isThereWinInColumn = false;
                    break;
                }
            }
            if (isThereWinInColumn) {
                return true;
            }
        }
        return false;
    }

    private boolean isThereDiagonalWinner() {
        boolean isThereWinInDiagonal = true;
        for (int rowAndColumn = 0; rowAndColumn < model.gridSize; rowAndColumn++) {
            if (model.gridPlayer[rowAndColumn][rowAndColumn] == Player.NA) {
                isThereWinInDiagonal = false;
                break;
            }
            if (rowAndColumn - 1 < 0) {
                continue;
            }
            if (model.gridPlayer[rowAndColumn][rowAndColumn] != model.gridPlayer[rowAndColumn - 1][rowAndColumn - 1]) {
                isThereWinInDiagonal = false;
                break;
            }
        }
        if (isThereWinInDiagonal) {
            return true;
        }
        isThereWinInDiagonal = true;
        final int LAST_INDEX = 2;
        for (int row = LAST_INDEX, column = 0; row > -1 && column > -1; row--, column++) {
            if (model.gridPlayer[row][column] == Player.NA) {
                isThereWinInDiagonal = false;
                break;
            }
            if (row + 1 > LAST_INDEX || column - 1 < 0) {
                continue;
            }
            if (model.gridPlayer[row][column] != model.gridPlayer[row + 1][column - 1]) {
                isThereWinInDiagonal = false;
                break;
            }
        }
        return isThereWinInDiagonal;
    }

    private boolean isXOrYWithinGridRange(int value) {
        return value < model.gridSize && value >= 0;
    }

    private boolean isGridSizeWithinRange(int value) {
        return value > 2;
    }

}
