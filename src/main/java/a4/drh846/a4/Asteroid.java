//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Random;

public class Asteroid {
    private double tX;
    private double tY;
    private double tR;
    private double angle;
    private double vX;
    private double vY;
    private double vAngle;
    private double[] xPoints;
    private double[] yPoints;
    private double size;
    private double viewSize;
    //bitmap
    private Canvas offscreen;
    private WritableImage buffer;
    private PixelReader reader;
    private int z;

    public Asteroid(double tX, double tY, double tR, double angle, double viewSize, int z) {
        Random rand = new Random();
        double range = 0.002;

        this.tX = tX;
        this.tY = tY;
        this.tR = tR;
        this.angle = angle;
        this.viewSize = viewSize;
        this.vX = rand.nextDouble(-range, range);
        this.vY = rand.nextDouble(-range, range);
        this.vAngle = rand.nextDouble(-range*1000, range*1000);
        this.z = z;

        int numSections = rand.nextInt(4, 9);
        xPoints = new double[numSections];
        yPoints = new double[numSections];

        double sectionSize = 360.0/numSections;
        double sectionAngle = 0;

        for (int i=0; i< numSections; i++) {
            double newR = new Random().nextDouble(0.05, 0.3) * (0.5*tR);
            double xPoint = newR*Math.cos(Math.toRadians(sectionAngle));
            double yPoint = newR*Math.sin(Math.toRadians(sectionAngle));
            xPoints[i] = xPoint;
            yPoints[i] = yPoint;
            sectionAngle += sectionSize;
        }

        // find height and width of polygon
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i=0; i<xPoints.length; i++) {
            maxX = Math.max(maxX, xPoints[i]);
            minX = Math.min(minX, xPoints[i]);
            maxY = Math.max(maxY, yPoints[i]);
            minY = Math.min(minY, yPoints[i]);
        }
        // shape size will be the max of either the height or width of the asteroid
        this.size = Math.max(Math.abs(maxX-minX), Math.abs(maxY-minY));
        createBitmap();
    }

    public double get_tX() {
        return tX;
    }

    public double get_tY() {
        return tY;
    }

    public double get_tR() {
        return tR;
    }

    public double getAngle() {
        return angle;
    }

    public double[] getXPoints() {
        return xPoints;
    }

    public double[] getYPoints() {
        return yPoints;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int value) {
        z = value;
    }

    public void set_tX(double value) {
        tX = value;
    }

    public void set_tY(double value) {
        tY = value;
    }

    public void set_vX(double value) {
        vX = value;
    }

    public void set_vY(double value) {
        vY = value;
    }

    // moves the asteroid
    public void moveAsteroid(){
        tX = tX + vX;
        tY = tY + vY;

        // max radius can be is tRadius*0.2
        if (tY < -0.5-tR*0.2) {
            tY = 0.5+tR*0.2;
        } else if (tY > 0.5+tR*0.2) {
            tY = -0.5-tR*0.2;
        }

        if (tX < -0.5-tR*0.2) {
            tX = 0.5+tR*0.2;
        } else if (tX > 0.5+tR*0.2) {
            tX = -0.5-tR*0.2;
        }
    }

    // rotates the asteroid
    public void spinAsteroid() {
        angle = (angle + vAngle) % 360;
        if (angle < 0) {
            angle = 360;
        }
    }

    // create a bitmap of the asteroid
    private void createBitmap() {
        // create bitmap
        offscreen = new Canvas(size*viewSize, size*viewSize);
        GraphicsContext offscreenGC = offscreen.getGraphicsContext2D();
        buffer = new WritableImage((int) offscreen.getWidth(), (int) offscreen.getHeight());
        offscreenGC.setFill(Color.BLACK);
        offscreenGC.fillRect(0,0, offscreen.getWidth(), offscreen.getHeight());
        offscreenGC.setFill(Color.WHITE);
        offscreenGC.translate(offscreen.getWidth()/2, offscreen.getHeight()/2);
        // translate points into normalized points
        double[] normXPoints = new double[xPoints.length];
        for (int i=0; i < xPoints.length; i++) {
            normXPoints[i] = xPoints[i]*viewSize;
        }
        double[] normYPoints = new double[yPoints.length];
        for (int i=0; i < yPoints.length; i++) {
            normYPoints[i] = yPoints[i]*viewSize;
        }

        offscreenGC.fillPolygon(normXPoints, normYPoints, normXPoints.length);

        offscreen.snapshot(null ,buffer);
        reader = buffer.getPixelReader();
    }

    // rotates an x value by radians radians
    private double rotateX(double x, double y, double radians) { return Math.cos(radians) * x - Math.sin(radians) * y; }
    // rotates a y value by radians radians
    private double rotateY(double x, double y, double radians) { return Math.sin(radians) * x + Math.cos(radians) * y; }

    // returns true if the point is inside the asteroid, else false
    public boolean contains(double x, double y) {
        // translate relative mouse coords to center of screen
        x -= tX;
        y -= tY;

        // rotate mouse coords back to their 0 angle equivalent
        double rx = rotateX(x, y, Math.toRadians(-angle));
        double ry = rotateY(x, y, Math.toRadians(-angle));

        // normalize rotated relative coords, convert home coords to regular coords b/c bitmap is in normal coords
        int px = (int) (rx*viewSize + buffer.getWidth()/2);
        int py = (int) (ry*viewSize + buffer.getHeight()/2);

        // check bitmap with transformed coords
        if (px >= 0 && px < buffer.getWidth() && py >= 0 && py < buffer.getHeight()) {
            return reader.getColor(px, py).equals(Color.WHITE);
        }
        else {
            return false;
        }
    }

    // helper function for areaContains, calculates distance from 2 points
    private double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    // returns true if this asteroid has any polygon points inside the area of a circle, else returns false
    public boolean areaContains(double x, double y, double r) {
        x -= tX;
        y -= tY;

        boolean pointInside = false;

        // check every point in the asteroid, if one is inside the area return true using all relative coords
        for (int i=0; i<xPoints.length; i++) {
            double px = rotateX(xPoints[i], yPoints[i], Math.toRadians(angle));
            double py = rotateY(xPoints[i], yPoints[i], Math.toRadians(angle));
            if (pointInside) {
                return true;
            }
            if (dist(x, y, px, py) <= r) {
                pointInside = true;
            }
        }
        return false;
    }
}
