package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.*;
import java.util.*;

public class MapDisplayer extends Pane
{
    @FXML
    MapCanvas mapCanvas;

    @FXML
    PlaneCanvas planeCanvas;

    @FXML
    XCanvas xCanvas;

    @FXML
    PointCanvas pointCanvas;

    double initX, initY;
    double distance;
    Client client;

    public MapDisplayer()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../JavaFX Components/map_canvass.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setMapData(File fileCSV) throws FileNotFoundException
    {
        List<List<String>> records = new ArrayList<>();
        Scanner scanner = new Scanner(fileCSV);
        while (scanner.hasNextLine())
        {
            records.add(getRecordFromLine(scanner.nextLine()));
        }
        double x=Double.parseDouble(records.get(0).get(0));
        double y=Double.parseDouble(records.get(0).get(1));
        double distance=Double.parseDouble(records.get(1).get(0));
        List<List<String>> lists = records.subList(2, records.size());
        double[][] coordinates = lists.stream().map(strings -> strings.stream().mapToDouble(Double::parseDouble).toArray()).toArray(double[][]::new);
        setMapData(coordinates,x,y, distance);
    }

    private static final String COMMA_DELIMITER=",";

    private List<String> getRecordFromLine(String line)
    {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line))
        {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
            return values;
        }
    }

    public void setMapData(double[][] coordinates,double x, double y, double distance)
    {
        this.initX=x;
        this.initY=y;
        this.distance = distance;
        double widthBlock = (mapCanvas.getWidth()) / coordinates[0].length;
        double heightBlock = (mapCanvas.getHeight()) / coordinates.length;
        mapCanvas.setCoordinates(coordinates);
        planeCanvas.setBlockSize(widthBlock,heightBlock);
        xCanvas.setBlockSize(widthBlock,heightBlock);
        pointCanvas.setBlockSize(widthBlock,heightBlock);
    }

    public void redraw()
    {
        mapCanvas.redraw();
    }

    public void showPoints(String movesSt)
    {
        pointCanvas.showPoints(movesSt,(int)planeCanvas.getPlaneX(),(int)planeCanvas.getPlaneY());
        pointCanvas.redraw();
        LinkedList<Index> indexs = pointCanvas.getIndexs();
        for (int i = 1; i < indexs.size(); i++) {
            double v = 90- Math.toDegrees(Math.atan2(indexs.get(i).column - indexs.get(i - 1).column, indexs.get(i).row - indexs.get(i - 1).row)); //todo check 90-deg correct
            if (v<0) v+=360;
        }
    }

    public void movePlane(double posX, double posY)
    {
        planeCanvas.setPlaneCoordinate(posX,posY);
        planeCanvas.redraw();
    }

    public void markDestByMouse(double posX, double posY)
    {
        xCanvas.markDestByMouse(posX,posY);
        xCanvas.redraw();
    }

    public void connectToServer(final Client client)
    {
        this.client=client;
        Timer myTimer = new Timer();
        myTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Double lat = client.getValue("/position/latitude-deg");
                Double lon = client.getValue("/position/longitude-deg");
                Double heading = client.getValue("/instrumentation/heading-indicator/indicated-heading-deg");
                double x =(lon-initX+distance)/distance;
                double y=(((lat-initY+distance)/distance));
                planeCanvas.setHeading(heading);
                movePlane(x,y*-1);
            }
        },1000,250);
    }

    public double[][] getCoordinates()
    {
        return mapCanvas.getCoordinates();
    }
}