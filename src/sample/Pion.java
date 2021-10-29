package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pion extends Circle
{
    private Color color;

    public Pion(int x, int y,Color color)
    {
        super(35, color);
        this.setCenterX(x);
        this.setCenterY(y);
        this.color = color;

    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
