package ar.edu.untref.adquisiciondedatos.tpfinal;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;

public class Cubo {

    private FloatBuffer bufferVertices;
    private int numCaras = 6;

    private float[][] colores = {

            {1.0f, 0.5f, 0.0f, 1.0f},
            {1.0f, 0.0f, 1.0f, 1.0f},
            {0.0f, 1.0f, 0.0f, 1.0f},
            {0.0f, 0.0f, 1.0f, 1.0f},
            {1.0f, 0.0f, 0.0f, 1.0f},
            {1.0f, 1.0f, 0.0f, 1.0f}
    };

    private float[] vertices = {

            // Frente
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Atras
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // Izquierda
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,

            // Derecha
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            // Arriba
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Abajo
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f
    };

    public Cubo() {

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        bufferVertices = vbb.asFloatBuffer();
        bufferVertices.put(vertices);
        bufferVertices.position(0);
    }

    public void dibujar(GL10 gl) {

        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufferVertices);

        for (int cara = 0; cara < numCaras; cara++) {

            gl.glColor4f(colores[cara][0], colores[cara][1], colores[cara][2], colores[cara][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, cara * 4, 4);
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }
}
