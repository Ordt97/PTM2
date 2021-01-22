package server_side;

public class State<T> {

    protected T state;
    private double cost;
    private State cameFrom;

    public State(T state) {
        this.state = state;
    }

    public T getState() {
        return state;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public State getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(State cameFrom) {
        this.cameFrom = cameFrom;
    }

    public boolean equals(State s) {
        return state.equals(s.getState());
    }

    @Override
    public String toString() {
        return "State [state=" + state + ", cost=" + cost + ", cameFrom=" + cameFrom + "]";
    }

    public double getCost() {
        if (this.getCameFrom() != null)
            return this.getCameFrom().getCost() + cost;

        else
            return cost;
    }

    @Override
    public int hashCode() {
        final int prime = 20;
        int result = 1;
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }
}
