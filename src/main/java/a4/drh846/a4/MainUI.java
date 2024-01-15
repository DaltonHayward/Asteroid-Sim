//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Random;

public class MainUI extends HBox {
    public MainUI(double size) {

        SpaceModel model = new SpaceModel();
        model.createChannel("asteroid");

        InteractionModel iModel = new InteractionModel();
        iModel.createChannel("worldRotation");
        iModel.createChannel("mouse");
        iModel.createChannel("selection");

        SpaceView view = new SpaceView(size);
        SpaceController controller = new SpaceController();

        // main view
        view.setModel(model);
        view.setIModel(iModel);
        view.setupEvents(controller);
        model.addSubscriber("asteroid", view);
        iModel.addSubscriber("worldRotation", view);
        iModel.addSubscriber("selection", view);
        // Side panel
        VBox sidePanel = new VBox();
        // mini view
        SpaceView miniView = new SpaceView(size/4);
        miniView.setModel(model);
        miniView.setIModel(iModel);
        model.addSubscriber("asteroid", miniView);
        iModel.addSubscriber("worldRotation", miniView);
        iModel.addSubscriber("selection", miniView);
        // 2x mouse view
        MouseView mouseView = new MouseView(size/4);
        mouseView.setModel(model);
        mouseView.setIModel(iModel);
        model.addSubscriber("asteroid", mouseView);
        iModel.addSubscriber("worldRotation", mouseView);
        iModel.addSubscriber("mouse", mouseView);
        // controls view
        ControlsView controlsView = new ControlsView();
        controlsView.setupEvents(controller);

        sidePanel.getChildren().addAll(miniView, mouseView, controlsView);

        controller.setModel(model);
        controller.setIModel(iModel);










        for (int i=0; i < 10; i++) {
            double asteroidX = Math.random() - 0.5;
            double asteroidY = Math.random() - 0.5;
            double asteroidR = new Random().nextDouble(0.3, 1.0);
            double asteroidAngle = new Random().nextDouble(0.0, 360.0);
            model.createAsteroid(asteroidX, asteroidY, asteroidR, asteroidAngle, size);
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                controller.handleAnimationTick();
            }
        };
        timer.start();

        this.setStyle("-fx-base: #191919; -fx-background-color: #191919");
        this.getChildren().addAll(sidePanel, view);
    }
}
