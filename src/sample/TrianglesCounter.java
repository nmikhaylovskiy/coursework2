package sample;

import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;

import java.lang.reflect.Field;

class TrianglesCounter {
    private static Field meshField;

    static {
        try {
            meshField = Sphere.class.getDeclaredField("mesh");
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldError(e.toString());
        }
        meshField.setAccessible(true);
    }

    @SuppressWarnings("deprecation")
    private static TriangleMesh getMesh(Sphere sphere) {
        sphere.impl_updatePeer();
        try {
            return (TriangleMesh) meshField.get(sphere);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.toString());
        }
    }

    private static TriangleMesh getMesh(Shape3D mySphere) {
        if (mySphere instanceof Sphere) {
            Sphere sphere = (Sphere) mySphere;
            return getMesh(sphere);
        } else if (mySphere instanceof CustomSphere) {
            CustomSphere sphere = (CustomSphere) mySphere;
            return (TriangleMesh) sphere.getMesh();
        } else {
            throw new IllegalStateException("Unknown sphere type");
        }
    }

    static int getTrianglesCount(Shape3D mySphere) {
        int n = getMesh(mySphere).getFaces().size();
        assert n % 6 == 0;
        return n / 6;
    }
}
