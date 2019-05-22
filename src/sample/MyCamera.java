package sample;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

class MyCamera extends PerspectiveCamera {

    private static final double INITIAL_DISTANCE = 20;
    static final double MIN_DISTANCE = 8;

    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate zoom = new Translate(0, 0, -INITIAL_DISTANCE);

    MyCamera() {
        super(true);
        setFarClip(Double.MAX_VALUE); // default 100
        getTransforms().addAll(rotateY, rotateX, zoom); // the order matters!
    }

    void rotate(double dx, double dy) {
        rotateX.setAngle(rotateX.getAngle() + dy);
        rotateY.setAngle(rotateY.getAngle() + dx);
    }

    void zoom(double delta) {
        double newZoom = zoom.getZ() + delta;
        if (newZoom > -MIN_DISTANCE) {
            newZoom = -MIN_DISTANCE;
        }
        zoom.setZ(newZoom);
    }

    final DoubleBinding distance = zoom.zProperty().multiply(-1);

}
