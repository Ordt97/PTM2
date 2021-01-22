package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.ArrayList;

public class MyClientHandler implements ClientHandler {
    CacheManager cm;
    Solver solver;

    public MyClientHandler(CacheManager cm) {
        this.cm = cm;
    }

    @Override
    public void handleClient(InputStream in, OutputStream out) {
        BufferedReader Bin = new BufferedReader(new InputStreamReader(in));
        PrintWriter Bout = new PrintWriter(new OutputStreamWriter(out));
        try {
            String Line;
            StringBuilder Solved;
            ArrayList<String[]> lines = new ArrayList<>();
            try {
                while (!(Line = Bin.readLine()).equals("end")) {
                    lines.add(Line.split(","));
                }
            } catch (SocketException | NullPointerException e) {
                System.out.println("Client left");
                System.exit(1);
            }
            int j = 0;
            int[][] mat = new int[lines.size()][];
            for (int i = 0; i < mat.length - 1; i++) {
                String[] line = lines.get(i);
                mat[i] = new int[line.length];
                for (String s : line) {
                    mat[i][j] = Integer.parseInt(s);
                    j++;
                }
                j = 0;
            }
            Matrix m = new Matrix(mat);
            BestFirstSearch.Heuristic heuristic = getHeuristic();
            Searcher searcher = new BestFirstSearch(heuristic);
            solver = new SolverSearcher<>(searcher);
            m.setInitState(Bin.readLine());
            m.setDestinationState(Bin.readLine());
            if (cm.Check(m.toString())) {
                Solved = new StringBuilder((String) cm.getSolution(m.toString()));
            } else {
                Solved = getSolved(m);
            }
            Bout.println(Solved.substring(0, Solved.length() - 1));
            Bout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BestFirstSearch.Heuristic getHeuristic() {
        BestFirstSearch.Heuristic heuristic = (s, goalState) -> {
            String start = (String) (s.getState());
            String[] split = start.split(",");
            double x1 = Integer.parseInt(split[0]);
            double y1 = Integer.parseInt(split[1]);
            String end = (String) goalState.getState();
            split = end.split(",");
            double x2 = Integer.parseInt(split[0]);
            double y2 = Integer.parseInt(split[1]);
            return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        };
        return heuristic;
    }

    private StringBuilder getSolved(Matrix m) {
        StringBuilder Solved;
        Solved = new StringBuilder((String) solver.Solve(m));
        String[] arrows = Solved.toString().split("->");
        Solved = new StringBuilder();
        String[] arrow1;
        String[] arrow2;
        int x, y;
        for (int i = 0; i < arrows.length - 1; i++) {
            arrow1 = arrows[i].split(",");
            arrow2 = arrows[i + 1].split(",");
            x = Integer.parseInt(arrow2[0]) - Integer.parseInt(arrow1[0]);
            y = Integer.parseInt(arrow2[1]) - Integer.parseInt(arrow1[1]);
            if (x > 0)
                Solved.append("Down" + ",");
            else if (x < 0)
                Solved.append("Up" + ",");
            else if (y > 0)
                Solved.append("Right" + ",");
            else
                Solved.append("Left" + ",");

        }
        cm.Save(m.toString(), Solved.toString());
        return Solved;
    }
}
