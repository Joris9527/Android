package com.joris.camer.DownLoad;

import java.util.ArrayList;

public class DownFileList {
    public String getDownUrl() {
        return DownUrl;
    }

    public void setDownUrl(String downUrl) {
        DownUrl = downUrl;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public int getDownSate() {
        return DownSate;
    }

    public void setDownSate(int downSate) {
        DownSate = downSate;
    }

    public int getToMp4Sate() {
        return ToMp4Sate;
    }

    public void setToMp4Sate(int toMp4Sate) {
        ToMp4Sate = toMp4Sate;
    }

    private String DownUrl;
    private String FileName;
    private int DownSate;
    private int ToMp4Sate;

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    private String urlName;


}
