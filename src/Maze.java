import java.awt.*;
import java.util.List;
import java.util.Vector;

public class Maze {

    private Point start;
    private Point end;
    private List<List<Boolean>> maze;

    public Maze(Point start, Point end, List<List<Boolean>> maze) {
        this.start = start;
        this.end = end;
        this.maze = maze;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        if (start == null)
            throw new IllegalArgumentException();

        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        if (end == null)
            throw new IllegalArgumentException();

        this.end = end;
    }

    public List<List<Boolean>> getMaze() {
        return maze;
    }

    public void setMaze(List<List<Boolean>> maze) {
        if (maze == null)
            throw new IllegalArgumentException();

        this.maze = maze;
    }

}
