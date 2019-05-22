package sample;

import javafx.scene.shape.TriangleMesh;

class MeshUtils {

    @FunctionalInterface
    interface FaceConsumer {
        void accept(
                int A, float Ax, float Ay, float Az,
                int B, float Bx, float By, float Bz,
                int C, float Cx, float Cy, float Cz
        );
    }

    static void iterateFaces(TriangleMesh mesh, FaceConsumer action) {
        assert mesh.getPoints().size() % 3 == 0;
        assert mesh.getFaces().size() % 6 == 0;

        for (int i = 0; i < mesh.getFaces().size(); i += 6) {
            int A = mesh.getFaces().get(i);
            int B = mesh.getFaces().get(i + 2);
            int C = mesh.getFaces().get(i + 4);

            float Ax = mesh.getPoints().get(A * 3);
            float Ay = mesh.getPoints().get(A * 3 + 1);
            float Az = mesh.getPoints().get(A * 3 + 2);

            float Bx = mesh.getPoints().get(B * 3);
            float By = mesh.getPoints().get(B * 3 + 1);
            float Bz = mesh.getPoints().get(B * 3 + 2);

            float Cx = mesh.getPoints().get(C * 3);
            float Cy = mesh.getPoints().get(C * 3 + 1);
            float Cz = mesh.getPoints().get(C * 3 + 2);

            action.accept(
                    A, Ax, Ay, Az,
                    B, Bx, By, Bz,
                    C, Cx, Cy, Cz
            );
        }
    }
}
