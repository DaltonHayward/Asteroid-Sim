//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MouseView extends StackPane implements Subscriber{
    private double size;
    private Canvas canvas;
    private SpaceModel model;
    private InteractionModel iModel;
    private GraphicsContext gc;

    public MouseView(double size) {
        this.size = size;
        canvas = new Canvas(size, size);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, size, size);

        this.getChildren().add(canvas);
    }

    public void setModel(SpaceModel m) {
        model = m;
    }

    public void setIModel(InteractionModel iM) {
        iModel = iM;
    }

    public void draw() {
        gc.fillRect(0,0, size, size);

        gc.save();
        // translate to position
        gc.translate(size/2 - (iModel.get_mX()*size)*2, size/2 - (iModel.get_mY()*size)*2);
        gc.scale(2,2);


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
        for (Asteroid asteroid: model.getAsteroids()) {
            normalizedX = asteroid.get_tX()*size;
            normalizedY = asteroid.get_tY()*size;
//            normalizedR = asteroid.get_tR()*size;
            gc.save();
            gc.setFill(Color.GREY);
            gc.translate(normalizedX, normalizedY);
            gc.scale(size, size);
            gc.rotate(asteroid.getAngle());
            gc.fillPolygon(asteroid.getXPoints(), asteroid.getYPoints(), asteroid.getXPoints().length);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(0.005);
            gc.strokePolygon(asteroid.getXPoints(), asteroid.getYPoints(), asteroid.getXPoints().length);
            gc.restore();
        }
        gc.restore();
    }

    @Override
    public void notify(String channel) {
        draw();
    }
}
