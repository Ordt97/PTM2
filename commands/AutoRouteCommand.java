package commands;

import model.Model;

public class AutoRouteCommand implements Command {
    @Override
    public void doCommand(String[] args) {
        Model.turn = false;
    }
}
