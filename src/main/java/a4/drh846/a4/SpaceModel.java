//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class SpaceModel {
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Star> stars;
    private PublishSubscribe publishSubscribe;
    public int z;

    public SpaceModel() {
        publishSubscribe = new PublishSubscribe();
        asteroids = new ArrayList<>();
        stars = new ArrayList<>();
        z = 0;
        for (int i=0; i < 100; i++) {
            double starX = Math.random()-0.5;
            double starY = Math.random()-0.5;
            double starR = new Random().nextDouble(0.0009, 0.001);
            stars.add(new Star(starX, starY, starR));
        }
//        stars.add(new Star(-0.45, -0.45, 0.05));
    }

    public ArrayList<Star> getStars() {
        return stars;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public int getZ() {
        return z++;
    }


    // create a new asteroid and add it to the asteroid list
    public void createAsteroid(double tx, double ty, double r, double angle, double size) {
        asteroids.add(new Asteroid(tx, ty, r, angle, size, z++));
        notifySubscribers("asteroid");
    }

    // move the asteroid by its x and y velocity
    public void moveAsteroids() {
        for (Asteroid a: asteroids) {
            a.moveAsteroid();
        }
        notifySubscribers("asteroid");
    }

    // rotate the asteroid by its angle velocity
    public void spinAsteroids() {
        for (Asteroid a: asteroids) {
            a.spinAsteroid();
        }
        notifySubscribers("asteroid");
    }

    // returns null or the asteroid containing the point x, y
    public Asteroid contains(double x, double y) {
        ArrayList<Asteroid> allHit = new ArrayList<>();
        for (Asteroid a : asteroids) {
            if (a.contains(x,y)) {
                allHit.add(a);
            }
        }
        if (allHit.isEmpty()) {
            return null;
        }
        else {
            allHit.sort(Comparator.comparingInt(Asteroid::getZ));
            return allHit.get(allHit.size()-1);
        }
    }

    // returns a list of asteroids that have polygon points inside the area of a circle
    public ArrayList<Asteroid> areaContains(double x, double y, double r) {
        ArrayList<Asteroid> allHit = new ArrayList<>();
        for (Asteroid a : asteroids) {
            if (a.areaContains(x, y, r)) {
                allHit.add(a);
            }
        }
        if (allHit.isEmpty()) {
            return null;
        }
        else {
            return allHit;
        }
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
