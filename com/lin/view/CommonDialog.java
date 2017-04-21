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
import java.util.Date;
import java.text.SimpleDateFormat;
import javafx.concurrent.*;
import java.io.File;
import java.io.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.lin.database.*;
import com.lin.model.*;
import com.lin.util.*;
import com.lin.view.*;

public class CommonDialog extends Stage {

    public CommonDialog(String title, String message) {

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(16);

        Label label = new Label(message, new ImageView(new Image(getClass().getResourceAsStream("提示.png"))));
        label.setFont(new Font("Arial", 18));
        label.setGraphicTextGap(12);
        vbox.getChildren().add(label);

        Button button = new Button("确定");
        button.setFont(new Font("Arial", 14));
        vbox.getChildren().add(button);

        Scene scene = new Scene(vbox, 200, 150);

        setTitle(title);
        setScene(scene);
        show();

        button.setOnAction((ActionEvent ae)->{
            hide();
        });
    }
}
