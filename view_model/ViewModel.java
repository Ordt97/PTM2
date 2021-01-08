package view_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server_side.SimulatorModel;

public class ViewModel {

    // models
    private final SimulatorModel simulator;

    public StringProperty simulatorIP;
    public StringProperty simulatorPort;
    // throttle & rudder
    public DoubleProperty rudder;
    public DoubleProperty throttle;

    // joystick arguments
    public StringProperty aileron;
    public StringProperty elevator;

    // script text



    // *** end of variables
    public ViewModel(SimulatorModel simulator) {
        this.simulator = simulator;
        simulatorIP = new SimpleStringProperty();
        simulatorPort = new SimpleStringProperty();

        // Sliders
        rudder = new SimpleDoubleProperty();
        throttle = new SimpleDoubleProperty();

        // Joystick
        aileron = new SimpleStringProperty();
        elevator = new SimpleStringProperty();

        connectToSimulator();
        initData();
        System.out.println("Connected to flight-gear server");
    }

    public void connectToSimulator() {
        simulator.connectToSimulator("127.0.0.1", 5402);
    }

    public void setThrottle() {
        simulator.setThrottle(throttle.get());
    }

    public void setRudder() {
        simulator.setRudder(rudder.get());

    }

    public void setJoystickChanges() {
        simulator.setAileron(Double.parseDouble(aileron.get()));
        simulator.setElevator(Double.parseDouble(elevator.get()));
    }

    public void initData()
    {
        simulator.setAileron(0);
        simulator.setRudder(0);
        simulator.setThrottle(0);
        simulator.setElevator(0);
    }
}