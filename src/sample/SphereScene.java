package sample;

import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

class SphereScene {

    private static final int W = 700, H = 700;

    final MyCamera camera = new MyCamera();
    final AnySphere sphere = new AnySphere(camera.distance);

    final SubScene scene = new SubScene(new Group(camera, sphere), W, H);

    SphereScene() {
        scene.setFill(Color.ALICEBLUE);
        scene.setCamera(camera);

        SceneUtils.setOnMousePressedDragged(scene, (mouseDx, mouseDy) -> {
            double angleDx = +mouseDx / H * 360;
            double angleDy = -mouseDy / W * 360;
            camera.rotate(angleDx, angleDy);
        });
        scene.setOnScroll(e -> {
            double delta = e.getDeltaY() / 10;
            camera.zoom(delta);
        });
    }
}
