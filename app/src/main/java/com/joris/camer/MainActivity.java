package com.joris.camer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hichip.callback.ICameraDownloadCallback;
import com.hichip.content.HiChipDefines;
import com.hichip.control.HiCamera;
import com.hichip.sdk.HiChipSDK;
import com.joris.camer.DownLoad.DownFileList;
import com.joris.camer.DownLoad.DownLoadListener;
import com.joris.camer.DownLoad.DownloadService;
import com.joris.camer.Onvif.until.GetDetailForHtml;
import com.joris.camer.Onvif.until.GetVideoStream;
import com.joris.camer.Onvif.until.PropertiesUntil;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static Context mContext;
    public static String[] megp=null;
    public static String[] ips = null;


    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;

    private ImageView delete_img1;
    private ImageView delete_img2;
    private ImageView delete_img3;
    private ImageView delete_img4;

    private ImageView ico_img1;
    private ImageView ico_img2;
    private ImageView ico_img3;
    private ImageView ico_img4;

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;

    private Button btn1;
    private Button btn2;

    private HiCamera hc;

    ArrayList<DownFileList> list;

    private static final String TAG = "DownloadService";
    private DownloadService.DownloadBinder mBinder;
    private final static String ACTION_UPDATE = "com.chris.download.service.UPDATE";
    private final static String ACTION_FINISHED = "com.chris.download.service.FINISHED";

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initView(){
        img1=(ImageView)findViewById(R.id.img1);
        img2=(ImageView)findViewById(R.id.img2);
        img3=(ImageView)findViewById(R.id.img3);
        img4=(ImageView)findViewById(R.id.img4);

        delete_img1 = (ImageView)findViewById(R.id.iv_delete1);
        delete_img2 = (ImageView)findViewById(R.id.iv_delete2);
        delete_img3 = (ImageView)findViewById(R.id.iv_delete3);
        delete_img4 = (ImageView)findViewById(R.id.iv_delete4);

        ico_img1 = findViewById( R.id.icLine_one );
        ico_img2= findViewById( R.id.icLine_two );
        ico_img3= findViewById( R.id.icLine_three );
        ico_img4= findViewById( R.id.icLine_four );

        text1 = findViewById( R.id.tv_text1 );
        text2 = findViewById( R.id.tv_text2 );
        text3 = findViewById( R.id.tv_text3 );
        text4 = findViewById( R.id.tv_text4 );

        btn1=findViewById( R.id.liveVideo );
        btn2=findViewById( R.id.playback );

        btn1.setOnClickListener( this );
        btn2.setOnClickListener( this );



        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);


        delete_img1.setOnClickListener(this);
        delete_img2.setOnClickListener(this);
        delete_img3.setOnClickListener(this);
        delete_img4.setOnClickListener(this);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);


        //判断权限够不够，不够就给
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            //权限够了这里处理逻辑
            Log.d(TAG, "权限够了");
        }


    }

    public void ImgIntil(){
        for (int i = 0;i<4;i++){
            if(ips[i]!=""){
                Glide.with(this).load(ips[i]).into((img1));
            }
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        unbindService(mConnection);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Activity onResume");

        Intent it = new Intent(this, DownloadService.class);
        bindService(it, mConnection, BIND_AUTO_CREATE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                if(img1.getDrawable()==null){
                    mBinder.startDownLoad(true);
//                    Intent intent1 =new Intent(MainActivity.this,SearchActivity.class);
//                    intent1.putExtra("num",0);
//                    MainActivity.this.startActivity(intent1);
                }
                break;
            case R.id.img2:
                if(img2.getDrawable()==null){
                    Intent intent1 =new Intent(MainActivity.this,SearchActivity.class);
                    intent1.putExtra("num",0);
                    MainActivity.this.startActivity(intent1);
                }
                break;
            case R.id.img3:
                if(img3.getDrawable()==null){
                    Intent intent1 =new Intent(MainActivity.this,SearchActivity.class);
                    intent1.putExtra("num",0);
                    MainActivity.this.startActivity(intent1);
                }
                break;
            case R.id.img4:
                if(img4.getDrawable()==null){
                    Intent intent1 =new Intent(MainActivity.this,SearchActivity.class);
                    intent1.putExtra("num",0);
                    MainActivity.this.startActivity(intent1);
                }
                break;
            case R.id.iv_delete1:
                new AlertDialog.Builder(this).setTitle( "确定删除摄像头？" )
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PropertiesUntil.setValue("deviceNumberIp1","0" );
                                PropertiesUntil.setValue("deviceNumberIsOnLine1","0" );
                                startActivity( getIntent() );
                                //这里是执行的方法
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                                finish();
                            }
                        }).show();
                break;

            case R.id.iv_delete2:
                new AlertDialog.Builder(this).setTitle( "确定删除摄像头？" )
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PropertiesUntil.setValue("deviceNumberIp2","0" );
                                PropertiesUntil.setValue("deviceNumberIsOnLine2","0" );
                                startActivity( getIntent() );
                                //这里是执行的方法
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                                finish();
                            }
                        }).show();
                break;

            case R.id.iv_delete3:
                new AlertDialog.Builder(this).setTitle( "确定删除摄像头？" )
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PropertiesUntil.setValue("deviceNumberIp3","0" );
                                PropertiesUntil.setValue("deviceNumberIsOnLine3","0" );
                                startActivity( getIntent() );
                                //这里是执行的方法
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                                finish();
                            }
                        }).show();
                break;

            case R.id.iv_delete4:
                new AlertDialog.Builder(this).setTitle( "确定删除摄像头？" )
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PropertiesUntil.setValue("deviceNumberIp4","0" );
                                PropertiesUntil.setValue("deviceNumberIsOnLine4","0" );
                                startActivity( getIntent() );
                                //这里是执行的方法
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                                finish();
                            }
                        }).show();
                break;
            case R.id.liveVideo:
                Intent intent = new Intent( MainActivity.this,LiveActivity.class );
                startActivity(intent);
                break;
            case R.id.playback:
                Intent intent2 = new Intent( MainActivity.this,BackPlayActivity.class );
                startActivity(intent2);
                break;
        }
    }

    //获取到权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限够了处理逻辑
                    Log.d(TAG, "权限够了,逻辑");
                } else {
                    Toast.makeText(this, "权限不够，程序将退出", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }


}




