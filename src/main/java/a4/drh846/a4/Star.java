//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Star {
    private double tx;
    private double ty;
    private double tr;
    private Color color;
    private static final Color[] POTENTIAL_COLORS = {
            Color.WHITE,
            Color.WHITESMOKE,
            Color.ANTIQUEWHITE,
            Color.LIGHTGRAY,
            Color.DARKGRAY
    };

    public Star(double x, double y, double r) {
        tx = x;
        ty = y;
        tr = r;
        int c = new Random().nextInt(5);
        color = POTENTIAL_COLORS[c];
    }

    public double getX() {
        return tx;
    }

    public double getY() {
        return ty;
    }

    public double getR() {
        return tr;
    }

    public Color getColor() {
        return color;
    }
}
