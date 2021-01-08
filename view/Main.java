package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import server_side.SimulatorModel;
import view_model.ViewModel;
import java.io.IOException;

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage _primaryStage) {
        primaryStage = _primaryStage;

        // models
        SimulatorModel simModel = new SimulatorModel();
        // view model
        ViewModel viewModel = new ViewModel(simModel);

        FXMLLoader fxl=new FXMLLoader();
        try {
            BorderPane root = fxl.load(getClass().getResource("JoyStick.fxml").openStream());

            JoyStickController wc=fxl.getController(); // View
            wc.setViewModel(viewModel);

            Scene scene = new Scene(root,350,800);
            scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
            wc.setSliderOnDragEvent();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);

    }
}