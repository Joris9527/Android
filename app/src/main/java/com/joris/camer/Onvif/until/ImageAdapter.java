package com.joris.camer.Onvif.until;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.joris.camer.Onvif.onvifBean.Device;
import com.joris.camer.R;

import org.bytedeco.librealsense.device;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private static HashMap<String, Device> device = OnvifSdk.deviceHashMap;
    int deviceLength = device.keySet().toArray().length;

    public String[] picUrl=device.keySet().toArray(new String[deviceLength]);

    //public Integer[] picUrl=OnvifSdk.deviceHashMap;
    public ImageAdapter(Context c)
    {
        mContext=c;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return picUrl.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //Object o= picUrl[position].toString();
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        //picUrl[position];
        return 0;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ImageView imageview;
        if(convertView==null)
        {
            imageview=new ImageView(mContext);
            imageview.setId(position);
            imageview.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageview.setPadding(8,8,8,8);
            imageview.setBackgroundColor(Color.GREEN);
        }
        else
        {
            imageview=(ImageView) convertView;
        }
        //imageview.setImageURI(new Uri(picUrl[position]));
        String imageUrl="http://"+picUrl[position].toString()+"/web/tmpfs/snap.jpg?-usr=admin&-pwd=admin";
        Glide.with(mContext)
                .load(imageUrl)
                .into(imageview);
//        imageview.setImageResource(picUrl[position]);
        //Bitmap bm = null;
        //imageview.setImageBitmap(getImageBitmap(picUrl[position]));
        return imageview;

    }

    //public Bitmap getImageBitmap(String url) {
    //    URL imgUrl = null;
    //    Bitmap bitmap = null;
    //    try {
    //        imgUrl = new URL();
    //        HttpURLConnection conn = (HttpURLConnection) imgUrl
    //                .openConnection();
    //        conn.setDoInput(true);
    //        conn.connect();
    //        InputStream is = conn.getInputStream();
    //        bitmap = BitmapFactory.decodeStream(is);
    //        is.close();
    //    } catch (MalformedURLException e) {
    //        // TODO Auto-generated catch block
    //        e.printStackTrace();
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //    return bitmap;
    //}




}