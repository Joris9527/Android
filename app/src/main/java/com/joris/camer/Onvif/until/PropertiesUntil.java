package com.joris.camer.Onvif.until;


import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUntil {
    private static String configPath = "DeviceSource.properties";

    public static Properties loadProperties(Context context) {
        Properties properties = new Properties();
        try {
            InputStream in = context.getAssets().open(configPath);
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }



    //修改
    public static String setValue(String key,String value) {

        File config = new File(configPath);
        if (!config.exists()) {
            try{
                config.createNewFile();
            }catch (Exception e){
            }
        }
        Properties props = new Properties();
        try{
            FileInputStream is = new FileInputStream(configPath);
            props.load(is);
        }catch (Exception e){
        }
        props.setProperty(key, value);

        try {
            FileOutputStream out = new FileOutputStream(configPath);
            props.store(out, null);
        } catch (Exception e) {

        }
        return "";
    }

}