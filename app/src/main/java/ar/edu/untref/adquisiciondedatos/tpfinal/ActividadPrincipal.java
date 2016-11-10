package ar.edu.untref.adquisiciondedatos.tpfinal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ar.edu.untref.adquisiciondedatos.tpfinal.utilidades.OpenGLRenderer;

public class ActividadPrincipal extends AppCompatActivity implements SensorEventListener {

    private static final float ALPHA = 0.3f;

    private SensorManager sensorManager;
    private Sensor acelerometro;
    private Sensor magnetometro;

    float[] gravedad;
    float[] geomagnetismo;
    float pitch = 0;
    float roll = 0;
    float nuevoPithc = 0;
    float nuevoRoll = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometro = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GLSurfaceView view = new GLSurfaceView(this);
        view.setRenderer(new OpenGLRenderer());

        setContentView(view);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, acelerometro,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometro,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private float restringirAngulo(float angulo) {
        while (angulo >= 180) angulo -= 360;
        while (angulo < -180) angulo += 360;
        return angulo;
    }

    private float calcularDiferencia(float x, float y) {

        float diferencia = x - y;

        //Nos aseguramos que la dif sea <= a 180
        diferencia = restringirAngulo(diferencia);

        y += ALPHA * diferencia;
        //Nos aseguramos que Y quede entre [-180 y 180]
        y = restringirAngulo(y);

        return y;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravedad = event.values;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetismo = event.values;
        }

        if (gravedad != null && geomagnetismo != null) {

            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, gravedad, geomagnetismo);

            if (success) {

                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                nuevoPithc = -(float) ((Math.toDegrees(orientation[1])));
                nuevoRoll = (float) ((Math.toDegrees(orientation[2])));

                pitch = calcularDiferencia(nuevoPithc, pitch);
                roll = calcularDiferencia(nuevoRoll, roll);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
