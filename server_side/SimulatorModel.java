package server_side;

import test.MyInterpreter;
import java.io.IOException;

public class SimulatorModel implements Model {

    @Override
    public void setThrottle(double v) {
        System.out.println("throttle "+v);
        // Send to FlightGear
        MyInterpreter.interpret(new String[]{"set /controls/engines/current-engine/throttle " + v});
    }

    @Override
    public void setRudder(double v) {
        System.out.println("rudder "+v);
        // Send to FlightGear
        MyInterpreter.interpret(new String[]{"set /controls/flight/rudder " + v});

    }

    @Override
    public void setAileron(double v) {
        System.out.println("aileron "+v);
        // Send to FlightGear
        MyInterpreter.interpret(new String[]{"set /controls/flight/aileron " + v});
    }

    @Override
    public void setElevator(double v) {
        System.out.println("elevator "+v);
        // Send to FlightGear
        MyInterpreter.interpret(new String[]{"set /controls/flight/elevator " + v});
    }

    public void openServer(String port, String freq) throws IOException {
        System.out.println("openDataServer Command pressed");
        MyInterpreter.interpret(new String[]{"openDataServer " + port + " " + freq});
    }

    public void connectToSimulator(String ip, int port) {
        MyInterpreter.interpret(new String[]{"connect " + ip + " " + port});
    }
}