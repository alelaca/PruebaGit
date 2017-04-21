package com.enlavuelta.pruebabeacons;

import java.util.HashMap;

/**
 * Created by Laka on 16/03/2016.
 */
public interface IPantalla {
    public void handlePostFinish(HashMap<String, String> datosBeacon);

    public void handlePostErrorFinish();
}
