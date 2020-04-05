package com.joris.camer;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawableWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.joris.camer.Onvif.imgLoad.GridViewAdaper;
import com.joris.camer.Onvif.imgLoad.ImageAndText;
import com.joris.camer.Onvif.onvifBean.Device;
import com.joris.camer.Onvif.until.OnvifSdk;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener{
    private static HashMap<String, Device> device = new HashMap<>();
    private static String[] ips;
    private static String chooseIP;

    List urls = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        Button bt = (Button)findViewById( R.id.btn_true );

        bt.setEnabled( false );
        bt.setOnClickListener( this );

        OnvifSdk.initSdk(this);
        OnvifSdk.findDevice(this);
        GridView gv = findViewById(R.id.Grid_view);
        while (OnvifSdk.searching){
            try {
                Thread.sleep(4000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }


        device = OnvifSdk.deviceHashMap;
        urls = Arrays.asList(device.keySet().toArray());

        List<ImageAndText> list = new ArrayList<ImageAndText>();
        for (int i=0;i<urls.size();i++){
            list.add(new ImageAndText("http://"+urls.get(i).toString().split(":")[0]+"/web/tmpfs/snap.jpg?-usr=admin&-pwd=admin", ""));
        }

        gv.setAdapter(new GridViewAdaper(this, list, gv));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                GridView gridView = (GridView) v.getParent();
                int count = gv.getCount();
                for (int i = 0;i<count;i++){
                    if(i==position){
                        v.setBackgroundColor(Color.GREEN);
                    }else{
                        v.setAlpha(1.0f);
                    }
                }
                urls.get(position);
                bt.setEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }



}
