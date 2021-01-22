package server_side;

import java.io.InputStream;
import java.io.OutputStream;

public class ClientHandlerPath implements ClientHandler {
    MyClientHandler myClientHandler;
    public static volatile boolean stop = false;

    public ClientHandlerPath(MyClientHandler myClientHandler) {
        this.myClientHandler = myClientHandler;
    }

    @Override
    public void handleClient(InputStream in, OutputStream out) {

        while (!stop) {
            myClientHandler.handleClient(in, out);
        }
    }
}
