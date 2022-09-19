import java.awt.*;
import java.util.*;
import java.util.List;

public class MazeSolver {

    public class DistanceHolderComparator implements Comparator<DistanceHolder> {
        @Override
        public int compare(DistanceHolder o1, DistanceHolder o2) {
            return o1.getDistance() - o2.getDistance();
        }
    }

    private class DistanceHolder {
        private Point parent;
        private Point node;
        int distance;

        private DistanceHolder(Point parent, Point node, int distance) {
            this.parent = parent;
            this.node = node;
            this.distance = distance;
        }

        public Point getParent() {
            return parent;
        }

        public void setParent(Point parent) {
            this.parent = parent;
        }

        public Point getNode() {
            return node;
        }

        public void setNode(Point node) {
            this.node = node;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

    }


    private List<Point> getNodeSuccessors(List<List<Boolean>> maze, int height, int maxWidth, Point node) {
        ArrayList<Point> successors = new ArrayList<>();

        if (node.x + 1 < maxWidth && !maze.get(node.y).get(node.x + 1)) //right
            successors.add(new Point(node.x + 1, node.y));
        if (node.x - 1 >= 0 && !maze.get(node.y).get(node.x - 1)) // left
            successors.add(new Point(node.x - 1, node.y));
        if (node.y + 1 < height && !maze.get(node.y + 1).get(node.x)) //up
            successors.add(new Point(node.x, node.y + 1));
        if (node.y - 1 >= 0 && !maze.get(node.y - 1).get(node.x)) // down
            successors.add(new Point(node.x, node.y - 1));
        if (node.x + 1 < maxWidth && node.y + 1 < height && !maze.get(node.y + 1).get(node.x + 1)) //upRight
        successors.add(new Point(node.x + 1, node.y + 1));
        if (node.x - 1 >= 0 && node.y + 1 < height && !maze.get(node.y + 1).get(node.x - 1)) //upLeft
            successors.add(new Point(node.x - 1, node.y + 1));
        if (node.x + 1 < maxWidth && node.y - 1 >= 0 && !maze.get(node.y - 1).get(node.x + 1)) //downRight
            successors.add(new Point(node.x + 1, node.y - 1));
        if (node.x - 1 >= 0 && node.y - 1 >= 0 && !maze.get(node.y - 1).get(node.x - 1)) //downLeft
            successors.add(new Point(node.x - 1, node.y - 1));

        return successors;
    }


    public List<Point> shortestPath(Maze maze) {
        int height = maze.getMaze().size();
        int maxWidth = 0;
        for (int i = 0; i < height; i++)
            maxWidth = Math.max(maze.getMaze().get(i).size(), maxWidth);

        List<List<Boolean>> mazeMatrix = maze.getMaze();
        Point startNode = maze.getStart();
        Point endNode = maze.getEnd();
        PriorityQueue<DistanceHolder> nodesQue = new PriorityQueue<>(new DistanceHolderComparator());
        //HashSet<Point> visited = new HashSet<>();
        HashMap<Point, DistanceHolder> disTable = new HashMap<>();

        for (int i = 0; i < mazeMatrix.size(); i++) {
            for (int j = 0; j < mazeMatrix.get(i).size(); j++) {
                if (!mazeMatrix.get(i).get(j)) {
                    Point point = new Point(j, i);
                    disTable.put(point, new DistanceHolder(null, point, Integer.MAX_VALUE));
                }
            }
        }
        disTable.get(startNode).setDistance(0);
        nodesQue.add(disTable.get(startNode));

        while (!nodesQue.isEmpty()) {
            DistanceHolder currentHolder = nodesQue.remove();

            //visited.add(currentHolder.getNode());
            List<Point> successors = getNodeSuccessors(mazeMatrix, height, maxWidth, currentHolder.getNode());
            for (Point successor : successors) {

                int dis = currentHolder.getDistance() + 1 + getDisFromTarget(successor, endNode);
                DistanceHolder successorDisHolder = disTable.get(successor);
                if (dis < successorDisHolder.getDistance()) {
                    successorDisHolder.setDistance(dis);
                    successorDisHolder.setParent(currentHolder.getNode());
                    nodesQue.remove(successorDisHolder);
                    nodesQue.add(successorDisHolder);
                }
            }
        }

        return getPath(disTable, endNode);

    }

    private List<Point> getPath(HashMap<Point, DistanceHolder> disTable, Point endNode) {

        ArrayList<Point> path = new ArrayList<>();
        DistanceHolder currDistanceHolder = disTable.get(endNode);
        while (currDistanceHolder.getParent() != null) {
            path.add(currDistanceHolder.getNode());
            currDistanceHolder = disTable.get(currDistanceHolder.getParent());
        }

        if (path.size() == 0) {
            return null;
        } else {
            ArrayList<Point> pathToReturn = new ArrayList<>();
            for (int i = path.size() - 1; i >= 0; i--)
                pathToReturn.add(path.get(i));

            return pathToReturn;
        }

    }

    private int getDisFromTarget(Point curr, Point target) {
        return (int) Math.sqrt(Math.pow(curr.x - target.x, 2) + Math.pow(curr.y - target.y, 2));
    }


    public HashMap<Point, MouseMovements> detectMouseMovements(List<Point> path) {
        if (path == null || path.size() <= 0)
            throw new IllegalArgumentException();

        ArrayList<MovementRange> movements = new ArrayList<>();
        ArrayList<MouseMovements> tempMovements = new ArrayList<>();
        tempMovements.add(MouseMovements.STRAIGHT);

        for (int i = 1; i < path.size(); i++) {
            switch (detectMovementType(i - 2 < 0 ? null : path.get(i - 2),
                    path.get(i - 1), path.get(i), tempMovements.get(i - 1))) {
                case STRAIGHT:
                    tempMovements.add(MouseMovements.STRAIGHT);
                    break;
                case DIAGONAL:
                    tempMovements.remove(i - 1);
                    tempMovements.remove(i - 2);
                    tempMovements.add(MouseMovements.DIAGONAL);
                    tempMovements.add(MouseMovements.DIAGONAL);
                    tempMovements.add(MouseMovements.DIAGONAL);
                    break;
                case QUADRANT:
                    tempMovements.remove(i - 1);
                    tempMovements.add(MouseMovements.QUADRANT);
                    tempMovements.add(MouseMovements.QUADRANT);
                    break;
                case SEMICIRCLE:
                    tempMovements.remove(i - 1);
                    tempMovements.remove(i - 2);
                    tempMovements.add(MouseMovements.SEMICIRCLE);
                    tempMovements.add(MouseMovements.SEMICIRCLE);
                    tempMovements.add(MouseMovements.SEMICIRCLE);
                    break;
            }
        }

        HashMap<Point, MouseMovements> mouseMovements = new HashMap<>();
        for (int i = 0; i < path.size(); i++)
            mouseMovements.put(path.get(i), tempMovements.get(i));

        return mouseMovements;
    }

    public List<MovementRange> detectMouseMovementsRanges(List<Point> path) {
        if (path == null || path.size() <= 0)
            throw new IllegalArgumentException();

        ArrayList<MovementRange> movements = new ArrayList<>();
        ArrayList<MouseMovements> tempMovements = new ArrayList<>();
        tempMovements.add(MouseMovements.STRAIGHT);

        for (int i = 1; i < path.size(); i++) {
            switch (detectMovementType(i - 2 < 0 ? null : path.get(i - 2),
                    path.get(i - 1), path.get(i), tempMovements.get(i - 1))) {
                case STRAIGHT:
                    tempMovements.add(MouseMovements.STRAIGHT);
                    break;
                case DIAGONAL:
                    tempMovements.remove(i - 1);
                    tempMovements.remove(i - 2);
                    tempMovements.add(MouseMovements.DIAGONAL);
                    tempMovements.add(MouseMovements.DIAGONAL);
                    tempMovements.add(MouseMovements.DIAGONAL);
                    break;
                case QUADRANT:
                    tempMovements.remove(i - 1);
                    tempMovements.add(MouseMovements.QUADRANT);
                    tempMovements.add(MouseMovements.QUADRANT);
                    break;
                case SEMICIRCLE:
                    tempMovements.remove(i - 1);
                    tempMovements.remove(i - 2);
                    tempMovements.add(MouseMovements.SEMICIRCLE);
                    tempMovements.add(MouseMovements.SEMICIRCLE);
                    tempMovements.add(MouseMovements.SEMICIRCLE);
                    break;
            }
        }

        Point currStartP;
        MouseMovements currMov;
        for (int i = 0; i < tempMovements.size(); i++) {
            currStartP = path.get(i);
            currMov = tempMovements.get(i);

            if (currMov == MouseMovements.STRAIGHT) {
                while (i < tempMovements.size() && tempMovements.get(i) == MouseMovements.STRAIGHT)
                    i++;

                i--;
                movements.add(new MovementRange(currMov, currStartP, new Point(path.get(i))));

            } else if (currMov == MouseMovements.DIAGONAL) {
                while (i < tempMovements.size() && tempMovements.get(i) == MouseMovements.DIAGONAL)
                    i++;

                i--;
                movements.add(new MovementRange(currMov, currStartP, new Point(path.get(i))));

            } else if (currMov == MouseMovements.QUADRANT) {
                i++;
                movements.add(new MovementRange(currMov, currStartP, new Point(path.get(i))));
            } else {
                i += 2;
                movements.add(new MovementRange(currMov, currStartP, new Point(path.get(i))));
            }

        }

        return movements;

    }

    private MouseMovements detectMovementType(Point p0, Point p1, Point p2, MouseMovements p1Type) {
        if (p1.x - p2.x == 0 || p1.y - p2.y == 0) {
            return MouseMovements.STRAIGHT;
        } else if (p1Type == MouseMovements.STRAIGHT) {
            return MouseMovements.QUADRANT;
        } else {
            if (p0.x == p2.x || p0.y == p2.y)
                return MouseMovements.SEMICIRCLE;
            else
                return MouseMovements.DIAGONAL;
        }
    }

}
