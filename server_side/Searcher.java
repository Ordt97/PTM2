package server_side;

public interface Searcher<Solution> {
    Solution search(Searchable s);
}
