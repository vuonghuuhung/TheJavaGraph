package application;

import javafx.scene.control.Label;
import javafx.scene.shape.Shape;


public class Edge {


    public Node source, target;
    public double weight;
    public Shape line;
    public Label weightLabel;
    public Edge(Node source, Node target, double weight, Shape line, Label weiLabel) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.line = line;
        this.weightLabel = weiLabel;
    }

}