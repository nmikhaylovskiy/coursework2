package sample;

import javafx.scene.Node;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

class SceneUtils {

    static void setOnMousePressedDragged(Node scene, BiConsumer<Double, Double> handler) {

        final AtomicReference<Double> prevX = new AtomicReference<>();
        final AtomicReference<Double> prevY = new AtomicReference<>();

        scene.setOnMousePressed(e -> {
            prevX.set(e.getSceneX());
            prevY.set(e.getSceneY());
        });

        scene.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                double dx = prevX.get() - e.getSceneX();
                double dy = prevY.get() - e.getSceneY();
                handler.accept(dx, dy);
            }
            prevX.set(e.getSceneX());
            prevY.set(e.getSceneY());
        });
    }

}
