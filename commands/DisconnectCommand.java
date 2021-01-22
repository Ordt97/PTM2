package commands;

public class DisconnectCommand extends ConnectCommand {

    @Override
    public void doCommand(String[] args) {
        OpenDataServerCommand.stop = true;
        ConnectCommand.stop = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Someone interrupt us to sleep");
        }
    }
}
