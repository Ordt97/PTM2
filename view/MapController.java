package view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MapController extends Pane {

    @FXML
    MapDisplayer mapCanvas;
    @FXML
    Button load;
    @FXML
    Button calc;

    Client client;
    Property<Boolean> enableProperty;

    private static final String folderPath="./src/sample/CSV/";

    public MapController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../JavaFX Components/map.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void bind(Client client, Property<Boolean> enableProperty){
        this.client=client;
        this.enableProperty=enableProperty;

        enableProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue){
                mapCanvas.setDisable(false);
                load.setDisable(false);
                calc.setDisable(false);
            }
        });

    }

    public void readCSV(File fileCSV) {
        try {
            mapCanvas.setMapData(fileCSV);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mapCanvas.redraw();
    }

    @FXML
    public void openFileDialogue(MouseEvent mouseEvent)
    {
        FileChooser chooser=new FileChooser();
        chooser.setInitialDirectory(new File(folderPath));
        File file = chooser.showOpenDialog(null);
        if(file!=null)
        {
            readCSV(file);
        }
    }
    @FXML
    public void connectToServer(MouseEvent mouseEvent)
    {
        Ip_PortController root=new Ip_PortController();
        Stage stage = new Stage();
        stage.setTitle("Enter IP and Port");
        stage.setScene(new Scene(root));
        stage.show();

        StringProperty ipProperty = new SimpleStringProperty();
        StringProperty portProperty=new SimpleStringProperty();
        root.bind(ipProperty,portProperty);
        portProperty.addListener((observable, oldValue, newValue) -> {
            String ip=ipProperty.get();
            String port=newValue;
            this.client.connect(ip,Integer.parseInt(port));
            mapCanvas.connectToServer(this.client);
            enableProperty.setValue(true);

        });
    }
    boolean flag=false;
    boolean isclick=false;
    StringProperty ipProperty = new SimpleStringProperty();
    StringProperty portProperty=new SimpleStringProperty();
    public void selectDestination(MouseEvent mouseEvent)
    {
        isclick=true;
        mapCanvas.markDestByMouse(mouseEvent.getX(),mouseEvent.getY());
        if (flag){
            calcPath(ipProperty.get(),portProperty.get());
        }
    }


    public void calcPath(MouseEvent mouseEvent)
    {
        if (!flag) {
            Ip_PortController root = new Ip_PortController();
            Stage stage = new Stage();
            stage.setTitle("Enter IP and Port");
            stage.setScene(new Scene(root));
            stage.show();
            root.bind(ipProperty, portProperty);
            portProperty.addListener((observable, oldValue, newValue) -> {
                String ip=ipProperty.get();
                String port=newValue;
                flag=true;
                //TODO
                calcPath(ip,port);
            });
        }
        else {
            calcPath(ipProperty.get(),portProperty.get());
        }


    }

    private void calcPath(String ip,String port){
        if (!isclick) return;
        try {
            PTMClient ptmClient=new PTMClient(ip,Integer.parseInt(port));
            double[][] doubles =mapCanvas.getCoordinates();
            String ans = ptmClient.sendMatrix(doubles, (int) mapCanvas.planeCanvas.getPlaneY(), (int) mapCanvas.planeCanvas.getPlaneX(),
                    (int)mapCanvas.xCanvas.getDestY(), (int)mapCanvas.xCanvas.getDestX());
            ptmClient.close();
            mapCanvas.showPoints(ans);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}