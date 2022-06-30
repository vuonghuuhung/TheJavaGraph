package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class TopSort extends CanvasController {
    StackPane stackRoot = DefineGraph.cref.getStackRoot();
    AnchorPane anchorRoot = DefineGraph.cref.getAnchorRoot();
    List<Shape> edges = DefineGraph.cref.edges;
    List<NodeFX> circles = DefineGraph.cref.circles;
    SequentialTransition st= DefineGraph.cref.st;
    int times = DefineGraph.cref.time;
    String reverse = "";
    List<String> topSort = new ArrayList<>();
    boolean cycleFound = false;

    public TopSort() {
        cycleFound = false;
        for (NodeFX n : circles) {
            if (n.node.DAGColor == 0) {
                cycleExists(n.node, 0);
            }
        }
        if (cycleFound == false) {
            for (NodeFX source : circles) {
                if (source.node.visited == false) {
                    topsortRecursion(source.node, 0, times);
                }
            }

            System.out.println("Hello World " + topSort);
            Collections.reverse(topSort);
            for (String s : topSort) {
                reverse += " -> " + s;
            }
            reverse = reverse.replaceFirst(" -> ", "");
            System.out.println(reverse);

        
            st.setOnFinished(ev -> {
                for (NodeFX n : DefineGraph.cref.circles) {
                    FillTransition ft1 = new FillTransition(Duration.millis(time), n);
                    ft1.setToValue(Color.BLACK);
                    ft1.play();
                }
                if (DefineGraph.cref.directed) {
                    for (Shape n : DefineGraph.cref.edges) {
                        n.setFill(Color.BLACK);
                    }
                } else if (DefineGraph.cref.undirected) {
                    for (Shape n : DefineGraph.cref.edges) {
                        n.setStroke(Color.BLACK);
                    }
                }
                Image image = new Image(getClass().getResourceAsStream("/image/hiPlay.png"));
                DefineGraph.cref.getPlayPauseImage().setImage(image);
                DefineGraph.cref.paused = true;
                DefineGraph.cref.playing = false;
                textFlow.appendText("---Finished--\n\n");
                textFlow.appendText("Top Sort: " + reverse + "\n");
            });
            st.onFinishedProperty();
            st.play();
            DefineGraph.cref.playing = true;
            DefineGraph.cref.paused = false;

        } else {
            System.out.println("Cycle");
            BoxBlur blur = new BoxBlur(3, 3, 3);

            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            dialogLayout.setStyle("-fx-background-color:#dfe6e9");
            JFXDialog dialog = new JFXDialog(stackRoot, dialogLayout, JFXDialog.DialogTransition.TOP);

            JFXButton button = new JFXButton("OK");
            button.setPrefSize(50, 30);
            button.getStyleClass().add("dialog-button");
            button.setButtonType(JFXButton.ButtonType.RAISED);
            dialogLayout.setActions(button);
            Label message = new Label("     Cycle Detected!\n" + "Cannot run TopSort on a  Directed Cyclic Graph!");
            message.setId("message");
            dialogLayout.setBody(message);
            button.setOnAction(e -> {
                dialog.close();
                anchorRoot.setEffect(null);
            });
            dialog.setOnDialogClosed(e -> {
                stackRoot.toBack();
                anchorRoot.setEffect(null);
            });

            stackRoot.toFront();
            dialog.toFront();
            dialog.show();
            anchorRoot.setEffect(blur);
            dialogLayout.setPadding(new Insets(0, 0, 0, 0));
        }
    }

    void cycleExists(Node source, int level) {
        source.DAGColor = 1;
        for (Edge e : source.adjacents) {
            if (e != null) {
                Node v = e.target;
                if (v.DAGColor == 1) {
                    cycleFound = true;
                } else if (v.DAGColor == 0) {
                    v.previous = source;
                    cycleExists(v, level + 1);
                }
            }
        }
        source.DAGColor = 2;
    }

    public void topsortRecursion(Node source, int level, int times) {
        FillTransition ft = new FillTransition(Duration.millis(10), source.circle);
        if (source.circle.getFill() == Color.BLACK) {
            ft.setToValue(Color.FORESTGREEN);
        }
        st.getChildren().add(ft);

        String str = "";
        for (int i = 0; i < level; i++) {
            str = str.concat("\t");
        }
        str = str.concat("Recursion(" + source.name + ") Enter\n");
        final String str2 = str;
        FadeTransition fd = new FadeTransition(Duration.millis(10), textFlow);
        fd.setOnFinished(e -> {
            textFlow.appendText(str2);
        });
        fd.onFinishedProperty();
        st.getChildren().add(fd);
        source.visited = true;
        for (Edge e : source.adjacents) {
            if (e != null) {
                Node v = e.target;
                if (!v.visited) {
                    v.minDistance = source.minDistance + 1;
                    v.previous = source;
                    if (isUndirected()) {
                        StrokeTransition ftEdge = new StrokeTransition(Duration.millis(10), e.line);
                        ftEdge.setToValue(Color.FORESTGREEN);
                        st.getChildren().add(ftEdge);
                    } else if (isDirected()) {
                        FillTransition ftEdge = new FillTransition(Duration.millis(10), e.line);
                        ftEdge.setToValue(Color.FORESTGREEN);
                        st.getChildren().add(ftEdge);
                    }
                    topsortRecursion(v, level + 1, times);
                    if (isUndirected()) {
                        StrokeTransition ftEdge = new StrokeTransition(Duration.millis(times), e.line);
                        ftEdge.setToValue(Color.BLUEVIOLET);
                        st.getChildren().add(ftEdge);
                    } else if (isDirected()) {
                        FillTransition ftEdge = new FillTransition(Duration.millis(times), e.line);
                        ftEdge.setToValue(Color.BLUEVIOLET);
                        st.getChildren().add(ftEdge);
                    }
                    FillTransition ft1 = new FillTransition(Duration.millis(times), v.circle);
                    ft1.setToValue(Color.BLUEVIOLET);
                    ft1.onFinishedProperty();
                    st.getChildren().add(ft1);
                }
            }
        }
        str = "";
        for (int i = 0; i < level; i++) {
            str = str.concat("\t");
        }
        topSort.add(source.name);

        str = str.concat("Recursion(" + source.name + ") Exit\n");
        final String str1 = str;
        fd = new FadeTransition(Duration.millis(10), textFlow);
        fd.setOnFinished(e -> {
            textFlow.appendText(str1);
        });
        fd.onFinishedProperty();
        st.getChildren().add(fd);
    }
}