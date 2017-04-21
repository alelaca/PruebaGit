package com.enlavuelta.pruebabeacons;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;

import java.util.List;
import java.util.UUID;

public class PromedioDistancias extends AppCompatActivity {

    private BeaconManager beaconManager;
    private Region region;

    private int distanciaAMedir = 0;
    private double promedioDistancia = 0;
    private double maxDistancia = 0;
    private double minDistancia = 32000;
    private int cantMedidas = 0;
    private int medidasTomadas = 0;

    private boolean calcularPromedio = false;

    private EditText beaconUUID;
    private EditText beaconMajor;
    private EditText beaconMinor;
    private EditText beaconDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promedio_distancias);


        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    //int rssi = nearestBeacon.getRssi();
                    //int measuredPower = nearestBeacon.getMeasuredPower();
                    double distanceDouble = Utils.computeAccuracy(nearestBeacon);
                    String distance = (new Double(distanceDouble)).toString();

                    if (calcularPromedio){
                        if (medidasTomadas < cantMedidas){
                            promedioDistancia += distanceDouble;
                            medidasTomadas ++;

                            if (distanceDouble > maxDistancia){
                                maxDistancia = distanceDouble;
                            }
                            if (distanceDouble < minDistancia){
                                minDistancia = distanceDouble;
                            }

                            TextView distanciaTomada = (TextView) findViewById(R.id.distancia_medida);
                            distanciaTomada.setText(String.format("%.2f", distanceDouble));

                            TextView medidasRestantes = (TextView) findViewById(R.id.medidas_restantes);
                            medidasRestantes.setText(new Integer(medidasTomadas).toString());
                        }
                        else{
                            // calcular el promedio e imprimirlo, max y min tambien
                            if (medidasTomadas != 0){
                                // imprimir promedio
                                String resource = "textViewPROM_" + distanciaAMedir + "m";
                                int idRutaImagen =  getResources().getIdentifier(
                                        resource,
                                        "id",
                                        getPackageName());
                                TextView textView = (TextView) findViewById(idRutaImagen);
                                textView.setText(String.format("%.2f", promedioDistancia / medidasTomadas));

                                // imprimir MAX
                                 resource = "textViewMAX_" + distanciaAMedir + "m";
                                idRutaImagen =  getResources().getIdentifier(
                                        resource,
                                        "id",
                                        getPackageName());
                                textView = (TextView) findViewById(idRutaImagen);
                                textView.setText(String.format("%.2f", maxDistancia));

                                // imprimir MIN
                                resource = "textViewMIN_" + distanciaAMedir + "m";
                                idRutaImagen =  getResources().getIdentifier(
                                        resource,
                                        "id",
                                        getPackageName());
                                textView = (TextView) findViewById(idRutaImagen);
                                textView.setText(String.format("%.2f", minDistancia));
                            }

                            // resetear valores para tomar otra medida
                            maxDistancia = 0;
                            minDistancia = 32000;
                            promedioDistancia = 0;
                            medidasTomadas = 0;
                            calcularPromedio = false;
                        }
                    }
                }
            }
        });

        region = new Region(
                "Ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                new Integer(47612),
                new Integer(19588));
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


    public void ejecutarPromedios(View view){
        calcularPromedio = true;
        distanciaAMedir = new Integer(((EditText) findViewById(R.id.distancia_a_calcular)).getText().toString());
        cantMedidas = new Integer(((EditText) findViewById(R.id.cantidad_medidas)).getText().toString());

        maxDistancia = 0;
        minDistancia = 32000;
        promedioDistancia = 0;
        medidasTomadas = 0;
    }

    public void cancelarPromedios(View view){
        maxDistancia = 0;
        minDistancia = 32000;
        promedioDistancia = 0;
        medidasTomadas = 0;
        calcularPromedio = false;
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
}
