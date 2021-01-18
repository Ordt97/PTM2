package server_side;

public interface CacheManager<Problem, Solution> {
    Boolean Check(Problem in);

    Solution Extract(Problem in);

    void Save(Problem in, Solution out);
}
