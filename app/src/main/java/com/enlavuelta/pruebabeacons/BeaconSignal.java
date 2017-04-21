package com.enlavuelta.pruebabeacons;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Laka on 17/02/2016.
 */
public class BeaconSignal {
    private static BeaconSignal instance;

    /**
     * Metodo privado para que no se pueda utilizar el constructor
     */
    private BeaconSignal(){
    }


    public static synchronized BeaconSignal getInstance(Context ctx){
        if (instance == null){
            instance = new BeaconSignal();
        }
        return instance;
    }


    public void iniciarBeaconListener(final IPantalla pantalla, BeaconManager beaconManager) {
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
                }
            }
        });
    }
}
