package com.joris.camer;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.os.Message;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hichip.sdk.HiChipSDK;
import com.hichip.sdk.PlayLocal;
import com.joris.camer.DownLoad.DownFileList;
import com.joris.camer.Onvif.until.AXLog;
import com.joris.camer.Onvif.until.GetDetailForHtml;
import com.joris.camer.widget.IjkVideoView;
import com.joris.camer.widget.OnValueChangeListener;
import com.joris.camer.widget.TunlView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;

import static com.joris.camer.widget.TunlView.getTime;

public class BackPlayActivity extends Activity {
    private PlayLocal playLocal;
    private PlayLocal playLocal2;
    public Context mContext;
    private IjkVideoView mVideoView1;
    private IjkVideoView mVideoView2;
    private IjkVideoView mVideoView3;
    private IjkVideoView mVideoView4;
    private View divider1;
    private View divider2;

    private static ArrayList<DownFileList> playVideoList;

    private static boolean nextToday = false;

    private  static int filetime;


    TextView textView ;
    TunlView tunlView ;

    public ArrayList<IjkVideoView> videolist = new ArrayList<>();

    public static final int REFRESH_TIMERTV = 1;

    Handler handler =  new Handler(){

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            switch (message.what){
                case REFRESH_TIMERTV:
                    Message msg = Message.obtain();
                    msg.what = REFRESH_TIMERTV ;
                    handler.sendMessageDelayed(msg,1000);
                    getNowTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.activity_playback);
        mContext = this;

        mVideoView1 = findViewById(R.id.video_view1);
        mVideoView2 = findViewById(R.id.video_view2);
        mVideoView3 = findViewById(R.id.video_view3);
        mVideoView4 = findViewById(R.id.video_view4);

        divider1 = findViewById(R.id.divider1);
        divider2 = findViewById(R.id.divider2);


        textView = (TextView) findViewById(R.id.tv_time);
        tunlView = (TunlView) findViewById(R.id.tunlview);
        textView.setText(""+tunlView.getValue());

        addClient(1, String.format(Environment.getExternalStorageDirectory().getPath()+"/tmp.mp4"));

        tunlView.setmListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                AXLog.e("wzytest","滑动 value:"+value);
                mVideoView1.seekTo((int) value);
                mVideoView1.seekTo((int) value);
                mVideoView1.seekTo((int) value);
                mVideoView1.seekTo((int) value);

                textView.setText(getTime(value));
            }
        });

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(System.currentTimeMillis());
        textView.setText(dateStr);
        Message msg = Message.obtain();
        msg.what = REFRESH_TIMERTV ;
        handler.sendMessageDelayed(msg,1000);

    }


    // 初始化数据，播放器url初始化，时间戳视频list累计初始化
    public void initviewData(Date thedate) throws IOException {
        if(playVideoList==null || nextToday){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            playVideoList = GetDetailForHtml.GetDetailDateForHtml(simpleDateFormat.format(thedate));
        }

        while (true){
            tunlView.initData();
        }
    }
    /**
     * 添加客户端
     *
     * @param target    target
     * @param clientUrl clientUrl
     */
    private void addClient(int target, String clientUrl) {
        switch (target) {
            case 1:
                mVideoView1.setVisibility(View.VISIBLE);
                mVideoView1.setVideoPath(clientUrl);
                mVideoView1.start();
                break;
            case 2:
                mVideoView2.setVisibility(View.VISIBLE);
                mVideoView2.setVideoPath(clientUrl);
                mVideoView2.start();
                break;
            case 3:
                mVideoView3.setVisibility(View.VISIBLE);
                mVideoView3.setVideoPath(clientUrl);
                mVideoView3.start();
                break;
            case 4:
                mVideoView4.setVisibility(View.VISIBLE);
                mVideoView4.setVideoPath(clientUrl);
                mVideoView4.start();
                break;
            default:
                break;
        }
    }



    public void getNowTime(){
        String str = (String) textView.getText();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf1.parse(str);
            date.setTime(date.getTime()+1000);
            textView.setText(sdf1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiChipSDK.uninit();
    }





}
