package application;


import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RightClickMenu {

    ContextMenu menu;
    NodeFX sourceNode;
    Edge sourceEdge;
    MenuItem delete, changeId;

    public RightClickMenu() {
        menu = new ContextMenu();
        delete = new MenuItem("Delete");
        changeId = new MenuItem("Change ID");

        Image openIcon = new Image(getClass().getResourceAsStream("/image/delete_img.png"));
        ImageView openView = new ImageView(openIcon);
        delete.setGraphic(openView);

        Image textIcon = new Image(getClass().getResourceAsStream("/image/rename_img.png"));
        ImageView textIconView = new ImageView(textIcon);
        changeId.setGraphic(textIconView);

        menu.getItems().addAll(delete, changeId);
        menu.setOpacity(0.9);
    }

    public RightClickMenu(NodeFX node) {
        this();
        sourceNode = node;
        delete.setOnAction(e -> {
            DefineGraph.cref.deleteNode(sourceNode);
        });
        changeId.setOnAction(e -> {
            DefineGraph.cref.changeID(node);
        });
    }

    public RightClickMenu(Edge edge) {
        this();
        sourceEdge = edge;
        delete.setOnAction(e -> {
            DefineGraph.cref.deleteEdge(sourceEdge);
        });
        changeId.setOnAction(e -> {
            DefineGraph.cref.changeWeight(sourceEdge);
        });
    }

    public ContextMenu getMenu() {
        return menu;
    }
}
