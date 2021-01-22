package server_side;

import java.util.ArrayList;
import java.util.HashSet;

public class BestFirstSearch<Solution> extends CommonSearcher<Solution> {

    Heuristic heuristic;
    public interface Heuristic {
        double cost(State s, State goalState);
    }

    public BestFirstSearch(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Solution search(Searchable s) {
        openList.add(s.getInitialState());
        HashSet<State> closedSet = new HashSet<>();
        while (openList.size() > 0) {
            State n = popOpenList();
            closedSet.add(n);
            ArrayList<State> successors = s.getAllPossibleStates(n);
            n.setCost(n.getCost() + heuristic.cost(n, s.getDestinationState()));
            if (n.equals(s.getDestinationState())) return backTrace(n, s.getInitialState());
            for (State state : successors) {
                state.setCost(state.getCost() + heuristic.cost(state, s.getDestinationState()));
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
        return backTrace(s.getDestinationState(), s.getInitialState());
    }
}
