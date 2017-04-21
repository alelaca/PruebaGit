package com.enlavuelta.pruebabeacons;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.HashMap;
import java.util.List;

public class ServicioBeacons extends Service {

    private BeaconManager beaconManager;
    private IPantalla pantalla;

    public ServicioBeacons(Context context, IPantalla pantalla) {
        beaconManager = new BeaconManager(context);
        this.pantalla = pantalla;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    String beaconUUID = nearestBeacon.getProximityUUID().toString();
                    String major = (new Integer(nearestBeacon.getMajor())).toString();
                    String minor = (new Integer(nearestBeacon.getMinor())).toString();

                    int rssi = nearestBeacon.getRssi();
                    int measuredPower = nearestBeacon.getMeasuredPower();
                    double distanceDouble = Utils.computeAccuracy(nearestBeacon);
                    String distance = (new Double(distanceDouble)).toString();

                    // llama a la pantalla
                    HashMap<String, String> mapa = new HashMap<String, String>();
                    mapa.put("beaconUUID", beaconUUID);
                    mapa.put("beaconMajor", major);
                    mapa.put("beaconMinor", minor);
                    mapa.put("beaconDistance", distance);

                    pantalla.handlePostFinish(mapa);

                    Log.i("Entra", "");
                }
            }
        });

        return null;
    }
}
