import java.awt.*;

public class MovementRange {

    private MouseMovements mouseMovement;
    private Point start;
    private Point end;


    public MovementRange(MouseMovements mouseMovement, Point start, Point end) {
        this.mouseMovement = mouseMovement;
        this.start = start;
        this.end = end;
    }

    public MouseMovements getMouseMovement() {
        return mouseMovement;
    }

    public void setMouseMovement(MouseMovements mouseMovement) {
        this.mouseMovement = mouseMovement;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "StartX: " + start.x + ", StartY: " + start.y + "\n"
                + "EndX: " + end.x + ", EndY:" + end.y + "\n"
                + "Move Type: " + mouseMovement.toString();
    }
}
