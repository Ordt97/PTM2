package server_side;

public class boot_server {
    public static void main(String[] args) {
        Server s = new MySerialServer();
        CacheManager cm = new FileCacheManager();
        MyClientHandler ch = new MyClientHandler(cm);
        s.start(2030, new FlightSimulatorClientHandler(ch));
    }
}
