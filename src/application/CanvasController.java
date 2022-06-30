package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.HiddenSidesPane;

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class CanvasController implements Initializable, ChangeListener<Object> {
	// Attributes
    @FXML
    private HiddenSidesPane hiddenPane;
    @FXML
    private  AnchorPane anchorRoot;
    @FXML
    private  StackPane stackRoot;
    @FXML
    private JFXButton canvasBackButton, clearButton, resetButton, playPauseButton;
    @FXML
    private JFXToggleButton addNodeButton, addEdgeButton, bfsButton, dfsButton, topSortButton, dijkstraButton,
            mstButton;
    @FXML
    private ToggleGroup algoToggleGroup;
    @FXML
    private Pane viewer;
    @FXML
    private Group canvasGroup;
    @FXML
    private Line edgeLine;
    @FXML
    private Label sourceText = new Label("Source"), weight; 
    @FXML
    private Pane border;
    @FXML
    private Arrow arrow;
    @FXML
    private JFXNodesList nodeList;
    @FXML
    private JFXSlider slider = new JFXSlider();
    @FXML
    private ImageView playPauseImage, openHidden;
    boolean menuBool = false;
    ContextMenu globalMenu;
    int nNode = 0, time = 500;
    NodeFX selectedNode = null;
    List<NodeFX> circles = new ArrayList<>();
    List<Edge> mstEdges = new ArrayList<>(), realEdges = new ArrayList<>();
    List<Shape> edges = new ArrayList<>();
    boolean addNode = true, addEdge = false, calculate = false, calculated = false, playing = false, paused = false,
            pinned = false;
    List<Label> distances = new ArrayList<Label>();
    boolean weighted = DefineGraph.weighted, unweighted = DefineGraph.unweighted,
            directed = DefineGraph.directed, undirected = DefineGraph.undirected, bfs = true, dfs = true,
            dijkstra = true, mst = true, topSortBool = true;
    Algorithm algo = new Algorithm();
    public SequentialTransition st ;
    public AnchorPane hiddenRoot = new AnchorPane();
    public static TextArea textFlow = new TextArea();
    public ScrollPane textContainer = new ScrollPane();
    // End Attributes
    
    // setter and getter
    public boolean isDirected() {
    	return directed;
    }
    public boolean isUndirected() {
    	return undirected;
    }
    public void setCanvasGroup(Group canvasGroup) {
        this.canvasGroup = canvasGroup;
    }
    public Label getSourceText() {
        return sourceText;
    }
    public Label getWeight() {
        return weight;
    }
    public Pane getBorder() {
        return border;
    }
    public Arrow getArrow() {
        return arrow;
    }
    public ContextMenu getGlobalMenu() {
        return globalMenu;
    }
    public void setGlobalMenu(ContextMenu globalMenu) {
        this.globalMenu = globalMenu;
    }
    public boolean isPaused() {
        return paused;
    }
    public StackPane getStackRoot() {
        return stackRoot;
    }
    public AnchorPane getAnchorRoot() {
        return anchorRoot;
    }
    public List<Label> getDistances() {
        return distances;
    }
    public ImageView getPlayPauseImage() {
        return playPauseImage;
    }
    public ScrollPane getTextContainer() {
        return textContainer;
    }

    public void setTextContainer(ScrollPane textContainer) {
        this.textContainer = textContainer;
    }
    // End setter and getter

    // Bộ khởi tạo controller, phương thức này sẽ được thực thi sau khi các đối tượng FXML đã được thêm vào trong controller
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hiddenPane.setContent(canvasGroup);

        viewer.prefHeightProperty().bind(border.heightProperty());
        viewer.prefWidthProperty().bind(border.widthProperty());

        addEdgeButton.setDisable(true);
        addNodeButton.setDisable(true);
        clearButton.setDisable(true);
        
        if (nNode == 0) {
        	bfsButton.setDisable(true);
            dfsButton.setDisable(true);
            dijkstraButton.setDisable(true);
            mstButton.setDisable(true);
            topSortButton.setDisable(true);
        }

        if (weighted) {
            bfsButton.setDisable(true);
            dfsButton.setDisable(true);
        }

        if (unweighted) {
            dijkstraButton.setDisable(true);
            mstButton.setDisable(true);
        }
        
        if (undirected) {
        	topSortButton.setDisable(true);
        }

        canvasBackButton.setOnAction(e -> {
            try {
                ResetHandle(null);
                Parent root = FXMLLoader.load(getClass().getResource("DefineGraph.fxml"));

                Scene scene = new Scene(root);
                Main.primaryStage.setScene(scene);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        slider = new JFXSlider(10, 1000, 500);
        slider.setPrefWidth(150);
        slider.setPrefHeight(80);
        slider.setSnapToTicks(true);
        slider.setMinorTickCount(100);
        slider.setIndicatorPosition(JFXSlider.IndicatorPosition.RIGHT);
        slider.setBlendMode(BlendMode.MULTIPLY);
        slider.setCursor(Cursor.CLOSED_HAND);
        nodeList.addAnimatedNode(slider);
        nodeList.setSpacing(50D);
        nodeList.setRotate(270D);
        slider.toFront();
        nodeList.toFront();
        slider.valueProperty().addListener(this);

        hiddenRoot.setPrefWidth(300);
        hiddenRoot.setPrefHeight(512);

        Label detailLabel = new Label("Detail");
        detailLabel.setPrefSize(hiddenRoot.getPrefWidth() - 20, 38);
        detailLabel.setAlignment(Pos.CENTER);
        detailLabel.setFont(new Font("Roboto", 20));
        detailLabel.setPadding(new Insets(7, 40, 3, -10));
        detailLabel.setStyle("-fx-background-color: #dcdde1;");
        detailLabel.setLayoutX(35);// ?????????????

        textFlow.setPrefSize(hiddenRoot.getPrefWidth(), hiddenRoot.getPrefHeight() - 2);
        textFlow.setStyle("-fx-background-color: #dfe6e9;");
        textFlow.setLayoutY(39);
        textContainer.setLayoutY(textFlow.getLayoutY());
        textFlow.setPadding(new Insets(5, 0, 0, 5));
        textFlow.setEditable(false);
        textContainer.setContent(textFlow);

        JFXButton pinUnpin = new JFXButton();
        pinUnpin.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        ImageView imgPin = new ImageView(new Image(getClass().getResourceAsStream("/image/pinned.png")));
        imgPin.setFitHeight(20);
        imgPin.setFitWidth(20);
        ImageView imgUnpin = new ImageView(new Image(getClass().getResourceAsStream("/image/unpinned.png")));
        imgUnpin.setFitHeight(20);
        imgUnpin.setFitWidth(20);
        pinUnpin.setGraphic(imgPin);

        pinUnpin.setPrefSize(20, 39);
        pinUnpin.setButtonType(JFXButton.ButtonType.FLAT);
        pinUnpin.setStyle("-fx-background-color: #dcdde1;");
        pinUnpin.setOnMouseClicked(e -> {
            if (pinned) {
                pinUnpin.setGraphic(imgPin);
                hiddenPane.setPinnedSide(null);
                pinned = false;
            } else {
                pinUnpin.setGraphic(imgUnpin);
                hiddenPane.setPinnedSide(Side.RIGHT);
                pinned = true;
            }
        });

        hiddenRoot.getChildren().addAll(pinUnpin, detailLabel, textContainer);
        hiddenPane.setRight(hiddenRoot);
        hiddenRoot.setOnMouseEntered(e -> {
            hiddenPane.setPinnedSide(Side.RIGHT);
            openHidden.setVisible(false);
            e.consume();
        });
        hiddenRoot.setOnMouseExited(e -> {
            if (!pinned) {
                hiddenPane.setPinnedSide(null);
                openHidden.setVisible(true);
            }
            e.consume();
        });
        hiddenPane.setTriggerDistance(60);
    }

    @Override
    public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
        int temp = (int) slider.getValue();

        if (temp > 500) {
            int diff = temp - 500;
            temp = 500;
            temp -= diff;
            temp += 10;
        } else if (temp < 500) {
            int diff = 500 - temp;
            temp = 500;
            temp += diff;
            temp -= 10;
        }
        time = temp;
        System.out.println(time);
    }

    @FXML
    public void handle(MouseEvent ev) {
        if (addNode) {
            if (nNode == 1) {
                addNodeButton.setDisable(false);
            }
            if (nNode == 2) {
                addEdgeButton.setDisable(false);
                AddNodeHandle(null);
            }

            if (!ev.getSource().equals(canvasGroup)) {
                if (ev.getEventType() == MouseEvent.MOUSE_RELEASED && ev.getButton() == MouseButton.PRIMARY) {
                    if (menuBool) {
                        System.out.println("here" + ev.getEventType());
                        menuBool = false;
                        return;
                    }
                    nNode++;
                    NodeFX circle = new NodeFX(ev.getX(), ev.getY(), 1.2, String.valueOf(nNode));

                    canvasGroup.getChildren().add(circle.id);
                    circle.id.setLayoutX(ev.getX() - 18);
                    circle.id.setLayoutY(ev.getY() - 18);
                    circle.setOpacity(0.5);
                    circle.setBlendMode(BlendMode.MULTIPLY);
                    circle.setId("node");

                    RightClickMenu rt = new RightClickMenu(circle);
                    ContextMenu menu = rt.getMenu();
                    globalMenu = menu;
                    circle.setOnContextMenuRequested(e -> {
                        if (addEdge || addNode) {
                            menu.show(circle, e.getScreenX(), e.getScreenY());
                            menuBool = true;
                        }
                    });
                    menu.setOnAction(e -> {
                        menuBool = false;
                    });

                    System.out.println("ADDing: " + circles.size());
                    circles.add(circle);
                    canvasGroup.getChildren().add(circle);

                    circle.setOnMousePressed(mouseHandler);
                    circle.setOnMouseReleased(mouseHandler);
                    circle.setOnMouseDragged(mouseHandler);
                    circle.setOnMouseExited(mouseHandler);
                    circle.setOnMouseEntered(mouseHandler);

                    ScaleTransition tr = new ScaleTransition(Duration.millis(100), circle);
                    tr.setByX(10f);
                    tr.setByY(10f);
                    tr.play();

                }
            }
        }
    }

    public boolean edgeExists(NodeFX u, NodeFX v) {
        for (Edge e : realEdges) {
            if (e.source == u.node && e.target == v.node) {
                return true;
            }
        }
        return false;
    }

    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            NodeFX circle = (NodeFX) mouseEvent.getSource();
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED
                    && mouseEvent.getButton() == MouseButton.PRIMARY) {

                if (!circle.isSelected) {
                    if (selectedNode != null) {
                        if (addEdge && !edgeExists(selectedNode, circle)) {
                            weight = new Label();
                            System.out.println("Adding Edge");
                            // Adds the edge between two selected nodes
                            if (undirected) {
                                edgeLine = new Line(selectedNode.point.getX(), selectedNode.point.getY(),
                                        circle.point.getX(), circle.point.getY());
                                canvasGroup.getChildren().add(edgeLine);
                                edgeLine.setId("line");
                            } else if (directed) {
                                arrow = new Arrow(selectedNode.point.getX(), selectedNode.point.getY(),
                                        circle.point.getX(), circle.point.getY());
                                canvasGroup.getChildren().add(arrow);
                                arrow.setId("arrow");
                            }

                            // Adds weight between two selected nodes
                            if (weighted) {
                                weight.setLayoutX(((selectedNode.point.getX()) + (circle.point.getX())) / 2);
                                weight.setLayoutY(((selectedNode.point.getY()) + (circle.point.getY())) / 2);

                                boolean isValid = false;
                                do {
	                                TextInputDialog dialog = new TextInputDialog("0");
	                                dialog.setTitle(null);
	                                dialog.setHeaderText("Enter Weight of the Edge :");
	                                dialog.setContentText(null);
	                                
	                                Optional<String> result = dialog.showAndWait();
	                                if (result.get().matches("[A-Za-z]*")) continue;
	                                else isValid = true;
	                                if (result.isPresent()) {
	                                    weight.setText(result.get());
	                                } else {
	                                    weight.setText("0");
	                                }
                                } while (!isValid);
                                
                                canvasGroup.getChildren().add(weight);
                            } else if (unweighted) {
                                weight.setText("1");
                            }
                            Shape line_arrow = null;
                            Edge temp = null;
                            if (undirected) {
                                temp = new Edge(selectedNode.node, circle.node, Double.parseDouble(weight.getText()),
                                        edgeLine, weight);
                                if (weighted) {
                                    mstEdges.add(temp);
                                }
                                selectedNode.node.adjacents.add(new Edge(selectedNode.node, circle.node,
                                        Double.parseDouble(weight.getText()), edgeLine, weight));
                                circle.node.adjacents.add(new Edge(circle.node, selectedNode.node,
                                        Double.parseDouble(weight.getText()), edgeLine, weight));
                                edges.add(edgeLine);
                                realEdges.add(selectedNode.node.adjacents.get(selectedNode.node.adjacents.size() - 1));
                                realEdges.add(circle.node.adjacents.get(circle.node.adjacents.size() - 1));
                                line_arrow = edgeLine;

                            } else if (directed) {
                                temp = new Edge(selectedNode.node, circle.node, Double.parseDouble(weight.getText()), arrow,
                                        weight);
                                selectedNode.node.adjacents.add(temp);
                                edges.add(arrow);
                                line_arrow = arrow;
                                realEdges.add(temp);
                            }

                            RightClickMenu rt = new RightClickMenu(temp);
                            ContextMenu menu = rt.getMenu();
                            if (weighted) {
                                rt.changeId.setText("Change Weight");
                            } else if (unweighted) {
                                rt.changeId.setDisable(true);
                            }
                            final Shape la = line_arrow;
                            assert line_arrow != null;
                            line_arrow.setOnContextMenuRequested(e -> {
                                System.out.println("In Edge Menu :" + menuBool);

                                if (menuBool) {
                                    globalMenu.hide();
                                    menuBool = false;
                                }
                                if (addEdge || addNode) {
                                    globalMenu = menu;
                                    menu.show(la, e.getScreenX(), e.getScreenY());
                                    menuBool = true;
                                }
                            });
                            menu.setOnAction(e -> {
                                menuBool = false;
                            });
                        }
                  //-----------------------
                        if (addNode || (calculate && !calculated) || addEdge) {
                            selectedNode.isSelected = false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300), selectedNode, Color.RED,
                                    Color.BLACK);
                            ft1.play();
                        	System.out.println("Lam gi day????");
                        }
                        selectedNode = null;
                        return;
                    }

                    FillTransition ft = new FillTransition(Duration.millis(300), circle, Color.BLACK, Color.RED);
                    ft.play();
                    circle.isSelected = true;
                    selectedNode = circle;

                    if (calculate && !calculated) {
                        if (bfs) {
                            algo.newBFS(circle.node);
                        } else if (dfs) {
                            algo.newDFS(circle.node);
                        } else if (dijkstra) {
                            algo.newDijkstra(circle.node);
                        }

                        calculated = true;
                    } else if (calculate && !mst && !topSortBool) {

                        for (NodeFX n : circles) {
                            n.isSelected = false;
                            FillTransition ft1 = new FillTransition(Duration.millis(300), n);
                            ft1.setToValue(Color.BLACK);
                            ft1.play();
                        }
                        
                    }
                } else {
                    circle.isSelected = false;
                    FillTransition ft1 = new FillTransition(Duration.millis(300), circle, Color.RED, Color.BLACK);
                    ft1.play();
                    selectedNode = null;
                }

            }
        }
    };

    @FXML
    public void PlayPauseHandle(ActionEvent event) {
        System.out.println("IN PLAYPAUSE");
        System.out.println(playing + " " + paused);

        try {
            if (playing &&  st != null && st.getStatus() == Animation.Status.RUNNING) {
                Image image = new Image(getClass().getResourceAsStream("/image/hiPlay.png"));
                playPauseImage.setImage(image);
                System.out.println("Pausing");
                st.pause();
                paused = true;
                playing = false;
            } else if (paused && st != null) {
                Image image = new Image(getClass().getResourceAsStream("/image/pause-button.png"));
                playPauseImage.setImage(image);
                if (st.getStatus() == Animation.Status.PAUSED)
                    st.play();
                else if (st.getStatus() == Animation.Status.STOPPED)
                    st.playFromStart();
                playing = true;
                paused = false;
            }
        } catch (Exception e) {
            System.out.println("Error while play/pause: " + e);
            ClearHandle(null);
        }
    }

    @FXML
    public void ResetHandle(ActionEvent event) {
        ClearHandle(null);
        nNode = 0;
        canvasGroup.getChildren().clear();
        canvasGroup.getChildren().addAll(viewer);
        selectedNode = null;
        circles = new ArrayList<NodeFX>();
        distances = new ArrayList<Label>();
        addNode = true;
        addEdge = false;
        calculate = false;
        calculated = false;
        addNodeButton.setSelected(true);
        addEdgeButton.setSelected(false);
        addEdgeButton.setDisable(true);
        addNodeButton.setDisable(false);
        clearButton.setDisable(true);
        algo = new Algorithm();
        Image image = new Image(getClass().getResourceAsStream("/image/pause-button.png"));
        playPauseImage.setImage(image);
        hiddenPane.setPinnedSide(null);

        bfsButton.setDisable(true);
        topSortButton.setDisable(true);
        dfsButton.setDisable(true);
        dijkstraButton.setDisable(true);
        mstButton.setDisable(true);
        playing = false;
        paused = false;
    }

    @FXML
    public void ClearHandle(ActionEvent event) {
        if (st != null && st.getStatus() != Animation.Status.STOPPED)
            st.stop();
        if (st != null)
            st.getChildren().clear();
        menuBool = false;
        selectedNode = null;
        calculated = false;
        System.out.println("IN CLEAR:" + circles.size());
        for (NodeFX n : circles) {
            n.isSelected = false;
            n.node.visited = false;
            n.node.previous = null;
            n.node.minDistance = Double.POSITIVE_INFINITY;
            n.node.DAGColor = 0;

            FillTransition ft1 = new FillTransition(Duration.millis(300), n);
            ft1.setToValue(Color.BLACK);
            ft1.play();
        }
        for (Shape x : edges) {
            if (undirected) {
                StrokeTransition ftEdge = new StrokeTransition(Duration.millis(time), x);
                ftEdge.setToValue(Color.BLACK);
                ftEdge.play();
            } else if (directed) {
                FillTransition ftEdge = new FillTransition(Duration.millis(time), x);
                ftEdge.setToValue(Color.BLACK);
                ftEdge.play();
            }
        }
        canvasGroup.getChildren().remove(sourceText);
        for (Label x : distances) {
            x.setText("Distance : INFINITY");
            canvasGroup.getChildren().remove(x);
        }

        textFlow.clear();

        Image image = new Image(getClass().getResourceAsStream("/image/pause-button.png"));
        playPauseImage.setImage(image);

        distances = new ArrayList<>();
        addNodeButton.setDisable(false);
        addEdgeButton.setDisable(false);
        AddNodeHandle(null);
        bfs = false;
        dfs = false;
        dijkstra = false;
        mst = false;
        topSortBool = false;
        playing = false;
        paused = false;
    }

    @FXML
    public void AddEdgeHandle(ActionEvent event) {
        addNode = false;
        addEdge = true;
        calculate = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(true);

        if (unweighted) {
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);
            if (directed) {
                topSortButton.setDisable(false);
                topSortButton.setSelected(false);
            }
        }
        if (weighted) {
            dijkstraButton.setDisable(false);
            dijkstraButton.setSelected(false);
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);
            if (undirected) {
                mstButton.setDisable(false);
                mstButton.setSelected(false);
            }
        }
    }

    @FXML
    public void AddNodeHandle(ActionEvent event) {
        addNode = true;
        addEdge = false;
        calculate = false;
        addNodeButton.setSelected(true);
        addEdgeButton.setSelected(false);
        selectedNode = null;

        if (unweighted) {
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);

            if (directed) {
                topSortButton.setDisable(false);
                topSortButton.setSelected(false);
            }
        }
        if (weighted) {
            dijkstraButton.setDisable(false);
            dijkstraButton.setSelected(false);
            bfsButton.setDisable(false);
            bfsButton.setSelected(false);
            dfsButton.setDisable(false);
            dfsButton.setSelected(false);
            if (undirected) {
                mstButton.setDisable(false);
                mstButton.setSelected(false);
            }
        }
    }

    @FXML
    public void BFSHandle(ActionEvent event) {
        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        bfs = true;
        dfs = false;
        dijkstra = false;
        mst = false;
    }

    @FXML
    public void DFSHandle(ActionEvent event) {
        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        dfs = true;
        bfs = false;
        dijkstra = false;
        mst = false;
    }

    @FXML
    public void TopSortHandle(ActionEvent event) {
        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        dfs = false;
        bfs = false;
        dijkstra = false;
        mst = false;
        topSortBool = true;
        algo.newTopSort();
    }

    @FXML
    public void DijkstraHandle(ActionEvent event) {
        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        bfs = false;
        dfs = false;
        dijkstra = true;
        mst = false;
    }

    @FXML
    public void MSTHandle(ActionEvent event) {
        addNode = false;
        addEdge = false;
        addNodeButton.setSelected(false);
        addEdgeButton.setSelected(false);
        addNodeButton.setDisable(true);
        addEdgeButton.setDisable(true);
        calculate = true;
        clearButton.setDisable(false);
        bfs = false;
        dfs = false;
        dijkstra = false;
        mst = true;
        algo.newMST();
    }

    public void changeID(NodeFX source) {
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }
        selectedNode = null;

        TextInputDialog dialog = new TextInputDialog(Integer.toString(nNode));
        dialog.setTitle(null);
        dialog.setHeaderText("Enter Node ID :");
        dialog.setContentText(null);

        String res = null;
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            res = result.get();
        }

        circles.get(circles.indexOf(source)).id.setText(res);
        circles.get(circles.indexOf(source)).node.name = res;

        System.out.println("AFTER----------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }
    }

    public void deleteNode(NodeFX sourceFX) {
        selectedNode = null;
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }

        Node source = sourceFX.node;
        circles.remove(sourceFX);

        List<Edge> tempEdges = new ArrayList<>();
        List<Node> tempNodes = new ArrayList<>();
        for (Edge e : source.adjacents) {
            Node u = e.target;
            for (Edge x : u.adjacents) {
                if (x.target == source) {
                    x.target = null;
                    tempNodes.add(u);
                    tempEdges.add(x);
                }
            }
            edges.remove(e.line);
            canvasGroup.getChildren().remove(e.weightLabel);
            canvasGroup.getChildren().remove(e.line);
            mstEdges.remove(e);
        }
        for (Node q : tempNodes) {
            q.adjacents.removeAll(tempEdges);
        }
        List<Edge> tempEdges2 = new ArrayList<>();
        List<Shape> tempArrows = new ArrayList<>();
        List<Node> tempNodes2 = new ArrayList<>();
        for (NodeFX z : circles) {
            for (Edge s : z.node.adjacents) {
                if (s.target == source) {
                    tempEdges2.add(s);
                    tempArrows.add(s.line);
                    tempNodes2.add(z.node);
                    canvasGroup.getChildren().remove(s.line);
                }
            }
        }
        for (Node z : tempNodes2) {
            z.adjacents.removeAll(tempEdges2);
        }
        realEdges.removeAll(tempEdges);
        realEdges.removeAll(tempEdges2);
        canvasGroup.getChildren().remove(sourceFX.id);
        canvasGroup.getChildren().remove(sourceFX);

        System.out.println("AFTER----------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }
    }

    public void deleteEdge(Edge sourceEdge) {
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name);
            }
        }

        System.out.println(sourceEdge.source.name + " -- " + sourceEdge.target.name);
        List<Edge> ls1 = new ArrayList<>();
        List<Shape> lshape2 = new ArrayList<>();
        for (Edge e : sourceEdge.source.adjacents) {
            if (e.target == sourceEdge.target) {
                ls1.add(e);
                lshape2.add(e.line);
            }
        }
        for (Edge e : sourceEdge.target.adjacents) {
            if (e.target == sourceEdge.source) {
                ls1.add(e);
                lshape2.add(e.line);
            }
        }
        System.out.println("sdsdsd  " + ls1.size());
        sourceEdge.source.adjacents.removeAll(ls1);
        sourceEdge.target.adjacents.removeAll(ls1);
        realEdges.removeAll(ls1);

        edges.remove(sourceEdge.line);
        canvasGroup.getChildren().remove(sourceEdge.weightLabel);
        canvasGroup.getChildren().remove(sourceEdge.line);
        mstEdges.remove(sourceEdge);
        edges.removeAll(lshape2);
        canvasGroup.getChildren().removeAll(lshape2);

        System.out.println("AFTER----------");
        for (NodeFX p : circles) {
            System.out.println(p.node.name + " - ");
            for (Edge q : p.node.adjacents) {
                System.out.println(q.source.name + " " + q.target.name);
            }
        }
    }

    public void changeWeight(Edge sourceEdge) {
        System.out.println("Before-------");
        for (NodeFX u : circles) {
            System.out.println(u.node.name + " - ");
            for (Edge v : u.node.adjacents) {
                System.out.println(v.source.name + " " + v.target.name + " weight: " + v.weight);
            }
        }

        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle(null);
        dialog.setHeaderText("Enter Weight of the Edge :");
        dialog.setContentText(null);

        String res = null;
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            res = result.get();
        }

        for (Edge e : sourceEdge.source.adjacents) {
            if (e.target == sourceEdge.target) {
                e.weight = Double.valueOf(res);
                e.weightLabel.setText(res);
            }
        }
        for (Edge e : sourceEdge.target.adjacents) {
            if (e.target == sourceEdge.source) {
                e.weight = Double.valueOf(res);
            }
        }
        for (Edge e : mstEdges) {
            if (e.source == sourceEdge.source && e.target == sourceEdge.target) {
                e.weight = Double.valueOf(res);
            }
        }

        System.out.println("AFTER----------");
        for (NodeFX p : circles) {
            System.out.println(p.node.name + " - ");
            for (Edge q : p.node.adjacents) {
                System.out.println(q.source.name + " " + q.target.name + " weigh: " + q.weight);
            }
        }
    }

    // class thuat toan
    public class Algorithm {
        // thuat toan tim duong di ngan nhat tu 1 dinh toi cac dinh khac

        // duyet chieu rong
        public void newBFS(Node source) {
            st= new SequentialTransition();
            for (NodeFX n : circles) {
                distances.add(n.distance);
                n.distance.setLayoutX(n.point.getX() + 20);
                n.distance.setLayoutY(n.point.getY());
                canvasGroup.getChildren().add(n.distance);
            }
            sourceText.setLayoutX(source.circle.point.getX() + 20);
            sourceText.setLayoutY(source.circle.point.getY() + 10);
            canvasGroup.getChildren().add(sourceText);
            source.circle.distance.setText("Dist. : " + 0);
            new BFS(source);
        }

        // duyet chieu sau
        public void newDFS(Node source) {
            st= new SequentialTransition();
            for (NodeFX node : circles) {
                distances.add(node.distance);
                node.distance.setLayoutX(node.point.getX() + 20);
                node.distance.setLayoutY(node.point.getY());
                canvasGroup.getChildren().add(node.distance);
            }
            sourceText.setLayoutX(source.circle.point.getX() + 20);
            sourceText.setLayoutY(source.circle.point.getY() + 10);
            canvasGroup.getChildren().add(getSourceText());
            source.circle.distance.setText("Dist : " + 0);

            new DFS(source);
        }

        // cay con nho nhat
        public void newMST() {
            st= new SequentialTransition();
            new MST();
        }
        public void newDijkstra(Node source) {
            st = new SequentialTransition();
            for (NodeFX n : circles) {
                distances.add(n.distance);
                n.distance.setLayoutX(n.point.getX() + 20);
                n.distance.setLayoutY(n.point.getY());
                canvasGroup.getChildren().add(n.distance);
            }
            sourceText.setLayoutX(source.circle.point.getX() + 20);
            sourceText.setLayoutY(source.circle.point.getY() + 10);
            canvasGroup.getChildren().add(sourceText);

            source.circle.distance.setText("Dist. : " + 0);
            new Dijkstra(source);
        }

        // topo sort
        public void newTopSort() {
            st= new SequentialTransition();
            new TopSort();
        }
    }
}
