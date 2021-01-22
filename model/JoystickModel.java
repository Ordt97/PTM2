package model;

public class JoystickModel {

    public double turnPlus(double currentHeading) {
        int heading = (int) currentHeading + 7;
        if (heading > 360)
            heading -= 360;
        return heading;
    }

    public double turnMinus(double currentHeading) {
        int heading = (int) currentHeading - 7;
        if (heading < 0)
            heading = 360 + heading;
        return heading;
    }
}
