package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import view_model.ViewModel;

public class JoyStickController {

    // Regular data members
    private final StringProperty aileronV;
    private final StringProperty elevatorV;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    ViewModel vm;

    // GUI data members
    @FXML
    Slider throttle;
    @FXML
    Slider rudder;
    @FXML
    private Circle joystick;
    @FXML
    private Circle frameCircle;

    // Objects for manual mode data panel
    @FXML
    private final Label aileronValue;
    @FXML
    private final Label elevatorValue;
    @FXML
    private final Label throttleValue;
    @FXML
    private final Label rudderValueLabel;

    public JoyStickController(){
        joystick = new Circle();
        throttleValue = new Label();
        rudderValueLabel = new Label();
        aileronValue = new Label();
        elevatorValue = new Label();

        aileronV = new SimpleStringProperty();
        elevatorV = new SimpleStringProperty();

        throttleValue.setText("0");
        rudderValueLabel.setText("0");
        aileronValue.setText("0");
        elevatorValue.setText("0");
    }

    public void setViewModel(ViewModel vm) {
        this.vm=vm;
        vm.throttle.bind(throttle.valueProperty());
        vm.rudder.bind(rudder.valueProperty());

        vm.aileron.bind(aileronV);
        vm.elevator.bind(elevatorV);
        joystick = new Circle();
    }

    @FXML
    private void joystickPressed(MouseEvent me) {
        orgSceneX = me.getSceneX();
        orgSceneY = me.getSceneY();
        orgTranslateX = ((Circle) (me.getSource())).getTranslateX();
        orgTranslateY = ((Circle) (me.getSource())).getTranslateY();
    }

    @FXML
    private void joystickDragged(MouseEvent me) {
        double offsetX = me.getSceneX() - orgSceneX;
        double offsetY = me.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;
        double joystickCenterX = frameCircle.getTranslateX() + frameCircle.getRadius() - joystick.getRadius() - 33;
        double joystickCenterY = frameCircle.getTranslateY() - frameCircle.getRadius() - joystick.getRadius() - 33;
        double frameRadius = frameCircle.getRadius();
        double maxX = joystickCenterX + frameRadius;
        double contractionsCenterX = joystickCenterX - frameRadius;
        double maxY = joystickCenterY - frameRadius;
        double contractionsCenterY = joystickCenterY + frameRadius;

        double slant = Math
                .sqrt(Math.pow(newTranslateX - joystickCenterX, 2) + Math.pow(newTranslateY - joystickCenterY, 2));

        if (slant > frameRadius) {
            double alpha = Math.atan((newTranslateY - joystickCenterY) / (newTranslateX - joystickCenterX));
            if ((newTranslateX - joystickCenterX) < 0) {
                alpha = alpha + Math.PI;
            }
            newTranslateX = Math.cos(alpha) * frameRadius + orgTranslateX;
            newTranslateY = Math.sin(alpha) * frameRadius + orgTranslateY;
        }
        ((Circle) (me.getSource())).setTranslateX(newTranslateX);
        ((Circle) (me.getSource())).setTranslateY(newTranslateY);
        // normalize to range of [-1,1]
        double normalX = Math
                .round(((((newTranslateX - contractionsCenterX) / (maxX - contractionsCenterX)) * 2) - 1) * 100.00)
                / 100.00;
        // normalize to range of [-1,1]
        double normalY = Math
                .round(((((newTranslateY - contractionsCenterY) / (maxY - contractionsCenterY)) * 2) - 1) * 100.00)
                / 100.00;
        // send command only if manual mode is selected

        aileronValue.setText("" + normalX);
        elevatorValue.setText("" + normalY);
        aileronV.set("" + normalX);
        elevatorV.set("" + normalY);


        vm.setJoystickChanges();
    }

    @FXML
    private void joystickReleased(MouseEvent me) {
        ((Circle) (me.getSource())).setTranslateX(frameCircle.getTranslateX() + frameCircle.getRadius() - joystick.getRadius() - 33);
        ((Circle) (me.getSource())).setTranslateY(frameCircle.getTranslateY() - frameCircle.getRadius() - joystick.getRadius() - 33);

        aileronValue.setText("" + 0);
        elevatorValue.setText("" + 0);
        aileronV.set("0.0");
        elevatorV.set("0.0");

        // update that the value changed
        vm.setJoystickChanges();

    }

    public void setSliderOnDragEvent() {
        rudder.valueProperty().addListener((ChangeListener<Object>) (arg0, arg1, arg2) -> {
            rudderValueLabel.textProperty().setValue("" + (Math.round((rudder.getValue() * 10.00))) / 10.00);
            vm.setRudder();

        });

        throttle.valueProperty().addListener((ChangeListener<Object>) (arg0, arg1, arg2) -> {
            throttleValue.textProperty()
                    .setValue("" + (Math.round((throttle.getValue() * 10.00))) / 10.00);
            vm.setThrottle();

        });
    }

}