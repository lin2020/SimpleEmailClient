package com.lin.view;

import java.util.*;
import java.awt.Rectangle;
import javax.swing.event.ChangeListener;
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
import javafx.scene.Parent;
import javafx.fxml.*;
import java.util.regex.*;
import javafx.scene.web.HTMLEditor;
import javafx.geometry.Pos;
import javafx.scene.text.*;

import com.lin.database.*;
import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;
import com.lin.view.*;

public class ProgressDialog extends Stage {

    // cancel
    private boolean cancel;

    // root
    private VBox root;
    // hbox1
    private HBox hbox1;
    private VBox vbox1;
    private Label titleLabel;
    private VBox vbox2;
    private Label messageLabel;
    private ProgressBar progressBar;
    // separator
    private Separator separator;
    // hbox2
    private HBox hbox2;
    private Label addrLabel;
    private Pane spacePane;
    private Button cancelButton;


    public ProgressDialog(String msg) {
        initComponents(msg);
        initEvents();
    }

    private void initComponents(String msg) {
        // * root pane
        root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(8.0, 8.0, 8.0, 8.0));

        // ** hbox1
        hbox1 = new HBox();
        hbox1.setSpacing(7);
        // *** vbox1
        vbox1 = new VBox();
        vbox1.setSpacing(7);
        vbox1.setAlignment(Pos.CENTER);
        // **** titleLabel
        titleLabel = new Label("title");
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setWrapText(true);
        titleLabel.setFont(new Font("System Bold", 20));
        vbox1.getChildren().add(titleLabel);
        hbox1.getChildren().add(vbox1);
        // *** vbox2
        vbox2 = new VBox();
        vbox2.setSpacing(7);
        vbox2.setAlignment(Pos.CENTER);
        // **** progressBar
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(350);
        progressBar.setProgress(0.5f);
        // **** messageLabel
        messageLabel = new Label("message");
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setWrapText(true);
        messageLabel.setFont(new Font(16));
        vbox2.getChildren().add(messageLabel);
        vbox2.getChildren().add(progressBar);
        HBox.setHgrow(vbox2, Priority.ALWAYS);
        hbox1.getChildren().add(vbox2);
        VBox.setVgrow(hbox1, Priority.ALWAYS);
        root.getChildren().add(hbox1);

        // ** separator
        separator = new Separator();
        root.getChildren().add(separator);

        // ** hbox2
        hbox2 = new HBox();
        // *** addrLabel
        addrLabel = new Label(msg);
        hbox2.getChildren().add(addrLabel);
        // *** spacePane
        spacePane = new Pane();
        HBox.setHgrow(spacePane, Priority.ALWAYS);
        hbox2.getChildren().add(spacePane);
        // *** cancelButton
        cancelButton = new Button("取消");
        hbox2.getChildren().add(cancelButton);
        root.getChildren().add(hbox2);

        Scene scene = new Scene(root, 500, 150);

        setTitle("收取邮件 - " + msg);
        setScene(scene);
        show();
    }

    private void initEvents() {

        cancel = false;

        cancelButton.setOnAction((ActionEvent t)->{
            hide();
            cancel = true;
        });
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public ProgressBar getProgress() {
        return progressBar;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public boolean getCancel(){
        return cancel;
    }
}
