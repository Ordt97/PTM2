package client_side;

import java.util.Observable;
import java.util.Observer;

public class Var extends Observable implements Observer {
    double value;
    String location;

    public Var() {
    }

    public Var(double v) {
        this.value = v;
        this.location = null;
    }

    public Var(String location) {
        super();
        this.location = location;
    }

    public double getValue() {
        return value;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return this.location;
    }

    public void setValue(double newValue) {
        if (this.value != newValue) {
            this.value = newValue;
            setChanged();
            notifyObservers(newValue);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Double d = (double) 0;
        if (arg.getClass() == (d.getClass())) {
            if (this.value != (double) arg) {
                this.setValue((double) arg);
                this.setChanged();
                this.notifyObservers(arg + "");
            }
        }
    }
}
