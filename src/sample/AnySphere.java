package sample;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

class AnySphere extends Group {

    private static final float RADIUS = 5;

    private static Shape3D makeSphereCarcass(TriangulationMode mode, PolyhedronType type, int level) {
        switch (mode) {
            case JavaFX:
                return new Sphere(RADIUS, 1 << level << 2);
            case CenterFace:
            case CenterEdge:
                return new CustomSphere(RADIUS, mode, type, level);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static Shape3D makeSphere(TriangulationMode mode, PolyhedronType type, int level, DrawMode drawMode) {
        Shape3D sphere = makeSphereCarcass(mode, type, level);
        PhongMaterial material = new PhongMaterial(Color.RED);
        if (drawMode == DrawMode.FILL) {
            //material.setSpecularColor(Color.RED);
        }
        sphere.setMaterial(material);
        sphere.setDrawMode(drawMode);
        return sphere;
    }

    private static final int MIN_LEVEL = 0, MAX_LEVEL = 6, DEFAULT_LEVEL = 2;
    final IntegerProperty level = new SimpleIntegerProperty(DEFAULT_LEVEL);
    private TriangulationMode mode = TriangulationMode.JavaFX;
    private PolyhedronType type = PolyhedronType.Tetrahedron;
    private DrawMode drawMode = DrawMode.LINE;

    final IntegerProperty trianglesCount = new SimpleIntegerProperty();

    private void updateSphere() {
        Shape3D mySphere = makeSphere(mode, type, level.getValue(), drawMode);
        trianglesCount.setValue(TrianglesCounter.getTrianglesCount(mySphere));
        getChildren().setAll(mySphere);
    }

    private final DoubleExpression distance;

    AnySphere(DoubleExpression distance) {
        this.distance = distance;
        updateSphere();
    }

    private void updateDynamicLevel(double distance) {
        setLevel(calcLevel(distance,mode,type));
    }

    private final ChangeListener<Number> dynamicLevelUpdater = (observable, oldValue, newValue) -> updateDynamicLevel((double) newValue);

    private void setLevel(int newLevel) {
        if (MIN_LEVEL <= newLevel && newLevel <= MAX_LEVEL) {
            level.setValue(newLevel);
            updateSphere();
        }
    }

    void increaseLevel() {
        if (level.getValue() < MAX_LEVEL) {
            level.setValue(level.getValue() + 1);
            updateSphere();
        }
    }

    void decreaseLevel() {
        if (level.getValue() > MIN_LEVEL) {
            level.setValue(level.getValue() - 1);
            updateSphere();
        }
    }

    void setMode(TriangulationMode newMode) {
        mode = newMode;
        updateSphere();
    }

    void switchMode() {
        switch (mode) {
            case JavaFX:
                mode = TriangulationMode.CenterFace;
                break;
            case CenterFace:
                mode = TriangulationMode.CenterEdge;
                break;
            case CenterEdge:
                mode = TriangulationMode.JavaFX;
                break;
        }
        updateSphere();
    }

    void setType(PolyhedronType newType) {
        type = newType;
        updateSphere();
    }

    void switchType() {
        switch (type) {
            case Tetrahedron:
                type = PolyhedronType.Octahedron;
                break;
            case Octahedron:
                type = PolyhedronType.Icosahedron;
                break;
            case Icosahedron:
                type = PolyhedronType.Tetrahedron;
                break;
        }
        updateSphere();
    }

    void setDrawMode(DrawMode newMode) {
        drawMode = newMode;
        updateSphere();
    }

    void setDynamicTriangulation(boolean enabled) {
        if (enabled) {
            updateDynamicLevel(distance.getValue());
            distance.addListener(dynamicLevelUpdater);
        } else {
            distance.removeListener(dynamicLevelUpdater);
        }
    }

    private static int calcLevel(double distance, TriangulationMode mode, PolyhedronType type) {
        switch (mode) {
            case JavaFX:
                if (distance > 1500) return 1;
                if (distance <=1500 && distance>350) return 2;
                if (distance<=350 && distance>100) return  3;
                if(distance<=100 && distance >24) return  4;
                if(distance<=24) return 5;
            case CenterFace:
                switch (type) {
                    case Tetrahedron:
                        if (distance > 1600) return 2;
                        if (distance <=1600 && distance>400) return 3;
                        if (distance<=400 && distance>200) return  4;
                        if(distance<=200 && distance >48) return  5;
                        if(distance<=48) return 6;
                    case Octahedron:
                        if (distance > 1600) return 1;
                        if (distance <=1600 && distance>900) return 2;
                        if (distance<=900 && distance>152) return  3;
                        if(distance<=152 && distance >52) return  4;
                        if (distance<=52) return 5;
                    case Icosahedron:
                        if (distance > 2000) return 0;
                        if (distance <=2000 && distance>1000) return 1;
                        if (distance<=1000 && distance>164) return  2;
                        if(distance<=164 && distance >56) return  3;
                        if (distance<=56) return 4;
                }
            case CenterEdge:
                switch (type) {
                    case Tetrahedron:
                        if (distance > 2000) return 2;
                        if (distance <=2000 && distance>700) return 3;
                        if (distance<=700 && distance>250) return  4;
                        if(distance<=250 && distance >68) return  5;
                        if (distance<=68) return 6;
                    case Octahedron:
                        if (distance > 1428) return 1;
                        if (distance <=1428 && distance>552) return 2;
                        if (distance<=552 && distance>244) return  3;
                        if(distance<=244 && distance >136) return  4;
                        if (distance<=136 && distance>44) return 5;
                        if (distance<=44) return 6;
                    case Icosahedron:
                        if (distance > 2000) return 0;
                        if (distance <=2000 && distance>700) return 1;
                        if (distance<=700 && distance>480) return  2;
                        if(distance<=480 && distance >272) return  3;
                        if (distance<=272 && distance>80) return  4;
                        if(distance<=80 && distance >44) return  5;
                        if (distance<=44) return 6;
                }
        }
        return -1;
    }
    enum TriangulationMode {JavaFX, CenterFace, CenterEdge}

    enum PolyhedronType {Tetrahedron, Octahedron, Icosahedron}

    String describe() {
        StringBuilder b = new StringBuilder();
        b.append("level=").append(level);
        b.append("\tmode=").append(mode);
        if (mode != TriangulationMode.JavaFX)
            b.append("\ttype=").append(type);
        return b.toString();
    }

}
