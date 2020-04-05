package com.joris.camer.Onvif.until;


import com.hichip.callback.PlayLocalFileCallback;
import com.hichip.control.HiCamera;
import com.hichip.sdk.HiChipSDK;
import com.hichip.sdk.PlayLocal;

import java.util.ArrayList;
import java.util.List;

public class DownLoadToStart2Mp4 {

    private static boolean isNew = false;
    private int initState=-1;
    private int initSDK = -1;
    PlayLocal pl = new PlayLocal();


    public int init(){
        initSDK = 0;
        HiChipSDK.init(new HiChipSDK.HiChipInitCallback() {
            @Override
            public void onSuccess(int i, int i1) {
                initState=1;
                pl.registerPlayLocalStateListener(new PlayLocalFileCallback() {
                    @Override
                    public void callbackplaylocal(int i, int i1, int i2, long l, int i3, int i4) {
                        if(i4==LOCAL2MP4_STATE_END){

                        }
                    }
                });
            }

            @Override
            public void onFali(int i, int i1) {

            }
        });
        return initState;
    }

    public void Start2toMp4(String url){
        while (true){
            if(initState>0){

                pl.Start2Mp4(url,url.split(".")[0]+".mp4");

            }else{
                if(initSDK<0){
                    init();
                }
            }
        }


    }
}
