package server_side;

import java.util.ArrayList;
import java.util.HashSet;

public class Astar<Solution> extends CommonSearcher<Solution> {

    public interface Heuristic {
        double cost(State s, State goalState);
    }

    Heuristic h;

    public Astar(Heuristic h) {
        this.h = h;
    }

    @Override
    public Solution search(Searchable s) {
        openList.add(s.getInitialState());
        HashSet<State> closedSet = new HashSet<>();
        while (openList.size() > 0) {
            State n = popOpenList();
            closedSet.add(n);
            ArrayList<State> successors = s.getAllPossibleStates(n);
            n.setCost(n.getCost() + h.cost(n, s.getGoalState()));
            if (n.equals(s.getGoalState()))
                return backTrace(n, s.getInitialState());
            for (State state : successors) {
                state.setCost(state.getCost() + h.cost(state, s.getGoalState()));
                if (!closedSet.contains(state) && !openList.contains(state)) {
                    state.setCameFrom(n);
                    openList.add(state);
                } else if (n.getCost() + (state.getCost() - state.getCameFrom().getCost()) < state.getCost())
                    if (openList.contains(state))
                        state.setCameFrom(n);
                    else {
                        state.setCameFrom(n);
                        closedSet.remove(state);
                        openList.add(state);
                    }
            }
        }
        return backTrace(s.getGoalState(), s.getInitialState());
    }

}
