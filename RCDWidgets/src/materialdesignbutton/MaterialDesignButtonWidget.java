package materialdesignbutton;

import java.io.IOException;

import com.sun.javafx.scene.control.skin.ButtonSkin;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MaterialDesignButtonWidget extends Button {

	public static enum VAL {DEFAULT, TRANSPARENT, RED, YELLOW, GREEN};
	
    private Circle circleRipple;
    private Rectangle rippleClip = new Rectangle();
    private Duration rippleDuration =  Duration.millis(250);
    private double lastRippleHeight = 0;
    private double lastRippleWidth = 0;
    private Color rippleColor = new Color(0, 0, 0, 0.11);

    public MaterialDesignButtonWidget() {
        
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("material_design_button.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

//        getStyleClass().addAll("md-button");
//        createRippleEffect();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        final ButtonSkin buttonSkin = new ButtonSkin(this);
        // Adding circleRipple as fist node of button nodes to be on the bottom
        this.getChildren().add(0, circleRipple);
        return buttonSkin;
    }

    private void createRippleEffect() {
        circleRipple = new Circle(0.1, rippleColor);
        circleRipple.setOpacity(0.0);
        // Optional box blur on ripple - smoother ripple effect
//        circleRipple.setEffect(new BoxBlur(3, 3, 2));

        // Fade effect bit longer to show edges on the end
        final FadeTransition fadeTransition = new FadeTransition(rippleDuration, circleRipple);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        final Timeline scaleRippleTimeline = new Timeline();

        final SequentialTransition parallelTransition = new SequentialTransition();
        parallelTransition.getChildren().addAll(
                scaleRippleTimeline,
                fadeTransition
        );

        parallelTransition.setOnFinished(event1 -> {
            circleRipple.setOpacity(0.0);
            circleRipple.setRadius(0.1);
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            parallelTransition.stop();
            parallelTransition.getOnFinished().handle(null);

            circleRipple.setCenterX(event.getX());
            circleRipple.setCenterY(event.getY());

            // Recalculate ripple size if size of button from last time was changed
            if (getWidth() != lastRippleWidth || getHeight() != lastRippleHeight)
            {
                lastRippleWidth = getWidth();
                lastRippleHeight = getHeight();

                rippleClip.setWidth(lastRippleWidth);
                rippleClip.setHeight(lastRippleHeight);

                try {
                    rippleClip.setArcHeight(this.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
                    rippleClip.setArcWidth(this.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
                    circleRipple.setClip(rippleClip);
                } catch (Exception e) {

                }

                // Getting 45% of longest button's length, because we want edge of ripple effect always visible
                double circleRippleRadius = Math.max(getHeight(), getWidth()) * 0.45;
                final KeyValue keyValue = new KeyValue(circleRipple.radiusProperty(), circleRippleRadius, Interpolator.EASE_OUT);
                final KeyFrame keyFrame = new KeyFrame(rippleDuration, keyValue);
                scaleRippleTimeline.getKeyFrames().clear();
                scaleRippleTimeline.getKeyFrames().add(keyFrame);
            }

            parallelTransition.playFromStart();
        });
    }

    public void widgetSetRippleColor(Color color) {
        circleRipple.setFill(color);
    }
    
    public void widgetSetVal(final VAL color) {

		getStyleClass().clear();
    	switch (color) {
		case RED:
			getStyleClass().addAll("md-button-val-red");
			break;
			
		case YELLOW:
			getStyleClass().addAll("md-button-val-yellow");
			break;
			
		case GREEN:
			getStyleClass().addAll("md-button-val-green");
			break;

		case DEFAULT:
			getStyleClass().addAll("md-button-val-default");
			break;
			
		default:
			break;
		}
    	createRippleEffect();
    }

}
