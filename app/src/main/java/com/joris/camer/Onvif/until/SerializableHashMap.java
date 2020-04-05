package com.joris.camer.Onvif.until;

import com.joris.camer.Onvif.onvifBean.Device;

import java.io.Serializable;
import java.util.HashMap;

public class SerializableHashMap implements Serializable {

    private HashMap<String, Device> map;

    public HashMap<String, Device> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Device> map) {
        this.map = map;
    }
}

