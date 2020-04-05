package com.joris.camer.Onvif.until;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Spring on 2015/11/7.
 * 下载工具类
 */
public class HttpDownloader {
    private URL url = null;
    private final String TAG = "TAG";

    /**
     * 读取文本文件
     * @param urlStr url路径
     * @return 文本信息
     * 根据url下载文件，前提是这个文件中的内容是文本，
     * 1.创建一个URL对象
     * 2.通过URL对象，创建一个Http连接
     * 3.得到InputStream
     * 4.从InputStream中得到数据
     */
    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader bufferedReader = null;

        try {
            url = new URL(urlStr);
            //创建http连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //使用IO流读取数据
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("TAG","下载txt文件");
        Log.e("TAG",sb.toString());
        return sb.toString();
    }

    /**
     * 读取任何文件
     * 返回-1 ，代表下载失败。返回0，代表成功。返回1代表文件已经存在
     *
     * @param urlStr
     * @param path
     * @param fileName
     * @return
     */
    public ArrayList downlaodFile(ArrayList urlStr, String path, String fileName) {
        int inter = 0;
        InputStream input = null;
        ArrayList thelist = null;

        for (int i=0;i<urlStr.size();i++){
            try {
                FileUtils fileUtils = new FileUtils();
                if (fileUtils.isFileExist(path + fileName)) {
                    inter = 1;
                } else {
                    input = getInputStearmFormUrl(urlStr.get(i).toString());
                    File resultFile = fileUtils.write2SDFromInput(path,fileName,input);
                    if (resultFile == null)
                        inter = -1;
                }
            } catch (IOException e) {
                e.printStackTrace();
                inter= -1;
            }
            finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return  thelist;
    }


    public InputStream getInputStearmFormUrl(String urlStr) throws IOException {
        url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        InputStream input = urlConn.getInputStream();
        return input;
    }
}