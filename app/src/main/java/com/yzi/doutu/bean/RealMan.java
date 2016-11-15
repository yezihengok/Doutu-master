package com.yzi.doutu.bean;

import java.util.List;

/**
 * Created by yzh-t105 on 2016/9/27.
 */

public class RealMan {


    /**
     * code : 0
     * data : [{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470810238000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810142346_775.jpg","id":5786,"mediaType":0,"name":"我就问你服不服","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810142346_775.jpg","recommendTime":1470810238000,"ts":1470810238000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470810197000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810142250_278.jpg","id":5785,"mediaType":0,"name":"你差的不止一丢丢","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810142250_278.jpg","recommendTime":1470810197000,"ts":1470810197000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470810144000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810142011_928.jpg","id":5784,"mediaType":0,"name":"燥起来吧！","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810142011_928.jpg","recommendTime":1470810144000,"ts":1470810144000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470809782000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810141607_906.jpg","id":5783,"mediaType":0,"name":"给你摸摸我的肌肉","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810141607_906.jpg","recommendTime":1470809782000,"ts":1470809782000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470809746000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810141534_996.jpg","id":5782,"mediaType":0,"name":"我还能说点什么呢","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810141534_996.jpg","recommendTime":1470809746000,"ts":1470809746000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470809498000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810141121_40.jpg","id":5781,"mediaType":0,"name":"排山倒海","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810141121_40.jpg","recommendTime":1470809498000,"ts":1470809498000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470809319000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810140835_692.jpg","id":5780,"mediaType":0,"name":"白鹤亮翅","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810140835_692.jpg","recommendTime":1470809319000,"ts":1470809319000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470802658000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121712_884.jpg","id":5779,"mediaType":0,"name":"现场的观众让我看到你们的双手好嘛","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121712_884.jpg","recommendTime":1470975037000,"ts":1470975037000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470802531000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121521_503.jpg","id":5777,"mediaType":0,"name":"让我们一起摇摆~","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121521_503.jpg","recommendTime":1470827104000,"ts":1470827104000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470802461000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121348_125.jpg","id":5776,"mediaType":0,"name":"康萌北鼻~","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121348_125.jpg","recommendTime":1470895371000,"ts":1470895371000},{"bisDelete":0,"bisRecommend":0,"clickNum":0,"clickWeight":0,"createTime":1470802393000,"gifPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121233_530.jpg","id":5775,"mediaType":0,"name":"想要怒放的生命~","picPath":"http://img.jiefu.tv/img/attached/1/image/20160810/20160810121233_530.jpg","recommendTime":1470895345000,"ts":1470895345000}]
     * message : 成功
     * result : true
     */

    private String code;
    private String message;
    private boolean result;
    /**
     * bisDelete : 0
     * bisRecommend : 0
     * clickNum : 0
     * clickWeight : 0
     * createTime : 1470810238000
     * gifPath : http://img.jiefu.tv/img/attached/1/image/20160810/20160810142346_775.jpg
     * id : 5786
     * mediaType : 0
     * name : 我就问你服不服
     * picPath : http://img.jiefu.tv/img/attached/1/image/20160810/20160810142346_775.jpg
     * recommendTime : 1470810238000
     * ts : 1470810238000
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }


}
