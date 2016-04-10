package android.buraqueira.camiloporto.com.br.buraqueiraandroid;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    public static final int UPDATE_THRESHOLD = 200;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long sensorLastUpdate;
    private LocationManager locationManager;
    private LocationProvider gpsProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorLastUpdate = System.currentTimeMillis();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
        locationManager.requestLocationUpdates(gpsProvider.getName(), 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            long actualTime = event.timestamp;
            if (actualTime - sensorLastUpdate < UPDATE_THRESHOLD) { //FIXME SET adequate threshold
                return;
            }

            sensorLastUpdate = actualTime;
            Log.d("BURAQUEIRA", "aceleracao: [x, y, z, time] : [" + x + ", " + y + ", " + z + ", " + actualTime);

            //FIXME create ModelObject for ReadData. serialize into JSON and send to RESTFul service raw data.
            //FIXME identify the sender
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        long actualTime = location.getTime();

        Log.d("BURAQUEIRA", "localizacao: [lat, long, time] : [" + lat + ", " + lng + ", " + actualTime);
        //FIXME create ModelObject for ReadData. serialize into JSON and send to RESTFul service raw data
        //FIXME identify the sender
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
