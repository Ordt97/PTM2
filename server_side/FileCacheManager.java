package server_side;

import java.util.HashMap;

public class FileCacheManager<Problem, Solution> implements CacheManager<Problem, Solution> {

    HashMap<Problem, Solution> map;

    public FileCacheManager() {
        map=new HashMap<>();
    }

    @Override
    public Boolean Check(Problem p) {
        if (map.isEmpty())
            return false;
        return map.containsKey(p);

    }

    @Override
    public Solution getSolution(Problem p) {
        return map.get(p);
    }

    @Override
    public void Save(Problem p, Solution s) {
        map.put(p, s);
    }
}
