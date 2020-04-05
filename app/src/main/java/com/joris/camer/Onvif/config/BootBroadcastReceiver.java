package com.joris.camer.Onvif.config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.joris.camer.LiveActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("ACTION_BOOT_COMPLETED")) {
            Intent bootStartIntent = new Intent(context, LiveActivity.class);
            bootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootStartIntent);
        }
    }
}
