package application;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class DFS extends CanvasController {
    int time = DefineGraph.cref.time;
    SequentialTransition st = DefineGraph.cref.st;

    public DFS(Node source) {
        source.minDistance = 0;
        source.visited = true;
        DFSRecursion(source, 0);

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
            FillTransition ft1 = new FillTransition(Duration.millis(time), source.circle);
            ft1.setToValue(Color.RED);
            ft1.play();
            Image image = new Image(getClass().getResourceAsStream("/image/hiPlay.png"));
            DefineGraph.cref.getPlayPauseImage().setImage(image);
            DefineGraph.cref.paused = true;
            DefineGraph.cref.playing = false;
            textFlow.appendText("---Finished--\n");
        });
        st.onFinishedProperty();
        st.play();
        DefineGraph.cref.playing = true;
        DefineGraph.cref.paused = false;

    }

    public void DFSRecursion(Node source, int level) {
        FillTransition ft = new FillTransition(Duration.millis(time), source.circle);
        if (source.circle.getFill() == Color.BLACK) {
            ft.setToValue(Color.FORESTGREEN);
        }
        st.getChildren().add(ft);
        String str = "";
        for (int i = 0; i < level; i++) {
            str = str.concat("\t");
        }
        str = str.concat("DFS(" + source.name + ") Enter\n");
        final String str2 = str;
        FadeTransition fd = new FadeTransition(Duration.millis(10), textFlow);
        fd.setOnFinished(e -> {
            textFlow.appendText(str2);
        });
        fd.onFinishedProperty();
        st.getChildren().add(fd);
        for (Edge e : source.adjacents) {
            if (e != null) {
                Node v = e.target;
                if (!v.visited) {
                    v.minDistance = source.minDistance + 1;
                    v.visited = true;
                    v.previous = source;
                    if (DefineGraph.undirected) {
                        StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), e.line);
                        ftEdge.setToValue(Color.FORESTGREEN);
                        st.getChildren().add(ftEdge);
                    } else if (DefineGraph.directed) {
                        FillTransition ftEdge = new FillTransition(Duration.millis(time), e.line);
                        ftEdge.setToValue(Color.FORESTGREEN);
                        st.getChildren().add(ftEdge);
                    }
                    DFSRecursion(v, level + 1);
                    if (DefineGraph.undirected) {
                        StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), e.line);
                        ftEdge.setToValue(Color.BLUEVIOLET);
                        st.getChildren().add(ftEdge);
                    } else if (DefineGraph.directed) {
                        FillTransition ftEdge = new FillTransition(Duration.millis(time), e.line);
                        ftEdge.setToValue(Color.BLUEVIOLET);
                        st.getChildren().add(ftEdge);
                    }
                    FillTransition ft1 = new FillTransition(Duration.millis(time), v.circle);
                    ft1.setToValue(Color.BLUEVIOLET);
                    ft1.onFinishedProperty();
                    ft1.setOnFinished(ev -> {
                        v.circle.distance.setText("Dist. : " + v.minDistance);
                    });
                    st.getChildren().add(ft1);
                }
            }
        }
        str = "";
        for (int i = 0; i < level; i++) {
            str = str.concat("\t");
        }
        str = str.concat("DFS(" + source.name + ") Exit\n");
        final String str1 = str;
        fd = new FadeTransition(Duration.millis(10), textFlow);
        fd.setOnFinished(e -> {
            textFlow.appendText(str1);
        });
        fd.onFinishedProperty();
        st.getChildren().add(fd);
    }
}
