//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ControlsView extends VBox{
    private Label label;
    private Slider slider;
    private CheckBox movementBox;
    private CheckBox spinBox;

    public ControlsView() {
        label = new Label("Rotation Speed");
        slider = new Slider(0.0, 0.5, 0.1);
        movementBox = new CheckBox("Asteroid Movement");
        movementBox.setSelected(true);
        spinBox = new CheckBox("Asteroid Spin");
        spinBox.setSelected(true);

        this.getChildren().addAll(label, slider, movementBox, spinBox);
    }

    public void setupEvents(SpaceController controller) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                controller.handleSliderDragged(slider.getValue());
            }
        });
        movementBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                controller.handleMovementBoxChecked();
            }
        });
        spinBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                controller.handleSpinBoxChecked();
            }
        });
    }
}
