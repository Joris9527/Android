package com.joris.camer.Onvif.until;

import android.os.Environment;
import android.util.Log;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.File;

public class GetVideoStream {
    public static boolean isStart = true;
    public static void frameRecord(String inputFile, String outputFile, int audioChannel) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {

        isStart = true;// 该变量建议设置为全局控制变量，用于控制录制结束
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        grabber.setOption("rtsp_transport","udp");
        grabber.setFrameRate(20);
        grabber.setVideoBitrate(3000000);
        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+outputFile);
        if(!file.exists()){
            file.createNewFile();
        }
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(file, 1280, 720,audioChannel);
        recorder.setFrameRate(20);
        recorder.setVideoBitrate(3000000);
        recordByFrame(grabber, recorder);
    }

    private static void recordByFrame(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder)
            throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        try {//建议在线程中使用该方法
            grabber.start();
            recorder.start();
            Frame frame = null;
            Log.i("录像服务","开始录像");
            while (isStart&& (frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }
            recorder.stop();
            grabber.stop();
        } finally {
            if (grabber != null) {
                grabber.stop();
            }
        }
    }
}
