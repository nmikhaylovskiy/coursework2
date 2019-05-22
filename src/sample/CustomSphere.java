package sample;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CustomSphere extends MeshView {

    private static TriangleMesh makeTetrahedron(float r) {
        TriangleMesh mesh = new TriangleMesh();

        float f = (float) (1 / Math.sqrt(2));
        float k = (float) Math.sqrt(2f / 3f);

        mesh.getPoints().addAll(
                -r * k, 0, -f * r * k,
                +r * k, 0, -f * r * k,
                0, -r * k, +f * r * k,
                0, +r * k, +f * r * k
        );

        mesh.getFaces().addAll(
                0,0, 1,0, 2,0,
                1,0, 0,0, 3,0,
                2,0, 1,0, 3,0,
                0,0, 2,0, 3,0
        );

        return mesh;
    }

    private static TriangleMesh makeOctahedron(float r) {
        TriangleMesh mesh = new TriangleMesh();

        mesh.getPoints().addAll(
                0, 0, +r,
                0, 0, -r,
                0, +r, 0,
                0, -r, 0,
                +r, 0, 0,
                -r, 0, 0
        );

        mesh.getFaces().addAll(
                0,0,  4,0,  2,0,
                0,0,  2,0,  5,0,
                0,0,  3,0,  4,0,
                0,0,  5,0,  3,0,
                1,0,  2,0,  4,0,
                1,0,  5,0,  2,0,
                1,0,  4,0,  3,0,
                1,0,  3,0,  5,0
        );

        return mesh;
    }

    private static TriangleMesh makeIcosahedron(float r) {
        TriangleMesh mesh = new TriangleMesh();

        float f = (1 + (float) Math.sqrt(5)) / 2;
        float k = (float) Math.sqrt(2 / (5 + Math.sqrt(5)));

        mesh.getPoints().addAll(
                +r*k, +f*r*k, 0,
                -r*k, +f*r*k, 0,
                +r*k, -f*r*k, 0,
                -r*k, -f*r*k, 0,

                0, +r*k, +f*r*k,
                0, -r*k, +f*r*k,
                0, +r*k, -f*r*k,
                0, -r*k, -f*r*k,

                +f*r*k, 0, +r*k,
                -f*r*k, 0, +r*k,
                +f*r*k, 0, -r*k,
                -f*r*k, 0, -r*k
        );

        mesh.getFaces().addAll(
                0,0, 1,0, 4,0,
                1,0, 9,0, 4,0,
                4,0, 9,0, 5,0,
                5,0, 9,0, 3,0,
                2,0, 3,0, 7,0,

                3,0, 2,0, 5,0,
                7,0, 10,0, 2,0,
                0,0, 8,0, 10,0,
                0,0, 4,0, 8,0,
                8,0, 2,0, 10,0,

                8,0, 4,0, 5,0,
                8,0, 5,0, 2,0,
                1,0, 0,0, 6,0,
                11,0, 1,0, 6,0,
                3,0, 9,0, 11,0,

                6,0, 10,0, 7,0,
                3,0, 11,0, 7,0,
                11,0, 6,0, 7,0,
                6,0, 0,0, 10,0,
                9,0, 1,0, 11,0
        );

        return mesh;
    }

    private static TriangleMesh makePolyhedron(float r, AnySphere.PolyhedronType type) {
        switch (type) {
            case Tetrahedron:
                return makeTetrahedron(r);
            case Octahedron:
                return makeOctahedron(r);
            case Icosahedron:
                return makeIcosahedron(r);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static TriangleMesh makeMesh(float r, AnySphere.TriangulationMode mode, AnySphere.PolyhedronType type, int level) {
        TriangleMesh mesh = makePolyhedron(r, type);

        mesh.getTexCoords().addAll(0, 0); // dummy texture coordinates

        switch (mode) {
            case CenterFace:
                for (int i = 0; i < level; ++i) {
                    detailingStepCenterFace(r, mesh);
                }
                break;
            case CenterEdge:
                for (int i = 0; i < level; ++i) {
                    detailingStepCenterEdge(r, mesh);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }

        return mesh;
    }

    private static void detailingStepCenterFace(float r, TriangleMesh mesh) {
        // key - edge, val - centers of adjacent faces
        Map<ImmutableUnorderedIntPair, MutableIntPair> map = new HashMap<>();

        MeshUtils.iterateFaces(mesh, (A, Ax, Ay, Az, B, Bx, By, Bz, C, Cx, Cy, Cz) -> {
            // let D be center of triangle ABC
            float Dx = (Ax + Bx + Cx) / 3;
            float Dy = (Ay + By + Cy) / 3;
            float Dz = (Az + Bz + Cz) / 3;

            double l = Math.sqrt(Dx * Dx + Dy * Dy + Dz * Dz);
            Dx *= r / l;
            Dy *= r / l;
            Dz *= r / l;

            int j = mesh.getPoints().size() / 3;
            mesh.getPoints().addAll(Dx, Dy, Dz);
            map.computeIfAbsent(new ImmutableUnorderedIntPair(A, B), k -> MutableIntPair.empty()).add(j);
            map.computeIfAbsent(new ImmutableUnorderedIntPair(B, C), k -> MutableIntPair.empty()).add(j);
            map.computeIfAbsent(new ImmutableUnorderedIntPair(C, A), k -> MutableIntPair.empty()).add(j);
        });

        List<Integer> newFaces = new ArrayList<>();
        for (Map.Entry<ImmutableUnorderedIntPair, MutableIntPair> entry : map.entrySet())
        {
            IntPair E1E2 = entry.getKey(); // edge
            int E1 = E1E2.getFirst();
            int E2 = E1E2.getSecond();
            IntPair C1C2 = entry.getValue(); // faces centers
            int C1 = C1C2.getFirst();
            int C2 = C1C2.getSecond();
            newFaces.add(C2); newFaces.add(C1); newFaces.add(E1);
            newFaces.add(C1); newFaces.add(C2); newFaces.add(E2);
        }
        mesh.getFaces().clear();
        for (int f: newFaces) {
            mesh.getFaces().addAll(f, 0);
        }
    }

    // find k such that length of vector (x + ka, y + kb, z + kc) equalsal r
    private static double solve(
            float x, float y, float z,
            float a, float b, float c,
            float r
    ) {
        float _a = a * a + b * b + c * c;
        float _k = x * a + y * b + z * c;
        float _c = x * x + y * y + z * z - r * r;
        double _D = Math.sqrt(_k * _k - _a * _c);
        double ans1 = (-_k - _D) / _a;
        double ans2 = (-_k + _D) / _a;
        return Math.abs(ans1) < Math.abs(ans2) ? ans1 : ans2;
    }

    private static void detailingStepCenterEdge(float r, TriangleMesh mesh) {
        List<Integer> newFaces = new ArrayList<>();

        MeshUtils.iterateFaces(mesh, (A, Ax, Ay, Az, B, Bx, By, Bz, C, Cx, Cy, Cz) -> {
            float cx = (Ax + Bx) / 2;
            float cy = (Ay + By) / 2;
            float cz = (Az + Bz) / 2;

            float ax = (Bx + Cx) / 2;
            float ay = (By + Cy) / 2;
            float az = (Bz + Cz) / 2;

            float bx = (Cx + Ax) / 2;
            float by = (Cy + Ay) / 2;
            float bz = (Cz + Az) / 2;

            // scale (1) from sphere center
            double al = Math.sqrt(ax * ax + ay * ay + az * az); // Math.sqrt(...)
            ax *= r / al;
            ay *= r / al;
            az *= r / al;

            double bl = Math.sqrt(bx * bx + by * by + bz * bz); // Math.sqrt(...)
            bx *= r / bl;
            by *= r / bl;
            bz *= r / bl;

            double cl = Math.sqrt(cx * cx + cy * cy + cz * cz); // Math.sqrt(...)
            cx *= r / cl;
            cy *= r / cl;
            cz *= r / cl;

            // scale (2) along face normal
            /*float i = Ay * (Bz - Cz) + By * (Cz - Az) + Cy * (Az - Bz);
            float j = Az * (Bx - Cx) + Bz * (Cx - Ax) + Cz * (Ax - Bx);
            float k = Ax * (By - Cy) + Bx * (Cy - Ay) + Cx * (Ay - By);

            double al = solve(ax, ay, az, i, j, k, r);
            ax += al * i;
            ay += al * j;
            az += al * k;

            double bl = solve(bx, by, bz, i, j, k, r);
            bx += bl * i;
            by += bl * j;
            bz += bl * k;

            double cl = solve(cx, cy, cz, i, j, k, r);
            cx += cl * i;
            cy += cl * j;
            cz += cl * k;*/

            int a = mesh.getPoints().size() / 3;
            mesh.getPoints().addAll(ax, ay, az);

            int b = mesh.getPoints().size() / 3;
            mesh.getPoints().addAll(bx, by, bz);

            int c = mesh.getPoints().size() / 3;
            mesh.getPoints().addAll(cx, cy, cz);

            newFaces.add(A); newFaces.add(c); newFaces.add(b);
            newFaces.add(B); newFaces.add(a); newFaces.add(c);
            newFaces.add(C); newFaces.add(b); newFaces.add(a);
            newFaces.add(a); newFaces.add(b); newFaces.add(c); //
        });

        mesh.getFaces().clear();
        for (int f: newFaces) {
            mesh.getFaces().addAll(f, 0);
        }
    }

    CustomSphere(float radius, AnySphere.TriangulationMode mode, AnySphere.PolyhedronType type, int level) {
        super(makeMesh(radius, mode, type, level));
    }

}
