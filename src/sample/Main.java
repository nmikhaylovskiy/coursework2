package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.DrawMode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private final SphereScene sphereScene = new SphereScene();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.setTitle("");

        primaryStage.setScene(new Scene(new HBox(
                UiUtils.makeVBox(
                UiUtils.makeHBox(
                        new Text("Уровень детализации:"),
                        UiUtils.makeButton("−", sphereScene.sphere::decreaseLevel),
                        UiUtils.makeBindedText(sphereScene.sphere.level.asString()),
                        UiUtils.makeButton("+", sphereScene.sphere::increaseLevel)
                ),
                new RadioGroupBuilder("Mode:")
                        .add("JavaFX", () -> sphereScene.sphere.setMode(AnySphere.TriangulationMode.JavaFX), true)
                        .add("CenterFace", () -> sphereScene.sphere.setMode(AnySphere.TriangulationMode.CenterFace))
                        .add("CenterEdge", () -> sphereScene.sphere.setMode(AnySphere.TriangulationMode.CenterEdge))
                        .build(),
                new RadioGroupBuilder("Type:")
                        .add("Tetrahedron", () -> sphereScene.sphere.setType(AnySphere.PolyhedronType.Tetrahedron), true)
                        .add("Octahedron", () -> sphereScene.sphere.setType(AnySphere.PolyhedronType.Octahedron))
                        .add("Icosahedron", () -> sphereScene.sphere.setType(AnySphere.PolyhedronType.Icosahedron))
                        .build(),
                new RadioGroupBuilder("Draw mode:")
                        .add("Line", () -> sphereScene.sphere.setDrawMode(DrawMode.LINE), true)
                        .add("Fill", () -> sphereScene.sphere.setDrawMode(DrawMode.FILL))
                        .build(),
                UiUtils.makeBindedText(Bindings.format("Число треугольников: %d", sphereScene.sphere.trianglesCount)),
                UiUtils.makeBindedText(sphereScene.camera.distance.asString("Расстояние: %f")),
                UiUtils.makeCheckBox("Динамическая триангуляция", sphereScene.sphere::setDynamicTriangulation)
                ),
                sphereScene.scene
        )));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
