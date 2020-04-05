package com.joris.camer.Onvif.until;

import java.util.List;

public class MonitorBean {

    /**
     * timestamp : 1577879533
     * status : 200
     * data : {"room_name":"老裁缝航母工厂","lens":[{"lens_name":"1号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-361039-FACEA","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-361039-FACEA.jpg"},{"lens_name":"2号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461121-ECBDE","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461121-ECBDE.jpg"},{"lens_name":"3号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461109-ADDED","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461109-ADDED.jpg"},{"lens_name":"4号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461122-BBCFF","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461122-BBCFF.jpg"},{"lens_name":"5号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461148-CDEDB","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461148-CDEDB.jpg"},{"lens_name":"6号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461118-CDFBB","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461118-CDFBB.jpg"},{"lens_name":"7号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461112-CDDBB","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461112-CDDBB.jpg"},{"lens_name":"8号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461124-CCEDE","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461124-CCEDE.jpg"},{"lens_name":"9号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461117-EBAEC","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461117-EBAEC.jpg"},{"lens_name":"10号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461123-CEBAF","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461123-CEBAF.jpg"}]}
     */

    private int timestamp;
    private int status;
    private DataBean data;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * room_name : 老裁缝航母工厂
         * lens : [{"lens_name":"1号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-361039-FACEA","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-361039-FACEA.jpg"},{"lens_name":"2号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461121-ECBDE","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461121-ECBDE.jpg"},{"lens_name":"3号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461109-ADDED","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461109-ADDED.jpg"},{"lens_name":"4号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461122-BBCFF","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461122-BBCFF.jpg"},{"lens_name":"5号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461148-CDEDB","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461148-CDEDB.jpg"},{"lens_name":"6号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461118-CDFBB","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461118-CDFBB.jpg"},{"lens_name":"7号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461112-CDDBB","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461112-CDDBB.jpg"},{"lens_name":"8号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461124-CCEDE","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461124-CCEDE.jpg"},{"lens_name":"9号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461117-EBAEC","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461117-EBAEC.jpg"},{"lens_name":"10号镜头","online_url":"rtmp://krtxplay1.setrtmp.com/live/SSSS-461123-CEBAF","online_cover_url":"https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-461123-CEBAF.jpg"}]
         */

        private String room_name;
        private String addr_url;

        public String getAddr_url() {
            return addr_url;
        }

        public void setAddr_url(String addr_url) {
            this.addr_url = addr_url;
        }

        private List<LensBean> lens;

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public List<LensBean> getLens() {
            return lens;
        }

        public void setLens(List<LensBean> lens) {
            this.lens = lens;
        }

        public static class LensBean {
            /**
             * lens_name : 1号镜头
             * online_url : rtmp://krtxplay1.setrtmp.com/live/SSSS-361039-FACEA
             * online_cover_url : https://ycycc.oss-cn-shanghai.aliyuncs.com/images/SSSS-361039-FACEA.jpg
             */

            private String lens_name;
            private String online_url;
            private String online_cover_url;

            public String getLens_name() {
                return lens_name;
            }

            public void setLens_name(String lens_name) {
                this.lens_name = lens_name;
            }

            public String getOnline_url() {
                return online_url;
            }

            public void setOnline_url(String online_url) {
                this.online_url = online_url;
            }

            public String getOnline_cover_url() {
                return online_cover_url;
            }

            public void setOnline_cover_url(String online_cover_url) {
                this.online_cover_url = online_cover_url;
            }
        }
    }
}
