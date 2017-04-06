package test.lin.view;

import java.util.*;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.event.*;
import javafx.event.EventHandler;
import static javafx.geometry.HPos.RIGHT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.stage.*;
import javafx.geometry.*;

import com.lin.view.*;
import com.lin.util.*;
import com.lin.protocol.*;
import com.lin.model.*;
import com.lin.database.*;

public class PopupTest extends Application {
    @Override
    public void start(Stage primaryStage) {

        TextField input_text = new TextField();

        StackPane root = new StackPane();
        root.getChildren().add(input_text);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("PopupTest");
        primaryStage.setScene(scene);
        primaryStage.show();

        input_text.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                ObservableList<String> contents = FXCollections.observableArrayList("a", "b", "c", "d", "e", "f", "g");
                ListView<String> listView = new ListView<String>();
                listView.setItems(contents);
                Popup popup = new Popup();
                popup.setAutoHide(true);
                popup.setX(300);
                popup.setY(200);
                popup.getContent().addAll(listView);
                double x_offset = input_text.getScene().getWindow().getX() + input_text.getScene().getX() + input_text.localToScene(0, 0).getX();
                double y_offset = input_text.getScene().getWindow().getY() + input_text.getScene().getY() + input_text.localToScene(0, 0).getY() + input_text.getBoundsInParent().getHeight();
                popup.show(input_text, x_offset, y_offset);
            }
        });
    }
}
