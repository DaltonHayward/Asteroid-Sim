//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import java.util.ArrayList;

public class InteractionModel {
    private PublishSubscribe publishSubscribe;
    private double worldRotation;
    private double worldRotationSpeed;
    private ArrayList<Asteroid> selected;
    private ArrayList<Asteroid> hovered;
    private ArrayList<Asteroid> areaHovered;
    private double mX;
    private double mY;
    private double deltaX;
    private double deltaY;
    private double cursorSize;
    private double maxCursorSize;

    public InteractionModel() {
        publishSubscribe = new PublishSubscribe();
        worldRotation = 0;
        worldRotationSpeed = 0.1;
        selected = new ArrayList<>();
        hovered = new ArrayList<>();
        areaHovered = new ArrayList<>();
        cursorSize = 0.0;
        maxCursorSize = 0.25;
        mX = -0.5;
        mY = -0.5;
    }

    public void updateMousePos(double x, double y, double size) {
        x = x-size/2;
        y = y-size/2;
        // previous mouse pos
        deltaX = mX;
        deltaY = mY;
        // normalized home coordinates
        mX = rotateX(x, y, Math.toRadians(-worldRotation)) / size;
        mY = rotateY(x, y, Math.toRadians(-worldRotation)) / size;

        notifySubscribers("mouse");
    }

    public double get_mX() {
        return mX;
    }

    public double get_mY() {
        return mY;
    }

    public double getDeltaX(){
        return mX - deltaX;
    }

    public  double getDeltaY() {
        return  mY - deltaY;
    }

    private double rotateX(double x, double y, double radians) { return Math.cos(radians) * x - Math.sin(radians) * y; }
    private double rotateY(double x, double y, double radians) { return Math.sin(radians) * x + Math.cos(radians) * y; }


    public double getWorldRotation(){
        return worldRotation;
    }

    public double getWorldRotationSpeed(){
        return worldRotationSpeed;
    }

    public void setWorldRotationSpeed(double value){
        worldRotationSpeed = value;
    }

    public void rotateWorld(double amount) {
        worldRotation = (worldRotation + amount) % 360;
        notifySubscribers("worldRotation");

        mX = rotateX(mX, mY, Math.toRadians(-amount));
        mY = rotateY(mX, mY, Math.toRadians(-amount));
        deltaX = rotateX(deltaX, deltaY, Math.toRadians(-amount));
        deltaY = rotateY(deltaX, deltaY, Math.toRadians(-amount));

        for (Asteroid a : selected) {
            a.set_tX(rotateX(a.get_tX(), a.get_tY(), Math.toRadians(-amount)));
            a.set_tY(rotateY(a.get_tX(), a.get_tY(), Math.toRadians(-amount)));
        }
    }

    public ArrayList<Asteroid> getSelected() {
        return selected;
    }

    public void addSelected(Asteroid a) {
        selected.add(a);
        notifySubscribers("selection");
    }

    public void clearSelected() {
        selected.clear();
        notifySubscribers("selection");
    }

    public ArrayList<Asteroid> getHovered() {
        return hovered;
    }

    public void addHovered(Asteroid a) {
        hovered.add(a);
        notifySubscribers("selection");
    }

    public void clearHovered() {
        hovered.clear();
        notifySubscribers("selection");
    }

    public void removeHovered(Asteroid a) {
        areaHovered.remove(a);
        notifySubscribers("selection");
    }

    public double getCursorSize() {
        return cursorSize;
    }

    public void changeCursorSize(double delta) {
        delta = delta/3000;
        if (cursorSize + delta >= maxCursorSize) {
            cursorSize = maxCursorSize;
        }
        else if (cursorSize + delta <= 0) {
            cursorSize = 0;
        }
        else {
            cursorSize += delta;
        }
        notifySubscribers("mouse");
    }

    public void addSubscriber(String channelKey, Subscriber s) {
        publishSubscribe.subscribe(channelKey, s);
    }

    public void createChannel(String channelKey) {
        publishSubscribe.createChannel(channelKey);
    }

    public void notifySubscribers(String channelKey) {
        publishSubscribe.publish(channelKey);
    }

}
