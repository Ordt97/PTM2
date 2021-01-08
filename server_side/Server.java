package server_side;

public interface Server
{

    void start(int port, ClientHandler c);
    void stop();

}
