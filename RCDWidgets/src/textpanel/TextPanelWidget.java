package textpanel;

import java.io.IOException;

import com.sun.javafx.scene.control.skin.ButtonSkin;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TextPanelWidget extends AnchorPane {

	@FXML Label title;
	@FXML Rectangle rectangle;

    public TextPanelWidget() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("text_panel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        title.getStyleClass().addAll("md-text");
        rectangle.widthProperty().bind(widthProperty());
        rectangle.heightProperty().bind(heightProperty());
    }

    public void widgetSetPanelTitle(String name) {
    	title.setText(name);
    }
    

}
