package model;


import java.util.ArrayList;

public class MapModel {

    ArrayList<String[]> intersections = new ArrayList<>();

    protected void buildPlan() {
        intersections = new ArrayList<>();

        for (int i = 0; i < intersections.size(); i++) {
            int tmp = Integer.parseInt(intersections.get(i)[1]);
            if (tmp <= 5) {
                int index;
                int tmp2;
                if (i != 0) {
                    index = i - 1;
                } else {
                    index = i + 1;
                }
                tmp2 = Integer.parseInt(intersections.get(index)[1]);
                tmp2 += tmp;
                String[] strings = new String[]{intersections.get(index)[0], tmp2 + ""};
                intersections.set(index, strings);
                intersections.remove(i);
            }
        }
        for (int i = 0; i < intersections.size() - 1; i++) {
            if (intersections.get(i)[0].equals(intersections.get(i + 1)[0])) {
                int tmp = Integer.parseInt(intersections.get(i)[1]) + Integer.parseInt(intersections.get(i + 1)[1]);
                String s = "" + tmp;
                intersections.get(i)[1] = s;
                intersections.remove(i + 1);
            }
        }
        for (int i = 0; i < intersections.size(); i++) {
            String s = findDegree(i) + "";
            intersections.get(i)[0] = s.intern();
        }
    }

    public void FindIntersections(String[] solution) {
        int count = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            if (solution[i].equals(solution[i + 1])) {
                count++;
            } else {
                String[] tmp = new String[2];
                tmp[0] = solution[i];
                tmp[1] = count + 1 + "";
                intersections.add(tmp);
                count = 0;
            }
        }
        if (count != 0) {
            String[] tmp = new String[2];
            tmp[0] = solution[solution.length - 1];
            tmp[1] = count + 1 + "";
            intersections.add(tmp);
        }
    }

    private int findDegree(int index) {
        int tmp = Integer.parseInt(intersections.get(index)[1]);
        String direct = intersections.get(index)[0];
        int degree = this.calculateDegree(direct);
        if (tmp <= 15 && tmp > 5) {
            if (index + 1 < intersections.size()) {
                switch (degree) {
                    case 360:
                        if (this.calculateDegree(intersections.get(index + 1)[0]) == 90)
                            degree = 45;
                        else
                            degree -= 45;
                        break;
                    case 90:
                        if (this.calculateDegree(intersections.get(index + 1)[0]) == 360)
                            degree = 45;
                        else
                            degree -= 45;
                        break;
                    default:
                        if (degree < this.calculateDegree(intersections.get(index + 1)[0]))
                            degree += 45;
                        else
                            degree -= 45;
                }
            }
        }
        return degree;
    }

    private int calculateDegree(String s) {
        int degree = 0;
        switch (s) {
            case "Down":
                degree = 180;
                break;
            case "Right":
                degree = 90;
                break;
            case "Left":
                degree = 270;
                break;
            case "Up":
                degree = 360;
        }
        return degree;
    }
}