package client_side;

import java.util.HashMap;
import java.util.Map;

public class GenericFactory<Command> {

    private interface Creator<Command> {
        Command create();
    }

    Map<String, Creator<Command>> map;

    public GenericFactory() {
        map = new HashMap<>();
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

    public Command createCommand(String key){
        return map.containsKey(key) ? map.get(key).create() : null;
    }

    public Command getNewCommand(String key) {
        if (map.containsKey(key))
            return map.get(key).create();
        return null;
    }
}
