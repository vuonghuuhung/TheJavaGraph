package application;


import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Light.Point;
import javafx.scene.shape.Circle;

public class NodeFX extends Circle {

    Node node;
    Point point;
    Label distance;
    Label visitTime;
    Label lowTime;
    Label id= new Label();
    boolean isSelected = false;

    public NodeFX(double x, double y, double rad, String name) {
        super(x, y, rad);
        distance = new Label("Dist. : INFINITY");
        visitTime = new Label("Visit : 0");
        lowTime = new Label("Low : 0");
        id = new Label();
        node = new Node(name, this);
        point = new Point((int) x, (int) y, rad, null);
        id.setText(name);

        id.setLayoutX(x - 18);
        id.setLayoutY(y - 18);
        this.setOpacity(0.5);
        this.setBlendMode(BlendMode.MULTIPLY);
        this.setId("node");
    }
}
