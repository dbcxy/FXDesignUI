package textvalue.three;

import java.io.IOException;

import materialdesignbutton.MaterialDesignButtonWidget.VAL;
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

public class TextValue3Widget extends AnchorPane {
	
	@FXML Label name;
	@FXML Rectangle val1;
	@FXML Rectangle val2;
	@FXML Rectangle val3;

    public TextValue3Widget() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("text_value_3.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        getStyleClass().addAll("md-anchore-pane");
        name.getStyleClass().addAll("md-name");
        val1.getStyleClass().addAll("md-rect-val-default");
        val2.getStyleClass().addAll("md-rect-val-default");
        val3.getStyleClass().addAll("md-rect-val-default");
    }

    public void widgetSetName(String str) {
    	name.setText(str);
    }
    
    public void widgetSetVal_1(VAL color) {
    	widgetSetVal(val1, color);
    }
    
    public void widgetSetVal_2(VAL color) {
    	widgetSetVal(val2, color);
    }
    
    public void widgetSetVal_3(VAL color) {
    	widgetSetVal(val3, color);
    }
    
    public void widgetSetVal_3_Opacity(double val) {
    	val3.setOpacity(val);
    }
    
    private void widgetSetVal(Rectangle val, VAL color) {
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
			
		case DEFAULT:
			val.getStyleClass().addAll("md-rect-val-default");
			break;

		case TRANSPARENT:
			val.getStyleClass().addAll("md-rect-val-transparent");
			break;
		}
    }
    

}
