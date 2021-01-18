package server_side;

import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class CommonSearcher<Solution> implements Searcher<Solution> {

    protected PriorityQueue<State> openList;
    protected int evaluateNodes;

    public CommonSearcher() {
        Comparator<State> comp = new StateComparator();
        openList = new PriorityQueue<>(comp);
        evaluateNodes = 0;
    }

    protected State popOpenList() {
        evaluateNodes++;
        return openList.poll();
    }

    protected Solution backTrace(State goalState, State initialState) {
        if (goalState.equals(initialState))
            return (Solution) initialState.getState();
        return (Solution) (backTrace(goalState.getCameFrom(), initialState) + "->" + goalState.getState());
    }
}
