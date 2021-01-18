package commands;

public class DisconnectCommand implements Command {
    @Override
    public void doCommand(String[] args) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OpenDataServerCommand.stop = true;
        ConnectCommand.stop = true;
    }
}
