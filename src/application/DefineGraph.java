package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXRadioButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class DefineGraph implements Initializable {
    public static boolean directed = false, undirected = false, weighted = false, unweighted = false;

    @FXML
    public Button nextButton;
    @FXML
    private JFXRadioButton dButton, udButton, wButton, uwButton;
    @FXML
    private AnchorPane defineGraphPane;
    static CanvasController cref;

    @Override
    public void initialize(URL loacation, ResourceBundle resource) {

        dButton.setSelected(directed);
        udButton.setSelected(undirected);
        wButton.setSelected(weighted);
        uwButton.setSelected(unweighted);

        nextButton.setDisable(true);
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(directed + " " + weighted);
                    if ((directed || undirected) && (weighted || unweighted)) {
                        System.out.println("In thread " + directed);
                        nextButton.setDisable(false);
                        break;
                    }
                }
                System.out.println("Exiting thread");
            }
        };
        t.start();

        dButton.setOnAction(e -> {
            directed = true;
            undirected = false;
            System.out.println("dButton");
        });
        udButton.setOnAction(e -> {
            directed = false;
            undirected = true;
            System.out.println("udButton");
        });
        wButton.setOnAction(e -> {
            weighted = true;
            unweighted = false;
            System.out.println("wButton");
        });
        uwButton.setOnAction(e -> {
            weighted = false;
            unweighted = true;
            System.out.println("uwButton");
        });
        nextButton.setOnAction(e -> {
            loadNextScene();
        });
    }

    void loadNextScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Canvas.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            cref = loader.getController();

            Main.primaryStage.setScene(newScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}