package view;

import commands.DisconnectCommand;
import client_side.AutoPilot;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view_model.ViewModel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Window.fxml"));
        Parent root = loader.load();
        WindowController ctrl = loader.getController();
        ViewModel viewModel = new ViewModel();
        Model model = new Model();
        model.addObserver(viewModel);
        viewModel.setModel(model);
        viewModel.addObserver(ctrl);
        ctrl.setViewModel(viewModel);
        primaryStage.setTitle("FlightGear Simulator");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            DisconnectCommand command = new DisconnectCommand();
            String[] disconnect = {""};
            command.doCommand(disconnect);
            AutoPilot.autoPilot.interrupt();
            model.stopAll();
            System.out.println("bye");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
