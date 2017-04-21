package com.enlavuelta.pruebabeacons;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntentServicioBeacons extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.enlavuelta.pruebabeacons.action.FOO";
    private static final String ACTION_BAZ = "com.enlavuelta.pruebabeacons.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.enlavuelta.pruebabeacons.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.enlavuelta.pruebabeacons.extra.PARAM2";

    private IPantalla pantalla;
    private Context context;

    private BeaconManager beaconManager;

    public IntentServicioBeacons() {
        super("IntentServicioBeacons");
    }

    public void setParameters(Context context, IPantalla pantalla){
        this.pantalla = pantalla;
        this.context = context;
        this.beaconManager = new BeaconManager(context);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public void startActionFoo() {
        Intent intent = new Intent(context, IntentServicioBeacons.class);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, IntentServicioBeacons.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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

                    Log.i("Entra", "Entra listener");
                }
            }
        });
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
