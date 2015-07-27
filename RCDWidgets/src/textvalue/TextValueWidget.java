package textvalue;

import java.io.IOException;

import materialdesignbutton.MaterialDesignButtonWidget.VAL;

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

public class TextValueWidget extends AnchorPane {
	
	@FXML Label name;
	@FXML Rectangle val;

    public TextValueWidget() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("text_value.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        getStyleClass().addAll("md-anchore-pane");
        name.getStyleClass().addAll("md-name");
        val.getStyleClass().addAll("md-rect-val-default");
    }

    public void widgetSetName(String str) {
    	name.setText(str);
    }
    
    public void widgetSetVal(VAL color) {
    	switch (color) {
		case RED:
			val.getStyleClass().addAll("md-rect-val-red");
			break;
			
		case YELLOW:
			val.getStyleClass().addAll("md-rect-val-yellow");
			break;
			
		case GREEN:
			val.getStyleClass().addAll("md-rect-val-green");
			break;

		default:
			val.getStyleClass().addAll("md-rect-val-default");
			break;
		}
    }
    

}
