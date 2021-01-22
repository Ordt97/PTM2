package server_side;

public class Boot {
    public static void main(String[] args) {
        Server s = new MySerialServer();
        CacheManager cm = new FileCacheManager();
        MyClientHandler ch = new MyClientHandler(cm);
        s.start(2300, new ClientHandlerPath(ch));
    }
}
