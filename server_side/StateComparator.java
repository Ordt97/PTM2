package server_side;

import java.util.Comparator;

public class StateComparator implements Comparator<State> {

    @Override
    public int compare(State s1, State s2) {
        return Double.compare(s1.getCost(), s2.getCost());
    }
}
