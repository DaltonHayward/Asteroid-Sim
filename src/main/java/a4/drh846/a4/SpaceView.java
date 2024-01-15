//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Comparator;

public class SpaceView extends StackPane implements Subscriber {
    private double size;
    private Canvas canvas;
    private SpaceModel model;
    private InteractionModel iModel;
    private GraphicsContext gc;

    public SpaceView(double size) {
        this.size = size;
        canvas = new Canvas(size, size);
        gc = canvas.getGraphicsContext2D();

        this.getChildren().add(canvas);
    }

    public void setModel(SpaceModel m) {
        model = m;
    }

    public void setIModel(InteractionModel iM) {
        iModel = iM;
    }

    public void setupEvents(SpaceController controller){
        canvas.setOnMouseMoved(e -> controller.handleMouseMoved(e, size));
        canvas.setOnScroll(controller::handleWheel);
        canvas.setOnMousePressed(controller::handleMousePressed);
        canvas.setOnMouseDragged(e -> controller.handleMouseDragging(e, size));
        canvas.setOnMouseReleased(controller::handleMouseReleased);
    }

    public void draw() {
        gc.save();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, size, size);
        gc.translate(size/2, size/2);
        gc.rotate(iModel.getWorldRotation());

        double normalizedX;
        double normalizedY;
        double normalizedR;

        // draw stars
        for (Star star: model.getStars()) {
            normalizedX = star.getX()*size;
            normalizedY = star.getY()*size;
            normalizedR = star.getR()*size;
            gc.save();
            gc.setFill(star.getColor());
            gc.fillOval(normalizedX, normalizedY, normalizedR*2, normalizedR*2);
            gc.restore();
        }

        // draw asteroids
        model.getAsteroids().sort(Comparator.comparingInt(Asteroid::getZ));
        for (Asteroid asteroid: model.getAsteroids()) {
            normalizedX = asteroid.get_tX()*size;
            normalizedY = asteroid.get_tY()*size;
            gc.save();
            gc.setFill((iModel.getHovered().contains(asteroid) || iModel.getSelected().contains(asteroid)) ? Color.YELLOW : Color.GREY);
            gc.translate(normalizedX, normalizedY);
            gc.rotate(asteroid.getAngle());
            gc.scale(size, size);
            gc.fillPolygon(asteroid.getXPoints(), asteroid.getYPoints(), asteroid.getXPoints().length);
            gc.setStroke((iModel.getSelected().contains(asteroid)) ? Color.RED : Color.WHITE);
            gc.setLineWidth(0.0025);
            gc.strokePolygon(asteroid.getXPoints(), asteroid.getYPoints(), asteroid.getXPoints().length);
            gc.restore();
        }

        // normalize cursor variables
        double normalizedSize = iModel.getCursorSize()*size;
        normalizedX = iModel.get_mX()*size;
        normalizedY = iModel.get_mY()*size;
        // draw area cursor
        gc.setFill(Color.web("#A9A9A9", 0.5));
        gc.translate(normalizedX, normalizedY);
        gc.fillOval(-normalizedSize,-normalizedSize, normalizedSize*2, normalizedSize*2);
        gc.restore();
    }

    @Override
    public void notify(String channel) {
        draw();
    }
}
