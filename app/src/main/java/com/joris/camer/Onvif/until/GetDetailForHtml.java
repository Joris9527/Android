package com.joris.camer.Onvif.until;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.joris.camer.DownLoad.DownFileList;

import org.bytedeco.opencv.presets.opencv_core;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class GetDetailForHtml {

    /**
     * @param ips ip集合
     * @return 获取有录像的日期
     * @throws Exception
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList LoadDataForHtml(ArrayList ips) throws Exception{
        ArrayList htmlData = new ArrayList();

        for (int i=0;i<ips.size();i++){
            ArrayList relist = new ArrayList();
            relist.clear();
            String theIp = ips.get(i).toString();
            Document doc = Jsoup.connect("http://"+theIp+"/sd/?-usr=admin&-pwd=admin")
                    .timeout(60000) // 设置超时时间
                    .get(); // 使用GET方法访问URL
            Elements elements = doc.select("a[href^=/sd/]");
            Matcher m;
            for (Element element:elements) {
                String title = element.html().replaceAll("\\D",""); // 新闻标题
                if(title.length()>1){
                    relist.add(title);
                }
                relist = new ArrayList(new HashSet(relist));

            }
            htmlData.add(relist);

        }

        ArrayList theDateOfViewList = GetFfrenceDate(htmlData);



        return theDateOfViewList;

    }

    /**
     * @param htmldata 获取有录像的日期
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList GetFfrenceDate(ArrayList htmldata){
        ArrayList theList = new ArrayList();

        ArrayList list1 = (ArrayList) htmldata.get(0);
        ArrayList list2 = (ArrayList) htmldata.get(1);
        ArrayList list3 = (ArrayList) htmldata.get(2);
        ArrayList list4 = (ArrayList) htmldata.get(3);

        theList = (ArrayList) list1.stream().filter(num->list2.contains(num) && list3.contains(num) && list4.contains(num)).collect(Collectors.toList());

        return theList;


    }

    /**
     * @param DateSource 传入的时间
     * @return 根据日期获取当前已有的回放视屏
     * @throws IOException
     */
    public static ArrayList<DownFileList> GetDetailDateForHtml(String DateSource) throws IOException {

        ArrayList<DownFileList> dateSourceList = new ArrayList<>();

        ArrayList<ArrayList<DownFileList>> AllList = new ArrayList<>();

        ArrayList ips = new ArrayList();
        ips.add("192.168.43.232");
        for (int i=0;i<ips.size();i++){
            String theIp = ips.get(i).toString();
            String dateSrc = DateSource;
            String url = "http://"+theIp+"/sd/"+dateSrc+"/record000/?-usr=admin&-pwd=admin";
            Document doc = Jsoup.connect(url)
                    .timeout(600000) // 设置超时时间
                    .get(); // 使用GET方法访问URL
            Elements elements = doc.select("a[href^=/sd/"+dateSrc+"/record000]");
            Matcher m;
            for (Element element:elements) {

                DownFileList objDown = new DownFileList();
                String title = element.attr("href"); // 新闻标题
                String downurl = "http://"+theIp+title;
                String filename = title.substring(title.lastIndexOf("/")+1);
                boolean istrue = false;
                if((dateSourceList.stream().filter(x-> x.getDownUrl().contains(title)).toArray().length<1)){
                    istrue=true;
                }
                if(title.length()>30 && istrue){
                    objDown.setDownUrl(downurl);
                    objDown.setDownSate(0);
                    objDown.setFileName(theIp+filename);
                    objDown.setToMp4Sate(0);
                    objDown.setUrlName(title);
                    dateSourceList.add(objDown);
                }

            }
            AllList.add(dateSourceList);


        }


        return ConsList(AllList);
    }

    /**
     * @param htmldata 获取4个摄像头共有的视频录像
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<DownFileList> ConsList(ArrayList<ArrayList<DownFileList>> htmldata){
        ArrayList<DownFileList> theList = new ArrayList();

        ArrayList<DownFileList> list1 = htmldata.get(0);
        ArrayList<DownFileList> list2 = htmldata.get(1);
        ArrayList<DownFileList> list3 = htmldata.get(2);
        ArrayList<DownFileList> list4 = htmldata.get(3);

        theList =(ArrayList<DownFileList>) list1.stream().filter(x->
                list2.stream().anyMatch(y->y.getUrlName().equals(x.getUrlName())) &&
                        list3.stream().anyMatch(z->z.getUrlName().equals(x.getUrlName())) &&
                        list4.stream().anyMatch(o->o.getUrlName().equals(x.getUrlName()))).collect(Collectors.toList());

        return theList;


    }





}
