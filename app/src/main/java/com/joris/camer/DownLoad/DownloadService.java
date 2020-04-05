package com.joris.camer.DownLoad;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hichip.callback.PlayLocalFileCallback;
import com.hichip.sdk.HiChipSDK;
import com.hichip.sdk.PlayLocal;
import com.joris.camer.MainActivity;
import com.joris.camer.Onvif.until.GetDetailForHtml;
import com.joris.camer.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.hichip.callback.PlayLocalFileCallback.LOCAL2MP4_STATE_START;

/**
 * Created by NIWA on 2017/3/19.
 */

public class DownloadService extends Service {


    private static final String TAG = "DownloadService";
    private DownLoadTask mDownLoadTask;
    private String downloadUrl;
    private DownloadBinder mBinder = new DownloadBinder();


    private static ArrayList<DownFileList> downlist;


    private static boolean initSdk = false;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder {

        public void start2toMp4(){
            if(!initSdk){
                HiChipSDK.init(new HiChipSDK.HiChipInitCallback(){

                    @Override
                    public void onSuccess(int i, int i1) {
                        initSdk = true;
                        PlayLocalinit("");
                    }

                    @Override
                    public void onFali(int i, int i1) {

                    }
                });
            }else{
                PlayLocalinit("");
            }

        }

        public void PlayLocalinit(String fileName){
            PlayLocal playLocal = new PlayLocal();


            playLocal.registerPlayLocalStateListener(new PlayLocalFileCallback(){
                @Override
                public void callbackplaylocal(int i, int i1, int i2, long l, int i3, int i4) {
                    if(i4==LOCAL2MP4_STATE_START){
                        Log.i("转换状态成功","");
                        start2toMp4();
                    }
                }
            });

            String s[]=fileName.split("/");
            String fileUrl=String.format("%s/%s/%s/%s",Environment.getExternalStorageDirectory().getAbsolutePath()+"/tempvideo",s[2],s[4],s[6]);
            fileUrl=fileUrl.replace(".","_");

            String outFile = String.format("%s/%s/%s/%s",Environment.getExternalStorageDirectory().getAbsolutePath()+"/out2Mp4",s[2],s[4],s[6]);
            outFile = outFile.replace(".","_");
            outFile = outFile.substring(0,outFile.length()-4)+".mp4";

            playLocal.Start2Mp4(fileUrl,outFile);
        }





        //定义同步下载数
        public   int downCount=0;
        //最大同步下载数
        private  int maxDownCount=4;
        /**
         *  开始下载
         * */
        public void startDownLoad(boolean states) {
            if (mDownLoadTask == null) {
                if(downlist==null){
                    try {
                        downlist = GetDetailForHtml.GetDetailDateForHtml("20200330");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            for(int i=0;i<downlist.size();i++){
                if(downlist.get(i).getDownSate()!=1){
                    downloadUrl = downlist.get(i).getDownUrl();
                    break;
                }
            }
            if(states){
                while (mBinder.downCount==mBinder.maxDownCount){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mDownLoadTask = new DownLoadTask(listener);
                mDownLoadTask.execute(downloadUrl + "?-usr=admin&-pwd=admin");//开始异步任务，传入url
                downCount++;
                startForeground(1, getNotification("下载中...", 0));
                Log.d(TAG, "开始下载的服务");
            }


        }

        public void pauseDownLoad(){
            if (mDownLoadTask != null){
                mDownLoadTask.pauseDownload();
            }
        }

        public void cancelDownLoad(){
            if (mDownLoadTask != null){
                mDownLoadTask.cancelDownload();
            }else{
                if(downloadUrl != null){

                    //下面三句是为了获取文件名字，然后对比手机存储内的，删除
                    String filename = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String derectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(derectory + filename);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);//关闭1号通知
                    stopForeground(true);
                    Log.d(TAG, "取消了");
                }
            }
        }


    }


    private DownLoadListener listener = new DownLoadListener() {
        @Override
        public void onProgress(int progress) {
            //设置进度条
            getNotificationManager().notify(1, getNotification("下载中....", progress));
        }

        @Override
        public void onSuccess() {
//            mDownLoadTask=null;
            for(int i=0;i<downlist.size();i++){
                if(downlist.get(i).getDownUrl()==downloadUrl){
                    downlist.get(i).setDownSate(1);
                    break;
                }
            }
            mBinder.downCount--;

            mBinder.startDownLoad(true);
            Log.d(TAG, "下载完url为："+downloadUrl);
        }

        @Override
        public void onFailed() {
            mDownLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("xiazai出错", -1));
            Log.d(TAG, "下载出错");
        }

        @Override
        public void onPaused() {
            mDownLoadTask = null;
            getNotificationManager().notify(1, getNotification("xiazai暂停", -1));
            Log.d(TAG, "下载暂停");
        }

        @Override
        public void onCanceled() {
            mDownLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("xiazai取消了", -1));
            Log.d(TAG, "下载取消了");
        }
    };

    /**
     * 获取系统状态栏信息服务
     */
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     *  显示进度封装
     * */
    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);//上下文
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //设置notification信息
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setContentTitle(title);
        if (progress >= 0) {
            //当Progress大于等于0时才显示进度
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }


}