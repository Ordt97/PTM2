package client_side;

import java.util.HashMap;
import java.util.Map;

public class GenericFactory<Command> {

    Map<String, Creator<Command>> map;

    private interface Creator<Command> {
        Command create();
    }

    public GenericFactory() {
        map = new HashMap<>();
    }

    public Command getNewCommand(String key) {
        if (map.containsKey(key))
            return map.get(key).create();
        return null;
    }

    public void insertCommand(String key, Class<? extends Command> command) {
        map.put(key, () -> {
            try {
                return command.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
