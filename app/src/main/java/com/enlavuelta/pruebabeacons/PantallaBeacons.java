package com.enlavuelta.pruebabeacons;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;
import com.estimote.sdk.internal.utils.EstimoteBeacons;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PantallaBeacons extends AppCompatActivity implements IPantalla {

    private BeaconManager beaconManager;
    private Region region;
    private BeaconSignal beaconSignal;
    ServicioBeacons servicioBeaconManager;

    private EditText beaconUUID;
    private EditText beaconMajor;
    private EditText beaconMinor;
    private EditText beaconDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_beacons);

        beaconUUID = (EditText) findViewById(R.id.beacon_id);
        beaconMajor = (EditText) findViewById(R.id.major);
        beaconMinor = (EditText) findViewById(R.id.minor);
        beaconDistance = (EditText) findViewById(R.id.distance);

        beaconManager = new BeaconManager(this);

        BeaconSignal.getInstance(this.getApplicationContext()).iniciarBeaconListener(this, beaconManager);

        /*
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    beaconUUID.setText(nearestBeacon.getProximityUUID().toString());
                    String major = (new Integer(nearestBeacon.getMajor())).toString();
                    String minor = (new Integer(nearestBeacon.getMinor())).toString();

                    int rssi = nearestBeacon.getRssi();
                    int measuredPower = nearestBeacon.getMeasuredPower();
                    double distanceDouble = Utils.computeAccuracy(nearestBeacon);
                    String distance = (new Double(distanceDouble)).toString();

                    beaconMajor.setText(major);
                    beaconMinor.setText(minor);
                    beaconDistance.setText(distance);
                }
            }
        });
        */

        region = new Region(
                "Ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                new Integer(47612),
                new Integer(19588));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void handlePostFinish(HashMap<String, String> datosBeacon){
        beaconUUID.setText(datosBeacon.get("beaconUUID"));
        Log.i("Entra", "Enra handle");
        beaconMajor.setText(datosBeacon.get("beaconMajor"));
        beaconMinor.setText(datosBeacon.get("beaconMinor"));
        beaconDistance.setText(datosBeacon.get("beaconDistance"));
    }

    public void handlePostErrorFinish(){

    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    public void activityCalcularPromedios(View view){
        Intent intent = new Intent(this, PromedioDistancias.class);
        startActivity(intent);
    }





    protected double calcularDistancia(int rssi, int measuredPower){
        if (rssi == 0){
            return -1.0;
        }

        double ratio = rssi*1.0/measuredPower;
        if (ratio < 1.0){
            return Math.pow(ratio, 10);
        }
        else{
            //double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            double accuracy = (0.42093) * Math.pow(ratio, 6.9476) + 0.54992;
            return accuracy;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pantalla_beacons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
