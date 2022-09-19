import java.awt.*;

public enum MouseMovements {
    STRAIGHT(new Color(104, 170, 255)), DIAGONAL(new Color(255, 255, 0)),
    QUADRANT(new Color(233, 60, 233)), SEMICIRCLE(new Color(250, 60, 98));


    private MouseMovements(Color color) {
        this.color = color;
    }
    private Color color;

    public Color getColor() {
        return color;
    }
}
