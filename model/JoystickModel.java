package model;

public class JoystickModel {

    public double turnPlus(double currentHeading) {
        int tmp = (int) currentHeading + 7;
        if (tmp > 360)
            tmp -= 360;
        return tmp;
    }

    public double turnMinus(double currentHeading) {
        int tmp = (int) currentHeading - 7;
        if (tmp < 0)
            tmp = 360 + tmp;
        return tmp;
    }
}
