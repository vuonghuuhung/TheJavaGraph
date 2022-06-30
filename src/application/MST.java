package application;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MST extends CanvasController {
    List<Edge> mstEdges = DefineGraph.cref.mstEdges;
    int times = DefineGraph.cref.time;
    List<NodeFX> circles = DefineGraph.cref.circles;
    SequentialTransition st = DefineGraph.cref.st;
    int mstValue = 0;

    public Node findParent(Node x) {
        if (x == x.previous) {
            return x;
        }
        x.previous = findParent(x.previous);
        return x.previous;
    }

    public void unionNode(Node x, Node y) {
        Node px = findParent(x);
        Node py = findParent(y);
        if (px == py) {
            return;
        }
        if (Integer.valueOf(px.name) < Integer.valueOf(py.name)) {
            px.previous = py;
        } else {
            py.previous = px;
        }
    }

    public MST() {
        // set up tìm cha của nột nút
        for (NodeFX x : circles) {
            x.node.previous = x.node;
        }

        for (NodeFX x : circles) {
            final String s = "Node : " + x.node.name + " , Parent: " + x.node.previous.name + "\n";
            FadeTransition fd = new FadeTransition(Duration.millis(times), textFlow);
            fd.setOnFinished(e -> {
                textFlow.appendText(s);
            });
            fd.onFinishedProperty();
            st.getChildren().add(fd);
        }
        final String s = "Start Algorithm :---\n";
        FadeTransition fdss = new FadeTransition(Duration.millis(times), textFlow);
        fdss.setOnFinished(ev -> {
            textFlow.appendText(s);
        });
        fdss.onFinishedProperty();
        st.getChildren().add(fdss);

        // sap xep lai cac cung theo thu tu tang dan cua trong so cac cung
        Collections.sort(mstEdges, new Comparator<Edge>() {
            public int compare(Edge o1, Edge o2) {
                if (o1.weight == o2.weight) {
                    return 0;
                }
                return o1.weight > o2.weight ? 1 : -1;
            }
        });

        for (Edge e : mstEdges) {
            // hieu ung dao dong giua 2 mau trong thoi gian times
            StrokeTransition ft0 = new StrokeTransition(Duration.millis(times), e.line);
            ft0.setToValue(Color.DARKORANGE);
            st.getChildren().add(ft0);
            // cung moi duoc them vao hien thi
            final String se = "Selected Edge:- (" + e.source.name.trim() + "--" + e.target.name.trim() + ") Weight: "
                    + String.valueOf(e.weight) + " \n" + "\t-> Node :" + e.source.name.trim() + "  Parent: "
                    + "\t-> Node :" + e.target.name.trim() + "  Parent: " + findParent(e.target.previous).name.trim()
                    + "\n";

            // hieu ung lam mo khi hien textflow trong hiddenpane
            FadeTransition fdx = new FadeTransition(Duration.millis(10), textFlow);
            fdx.setOnFinished(evx -> {
                textFlow.appendText(se);
            });
            fdx.onFinishedProperty();
            st.getChildren().add(fdx);

            if (findParent(e.source.previous) != findParent(e.target.previous)) {
                unionNode(e.source, e.target);
                mstValue += e.weight;

                final String sa = "\t---->Unioned\n" + "\t\t->Node :" + e.source.name.trim() + "  Parent: "
                        + findParent(e.source.previous).name.trim() + "\n" + "\t\t->Node :" + e.target.name.trim()
                        + "  Parent: " + findParent(e.target.previous).name.trim() + "\n";

                FadeTransition ft1 = new FadeTransition(Duration.millis(times), textFlow);
                ft1.setOnFinished(evx -> {
                    textFlow.appendText(sa);
                });
                ft1.onFinishedProperty();
                st.getChildren().add(ft1);

                StrokeTransition ft2 = new StrokeTransition(Duration.millis(times), e.line);
                ft2.setToValue(Color.DARKGREEN);
                st.getChildren().add(ft2);
                // hieu ung chuyen tu mau nay sang may khac
                FillTransition ft3 = new FillTransition(Duration.millis(times), e.source.circle);
                ft3.setToValue(Color.AQUA);
                st.getChildren().add(ft3);

                ft3 = new FillTransition(Duration.millis(times), e.target.circle);
                ft3.setToValue(Color.AQUA);
                st.getChildren().addAll(ft3);
            } else {
                final String sa = "\t---->Cycle Detected\n";
                FadeTransition ft = new FadeTransition(Duration.millis(10), textFlow);
                ft.setOnFinished(evx -> {
                    textFlow.appendText(sa);
                });
                ft.onFinishedProperty();
                st.getChildren().add(ft);
                StrokeTransition st2 = new StrokeTransition(Duration.millis(times), e.line);
                st2.setToValue(Color.DARKRED);
                st.getChildren().add(st2);

                st2 = new StrokeTransition(Duration.millis(times), e.line);
                st2.setToValue(Color.web("#E0E0E0"));
                st.getChildren().add(st2);

            }
        }
        st.setOnFinished(ev -> {
            Image image = new Image(getClass().getResourceAsStream("/image/hiPlay.png"));
            DefineGraph.cref.getPlayPauseImage().setImage(image);
            DefineGraph.cref.paused = true;
            DefineGraph.cref.playing = false;
            textFlow.appendText("Minimum Cost of the Graph " + mstValue);
        });
        st.onFinishedProperty();
        st.play();
        DefineGraph.cref.playing = true;
        // </editor-fold>
        System.out.println("" + mstValue);
    }
}