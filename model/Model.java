package model;

import client_side.AutoPilot;
import client_side.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements Observer {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 5402;
    private final JoystickModel joystickModel;
    private final SimulatorModel simulatorModel;
    private final MapModel mapModel;
    public static volatile boolean stop = false;
    public static volatile boolean turn = true;
    private final Interpreter interpreter;
    private static Socket socketPath;
    private static PrintWriter outPath;
    private static BufferedReader in;
    double startX;
    double startY;
    double planeX;
    double planeY;
    double markX;
    double markY;
    int[][] data;
    double offset;
    double currentLocationX;
    double currentLocationY;
    double currentHeading;
    ArrayList<String[]> intersections = new ArrayList<>();
    Thread route;
    Thread rudder;
    int indexPlan = 0;

    public Model() {
        simulatorModel = new SimulatorModel();
        joystickModel = new JoystickModel();
        mapModel = new MapModel();
        interpreter = new Interpreter();
        route=new Thread(()->{this.routeStart();});
        rudder=new Thread(()->{this.rudderStart();});
    }

    public void connectManual(String ip, int port) {
        simulatorModel.connect(ip, port);
    }

    public void send(String[] data) {
        simulatorModel.send(data);
    }

    public void parse(String[] script) {
        interpreter.interpret(script);
    }

    public void execute() {
        interpreter.execute();
    }

    public void stopAutoPilot() {
        interpreter.stop();
    }

    @Override
    public void update(Observable o, Object arg) { }

    public void connect(String ip, int port) {
        try {
            socketPath = new Socket(ip, port);
            outPath = new PrintWriter(socketPath.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketPath.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopAll() {
        Model.stop = true;
        try {
            if (outPath != null) outPath.close();
            if (in != null) in.close();
            if (socketPath != null) socketPath.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        simulatorModel.stop();
        AutoPilot.autoPilot.interrupt();
        AutoPilot.close = true;
        Model.turn = true;
    }

    public void GetPlaneLocation(double startX, double startY, double offset) {
        this.offset = offset;
        this.startX = startX;
        this.startY = startY;
        new Thread(() -> {
            Socket socket = null;
            try {
                socket = new Socket(IP, PORT);
                System.out.println("Searching for location...");
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!stop) {
                    out.println("dump /position");
                    out.flush();
                    String line;
                    ArrayList<String> lines = new ArrayList<>();
                    while (!(line = br.readLine()).equals("</PropertyList>")) {
                        if (!line.equals(""))
                            lines.add(line);
                    }
                    String longitude = lines.get(2);
                    String latitude = lines.get(3);
                    String[] x = longitude.split("[<>]");
                    String[] y = latitude.split("[<>]");
                    br.readLine();
                    out.println("get /instrumentation/heading-indicator/indicated-heading-deg");
                    out.flush();
                    String[] h = br.readLine().split(" ");
                    int tmp = h[3].length();
                    currentLocationX = Double.parseDouble(x[2]);
                    currentLocationY = Double.parseDouble(y[2]);
                    currentHeading = Double.parseDouble(h[3].substring(1, tmp - 1));
                    String[] data = {"plane", x[2], y[2], h[3].substring(1, tmp - 1)};
                    this.setChanged();
                    this.notifyObservers(data);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Objects.requireNonNull(socket).close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void findPath(int planeX, int planeY, int markX, int markY, int[][] data) {
        this.planeX = planeX;
        this.planeY = planeY;
        this.markX = markX;
        this.markY = markY;
        this.data = data;
        new Thread(() -> {
            int j, i;
            System.out.println("\tSending problem to the server...");
            for (i = 0; i < data.length; i++) {
                System.out.print("*");
                for (j = 0; j < data[i].length - 1; j++) {
                    outPath.print(data[i][j] + ",");
                }
                outPath.println(data[i][j]);
            }
            outPath.println("end");
            outPath.println(planeX + "," + planeY);
            outPath.println(markX + "," + markY);
            outPath.flush();
            String str = null;
            try {
                str = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\tSolution received");
            System.out.println(str);
            String[] tmp = Objects.requireNonNull(str).split(",");

            String[] notify = new String[tmp.length + 1];
            notify[0] = "path";
            for (i = 0; i < tmp.length; i++)
                notify[i + 1] = tmp[i];
            this.setChanged();
            this.notifyObservers(notify);
            mapModel.FindIntersections(tmp);
            mapModel.buildPlan();
            if(!route.isAlive())
                route.start();
            else if(Model.turn==false)
            {
                route.interrupt();
                route.start();
            }
        }).start();
    }

    private void rudderStart() {
        System.out.println(intersections);
        while (indexPlan < intersections.size()) {
            System.out.println("wowwwww");
            double heading, headingC;
            double tmp;
            heading = Integer.parseInt(intersections.get(indexPlan)[0]);
            headingC = currentHeading;
            int degree, degreeCom;
            degree = (int) (heading - headingC);
            if (degree < 0)
                degree += 360;
            degreeCom = 360 - degree;
            double turning;
            if (degree < degreeCom) {
                turning = joystickModel.turnPlus(headingC);
            } else {
                turning = joystickModel.turnMinus(headingC);
            }
            tmp = (turning - headingC);
            if (tmp >= 340)
                tmp = 360 - tmp;
            else if (tmp < -340)
                tmp = -360 - tmp;
            if (Math.abs(heading - headingC) > 9 && Math.abs(heading - headingC) < 349) {
                Parser.symbolTable.get("r").setValue(tmp / 20);
                Parser.symbolTable.get("e").setValue(0.095);
            } else {
                Parser.symbolTable.get("r").setValue(tmp / 100);
                Parser.symbolTable.get("e").setValue(0.053);
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void routeStart()
    {
        System.out.println("wowww2");
        while(turn) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        rudder.start();
        double intersectionX,intersectionY,pathX,pathY,endX,endY;
        pathX=startX+(planeY-1)*offset;
        pathY=startY-(planeX-1)*offset;
        endX=startX+(markY-1)*offset;
        endY=startY-(markX-1)*offset;
        int radiusX=17,radiusY=7;
        while(!turn && indexPlan<intersections.size()) {
            int h = Integer.parseInt(intersections.get(indexPlan)[0]);
            int n = Integer.parseInt(intersections.get(indexPlan)[1]);
            switch (h) {
                case 360:
                    intersectionX = pathX;
                    intersectionY = pathY + (n - 1) * offset;
                    break;
                case 45:
                    intersectionX = pathX + (n - 1) * offset;
                    intersectionY = pathY + (n - 1) * offset;
                    break;
                case 90:
                    intersectionX = pathX + (n - 1) * offset;
                    intersectionY = pathY;
                    break;
                case 135:
                    intersectionX = pathX + (n - 1) * offset;
                    intersectionY = pathY - (n - 1) * offset;
                    break;
                case 180:
                    intersectionX = pathX;
                    intersectionY = pathY - (n - 1) * offset;
                    ;
                    break;
                case 225:
                    intersectionX = pathX - (n - 1) * offset;
                    intersectionY = pathY - (n - 1) * offset;
                    break;
                case 270:
                    intersectionX = pathX - (n - 1) * offset;
                    intersectionY = pathY;
                    break;
                case 315:
                    intersectionX = pathX - (n - 1) * offset;
                    intersectionY = pathY + (n - 1) * offset;
                    break;
                default:
                    intersectionX = 0;
                    intersectionY = 0;
            }
            if(indexPlan==intersections.size()-1)
            {
                intersectionX=endX;
                intersectionY=endY;
                radiusY=20;
            }
            while (Math.abs(currentLocationX - intersectionX) >radiusX * offset || Math.abs(currentLocationY - intersectionY) > radiusX * offset) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            indexPlan++;
            pathX=currentLocationX;
            pathY=currentLocationY;

        }
        Parser.symbolTable.get("goal").setValue(1);

    }
}