package server_side;

import java.util.ArrayList;
import java.util.Arrays;

public class Matrix implements Searchable {
    private State initialState;
    private State destinationState;
    private final int[][] matrix;
    private final MatrixState[][] stateMatrix;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
        stateMatrix = new MatrixState[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                stateMatrix[i][j] = new MatrixState(i + "," + j);
                stateMatrix[i][j].setCost(matrix[i][j]);
            }
        }
    }

    @Override
    public State getInitialState() {
        return this.initialState;
    }

    @Override
    public State getDestinationState() {
        return this.destinationState;
    }

    @Override
    public String toString() {
        return "Matrix [initialState=" + initialState + ", destinationState=" + destinationState + ", matrix="
                + Arrays.toString(matrix) + "]";
    }

    public void setInitState(String s) {
        String[] loc = s.split(",");
        int row, col;
        row = Integer.parseInt(loc[0]);
        col = Integer.parseInt(loc[1]);
        this.initialState = stateMatrix[row][col];
    }

    public void setDestinationState(String s) {
        String[] loc = s.split(",");
        int row, col;
        row = Integer.parseInt(loc[0]);
        col = Integer.parseInt(loc[1]);
        this.destinationState = stateMatrix[row][col];
    }

    @Override
    public ArrayList<State> getAllPossibleStates(State s) {
        String[] loc = ((String) s.getState()).split(",");
        int row, col, tmp = 0;
        row = Integer.parseInt(loc[0]);
        col = Integer.parseInt(loc[1]);
        State right = null, left = null, up = null, down = null;

        ArrayList<State> ans = new ArrayList<>();
        if (row == 0)
            tmp += 3;
        if (row == matrix.length - 1)
            tmp += 7;
        if (col == 0)
            tmp += 5;
        if (col == matrix[row].length - 1)
            tmp += 11;
        switch (tmp) {
            case 8:
                right = stateMatrix[row][col + 1];
                down = stateMatrix[row + 1][col];
                break;
            case 3:
                right = stateMatrix[row][col + 1];
                down = stateMatrix[row + 1][col];
                left = stateMatrix[row][col - 1];
                break;
            case 5:
                right = stateMatrix[row][col + 1];
                down = stateMatrix[row + 1][col];
                up = stateMatrix[row - 1][col];
                break;
            case 7:
                right = stateMatrix[row][col + 1];
                up = stateMatrix[row - 1][col];
                left = stateMatrix[row][col - 1];
                break;
            case 11:
                up = stateMatrix[row - 1][col];
                left = stateMatrix[row][col - 1];
                down = stateMatrix[row + 1][col];
                break;
            case 14:
                left = stateMatrix[row][col - 1];
                down = stateMatrix[row + 1][col];
                break;
            case 12:
                up = stateMatrix[row - 1][col];
                right = stateMatrix[row][col + 1];
                break;
            case 18:
                up = stateMatrix[row - 1][col];
                left = stateMatrix[row][col - 1];
                break;
            default:
                up = stateMatrix[row - 1][col];
                left = stateMatrix[row][col - 1];
                right = stateMatrix[row][col + 1];
                down = stateMatrix[row + 1][col];
                break;
        }
        State[] surround = {right, left, up, down};
        for (State state : surround) {
            if (state != null)
                if (state != s.getCameFrom()) {
                    ans.add(state);
                }
        }

        return ans;
    }
}
