package server_side;

public interface CacheManager<Problem, Solution> {
    Boolean Check(Problem p);

    Solution getSolution(Problem p);

    void Save(Problem p, Solution s);
}
