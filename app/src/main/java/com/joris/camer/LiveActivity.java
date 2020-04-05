package com.joris.camer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.joris.camer.Onvif.listener.IjkPlayerListener;
import com.joris.camer.Onvif.listener.OnDoubleClickListener;
import com.joris.camer.Onvif.until.FileUtils;
import com.joris.camer.Onvif.until.GetVideoStream;
import com.joris.camer.Onvif.until.LogClientUtils;
import com.joris.camer.widget.IjkVideoView;
import com.nanchen.crashmanager.UncaughtExceptionHandlerImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeMap;

import retrofit2.Retrofit;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 多路投屏直播LiveAcitvity
 * Created by HeyJobs on 2019/01/04
 */
public class LiveActivity extends AppCompatActivity {

    private static final String TAG = LiveActivity.class.getSimpleName();
    public static AppCompatActivity sInstance = null;
    private static Retrofit retrofit;
    NetWorkStateReceiver netWorkStateReceiver;
    private IjkVideoView mVideoView1;
    private IjkVideoView mVideoView2;
    private IjkVideoView mVideoView3;
    private IjkVideoView mVideoView4;
    private View divider1;
    private View divider2;
    private RelativeLayout ainvr_main;
    private Button playback;
    public Context mContext;
    private IMediaPlayer ijkPlayer;
    //四分屏模式还是全屏模式
    private boolean isMultiScreen = true;
    //保存客户端ip与通道数对应关系
    private HashMap<String, Integer> clientMap = new HashMap<>();
    //记录每个通道的投屏状态
    private TreeMap<Integer, Boolean> channelMap = new TreeMap<>();

    private String url = "";

    /*
    判断状态栏
     */
    private static boolean isStatusbarVisible(Activity activity) {
        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        boolean isStatusbarHide = ((uiOptions | View.SYSTEM_UI_FLAG_FULLSCREEN) == uiOptions);
        return !isStatusbarHide;
    }

    /*
    隐藏状态栏
     */
    public static void hideStatusBar(Activity activity) {
        if (isStatusbarVisible(activity)) {
            int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        setContentView(R.layout.activity_live);
//        setContentView(R.layout.activity__nine_screen);
        hideStatusBar(this);
        // 设置崩溃后自动重启 APP
        UncaughtExceptionHandlerImpl.getInstance().init(this, BuildConfig.DEBUG, true, 0, LiveActivity.class);
        mContext = this;
        initView();
        initListener();
        showAllVideo();
        wakeUp();


    }

    protected void onResume() {

        super.onResume();

    }





    int beforeRoom;
    //接受参数，显示4个视频画面
    private void showAllVideo() {
        //从配置文件读取四个摄像头的画面，按照次序一次播放
//        ArrayList<String> videoList=new ArrayList<>( MainActivity.megp );
//        videoList

        addClient(1, String.format("rtsp://192.168.0.108:554/11/ch1-s1?tcp"));
        addClient(2, String.format("rtsp://192.168.0.107:554/11/ch1-s1?tcp"));
        addClient(3, String.format("rtsp://192.168.0.110:554/11/ch1-s1?tcp"));
        addClient(4, String.format("rtsp://192.168.0.111:554/11/ch1-s1?tcp"));

    }

    private void registerNetStateReceiver() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);

    }

    private void initView() {
        ainvr_main=findViewById(R.id.ainvr_main);

        mVideoView1 = findViewById(R.id.video_view1);
        mVideoView2 = findViewById(R.id.video_view2);
        mVideoView3 = findViewById(R.id.video_view3);
        mVideoView4 = findViewById(R.id.video_view4);
        setVideoView(mVideoView1);
        setVideoView(mVideoView2);
        setVideoView(mVideoView3);
        setVideoView(mVideoView4);

        divider1 = findViewById(R.id.divider1);
        divider2 = findViewById(R.id.divider2);

        playback = findViewById(R.id.playback );
        playback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LiveActivity.this,BackPlayActivity.class);
                LiveActivity.this.startActivity(intent);
            }
        });
    }

    private void setVideoView(IjkVideoView mVideoView) {
        mVideoView.setIjkPlayerListener(new IjkPlayerListener() {
            @Override
            public void onIjkPlayer(IjkMediaPlayer ijkMediaPlayer) {
                //设置播放器option
                setOptions(ijkMediaPlayer);
            }
        });
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkPlayer = iMediaPlayer;
            }
        });
    }

    private void removeAllClient() {
        removeClient(1);
        removeClient(2);
        removeClient(3);
        removeClient(4);
    }

    private void initListener() {
        mVideoView1.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.OnDoubleClick() {
            @Override
            public void onDouble() {
                changeScreenMode(1);
            }
        }));
        mVideoView2.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.OnDoubleClick() {
            @Override
            public void onDouble() {
                changeScreenMode(2);
            }
        }));
        mVideoView3.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.OnDoubleClick() {
            @Override
            public void onDouble() {
                changeScreenMode(3);
            }
        }));
        mVideoView4.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.OnDoubleClick() {
            @Override
            public void onDouble() {
                changeScreenMode(4);
            }
        }));
    }

    //唤醒屏幕
    private void wakeUp() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager == null)
            return;
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
        wakeLock.acquire(1000);
        wakeLock.release();
    }

    /**
     * 配置播放器参数项
     */
    private void setOptions(IjkMediaPlayer ijkPlayer) {
        if (ijkPlayer == null)
            return;

        ijkPlayer.setScreenOnWhilePlaying(true);

//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);   //丢帧是在视频帧处理不过来的时候丢弃一些帧达到同步的效果
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);  //设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
//        //播放延时的解决方案
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1024);//设置播放前的探测时间 1,达到首屏秒开效果
//
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1);  // 无限读
//        //如果是rtsp协议，可以优先用tcp(默认是用udp)
////        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
//        ijkPlayer.setOption(1, "analyzemaxduration", 100L);
//        ijkPlayer.setOption(1, "flush_packets", 1L);
////        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);   //需要准备好后自动播放
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", 0);//不额外优化
////        ijkPlayer.setOption(4, "packet-buffering",  1);  //是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
//
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);//开启mediacodec硬解
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);  //自动旋屏
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);   //处理分辨率变化
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 1024L);//最大缓冲大小,单位kb
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 30);//最大帧率
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "r", 15);//帧速率(fps) 可以改，确认非标准桢率会导致音画不同步，所以只能设定为15或者29.97
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 2);   //默认最小帧数2
//
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "an", 0);////是否有声音
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 3);   //最大缓存时长
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);   //是否限制输入缓存数
//
//        //添加mepg支持
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
//        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec_mpeg4", 1);

        ijkPlayer.setOption(1, "analyzemaxduration", 100L);
        ijkPlayer.setOption(1, "probesize", 10240L);
        ijkPlayer.setOption(1, "flush_packets", 1L);
        ijkPlayer.setOption(4, "packet-buffering", 0L);
        ijkPlayer.setOption(4, "framedrop", 1L);

//        ijkPlayer.setOption(1, "probesize", 10240L);  //播放前的探测Size，默认是1M, 改小一点会出画面更快
        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 5);  //播放重连次数

        ijkPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1); //因为项目中多次调用播放器，有网络视频，resp，本地视频，还有wifi上http视频，所以得清空DNS才能播放WIFI上的视频

        ijkPlayer.setVolume(0,0);
    }

    /**
     * 选择空闲通道
     *
     * @param clientNum clientNum
     * @return idleChannel
     */
    private int selectIdleChannel(int clientNum) {
        for (int channel = 1; channel < clientNum; channel++) {
            if (!channelMap.get(channel)) {
                return channel;
            }
        }
        return clientNum;
    }

    /**
     * 获取当前投屏通道
     *
     * @return idleChannel
     */
    private int getCastingChannel() {
        for (int channel = 1; channel <= 4; channel++) {
            if (channelMap.get(channel)) {
                return channel;
            }
        }
        return 0;
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

    /**
     * 移除客户端
     *
     * @param target target
     */
    private void removeClient(int target) {
        switch (target) {
            case 1:
                mVideoView1.stopPlayback();
                mVideoView1.setVisibility(View.GONE);
                mVideoView1.release(true);
                break;
            case 2:
                mVideoView2.stopPlayback();
                mVideoView2.setVisibility(View.GONE);
                mVideoView2.release(true);
                break;
            case 3:
                mVideoView3.stopPlayback();
                mVideoView3.setVisibility(View.GONE);
                mVideoView3.release(true);
                break;
            case 4:
                mVideoView4.stopPlayback();
                mVideoView4.setVisibility(View.GONE);
                mVideoView4.release(true);
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏分割线
     */
    private void hideDivider() {
        divider1.setVisibility(View.GONE);
        divider2.setVisibility(View.GONE);
    }

    /**
     * 显示分割线
     */
    private void showDivider() {
        divider1.setVisibility(View.VISIBLE);
        divider2.setVisibility(View.VISIBLE);
    }

    /**
     * 进入全屏模式
     *
     * @param channel channel
     */
    private void enterFullScreen(int channel) {
        hideDivider();
        switch (channel) {
            case 1:
                mVideoView1.setRenderViewVisible();
                mVideoView2.setRenderViewGone();
                mVideoView3.setRenderViewGone();
                mVideoView4.setRenderViewGone();

                mVideoView1.setVisibility(View.VISIBLE);
                mVideoView2.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.GONE);
                break;
            case 2:
                mVideoView1.setRenderViewGone();
                mVideoView2.setRenderViewVisible();
                mVideoView3.setRenderViewGone();
                mVideoView4.setRenderViewGone();

                mVideoView1.setVisibility(View.GONE);
                mVideoView2.setVisibility(View.VISIBLE);
                mVideoView3.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.GONE);
                break;
            case 3:
                mVideoView1.setRenderViewGone();
                mVideoView2.setRenderViewGone();
                mVideoView3.setRenderViewVisible();
                mVideoView4.setRenderViewGone();

                mVideoView1.setVisibility(View.GONE);
                mVideoView2.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.VISIBLE);
                mVideoView4.setVisibility(View.GONE);
                break;
            case 4:
                mVideoView1.setRenderViewGone();
                mVideoView2.setRenderViewGone();
                mVideoView3.setRenderViewGone();
                mVideoView4.setRenderViewVisible();

                mVideoView1.setVisibility(View.GONE);
                mVideoView2.setVisibility(View.GONE);
                mVideoView3.setVisibility(View.GONE);
                mVideoView4.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 退出全屏模式
     */
    private void exitFullScreen() {

        mVideoView1.setRenderViewVisible();
        mVideoView2.setRenderViewVisible();
        mVideoView3.setRenderViewVisible();
        mVideoView4.setRenderViewVisible();

        showDivider();
        mVideoView1.setVisibility(View.VISIBLE);
        mVideoView2.setVisibility(View.VISIBLE);
        mVideoView3.setVisibility(View.VISIBLE);
        mVideoView4.setVisibility(View.VISIBLE);
    }

    /**
     * 切换分屏模式
     *
     * @param channel channel
     */
    private void changeScreenMode(int channel) {
        isMultiScreen = !isMultiScreen;
        if (isMultiScreen) {
            enterFullScreen(channel);
        } else {
            exitFullScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sInstance = null;
        unregisterReceiver(netWorkStateReceiver);

        mVideoView1.stopPlayback();
        mVideoView1.release(true);
        mVideoView2.stopPlayback();
        mVideoView2.release(true);
        mVideoView3.stopPlayback();
        mVideoView3.release(true);
        mVideoView4.stopPlayback();
        mVideoView4.release(true);

        IjkMediaPlayer.native_profileEnd();

    }

    private void reStartActivity() {
        if (Build.VERSION.SDK_INT >= 11) {
            this.recreate();
        } else {
            this.finish();
            this.startActivity(this.getIntent());
        }
    }

    public class NetWorkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

//            System.out.println("网络状态发生变化");
//            Toast.makeText(context, "网络状态发生变化", Toast.LENGTH_SHORT).show();
            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
                } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                    Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
                } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    Toast.makeText(context, "WIFI已断开,移动数据已连接,重连摄像头", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
                }
//API大于23时使用下面的方式进行网络监听
            } else {

                System.out.println("API level 大于23");
                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                    sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                }
//                Toast.makeText(context, sb.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    //设置网络背景图
    private void setNetBackGround(final String imgUrl){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = (InputStream) new URL(imgUrl).getContent();
                    final Drawable d = Drawable.createFromStream(is, "src");
                    is.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ainvr_main.setBackground(d);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

