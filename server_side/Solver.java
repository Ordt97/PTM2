package server_side;

public interface Solver<Problem, Solution> {
    Solution Solve(Problem p);
}
