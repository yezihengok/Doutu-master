package com.yzi.doutu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yzh-t105 on 2016/11/25.
 */

public class AllPicInfo implements Serializable{

    /**
     * emotions : [{"url":"http://wxq.pic.bugua.com/554415d412bd21864c9f3613dffbdf74.jpeg","online_id":2079643,"thumb":"http://wxq.pic.bugua.com/554415d412bd21864c9f3613dffbdf74.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/be80a57fcf2fbbbd9a2d96bbcbd93c5f.jpeg","online_id":3018218,"thumb":"http://wxq.pic.bugua.com/be80a57fcf2fbbbd9a2d96bbcbd93c5f.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/7fda197dfdeb654b655111726c798b6c.jpeg","online_id":10360943,"thumb":"http://wxq.pic.bugua.com/7fda197dfdeb654b655111726c798b6c.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/6d933a9ccc0e1672167df5eb36713763.jpeg","online_id":4189310,"thumb":"http://wxq.pic.bugua.com/6d933a9ccc0e1672167df5eb36713763.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/78f2d695002afded8cad967e3138bcb8.jpeg","online_id":2099392,"thumb":"http://wxq.pic.bugua.com/78f2d695002afded8cad967e3138bcb8.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/51632a878232b993af8a8837889e1348.jpeg","online_id":9691403,"thumb":"http://wxq.pic.bugua.com/51632a878232b993af8a8837889e1348.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/9dcb11088c32c27c28d0b14cd863829a.jpeg","online_id":562160,"thumb":"http://wxq.pic.bugua.com/9dcb11088c32c27c28d0b14cd863829a.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/6e92030dd8919bda73d7f9103d137f73.jpeg","online_id":10215392,"thumb":"http://wxq.pic.bugua.com/6e92030dd8919bda73d7f9103d137f73.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/9f50341cfae024bff2f0a48c890ab9ee.jpeg","online_id":10215394,"thumb":"http://wxq.pic.bugua.com/9f50341cfae024bff2f0a48c890ab9ee.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/f24f12db3e45c1d54b6d44d823be2117.jpeg","online_id":2843856,"thumb":"http://wxq.pic.bugua.com/f24f12db3e45c1d54b6d44d823be2117.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/3fe964ad7e9eabb33693419b7bd32d89.jpeg","online_id":2843854,"thumb":"http://wxq.pic.bugua.com/3fe964ad7e9eabb33693419b7bd32d89.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/15dddd84e0b8842ba186e08e6b50a454.jpeg","online_id":2843853,"thumb":"http://wxq.pic.bugua.com/15dddd84e0b8842ba186e08e6b50a454.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/a2db3e5700c91ad801ac1f6aa75225e4.jpeg","online_id":2843851,"thumb":"http://wxq.pic.bugua.com/a2db3e5700c91ad801ac1f6aa75225e4.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/f5ff2b7abcd59790d82b6493a7e55136.jpeg","online_id":2449553,"thumb":"http://wxq.pic.bugua.com/f5ff2b7abcd59790d82b6493a7e55136.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/d6f664090e450b55301ed8de2d244240.jpeg","online_id":2449552,"thumb":"http://wxq.pic.bugua.com/d6f664090e450b55301ed8de2d244240.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/801873201197c5a2d35cb792999bbd76.jpeg","online_id":2843848,"thumb":"http://wxq.pic.bugua.com/801873201197c5a2d35cb792999bbd76.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/f7fce95ca48771a19052f8ae6f321719.jpeg","online_id":3106550,"thumb":"http://wxq.pic.bugua.com/f7fce95ca48771a19052f8ae6f321719.jpeg!sw300.gif"},{"url":"http://wxq.pic.bugua.com/11610f6829e1c214c5308a0d8f38bd50.jpeg","online_id":2449544,"thumb":"http://wxq.pic.bugua.com/11610f6829e1c214c5308a0d8f38bd50.jpeg!sw300.gif"}]
     * last_id : 2449544
     * rt : true
     * total : 58
     */

    private int last_id;
    private boolean rt;
    private int total;
    private List<EmotionsBean> emotions;

    public int getLast_id() {
        return last_id;
    }

    public void setLast_id(int last_id) {
        this.last_id = last_id;
    }

    public boolean isRt() {
        return rt;
    }

    public void setRt(boolean rt) {
        this.rt = rt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<EmotionsBean> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<EmotionsBean> emotions) {
        this.emotions = emotions;
    }

    public static class EmotionsBean implements Serializable{
        /**
         * url : http://wxq.pic.bugua.com/554415d412bd21864c9f3613dffbdf74.jpeg
         * online_id : 2079643
         * thumb : http://wxq.pic.bugua.com/554415d412bd21864c9f3613dffbdf74.jpeg!sw300.gif
         */

        private String url;
        private int online_id;
        private String thumb;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getOnline_id() {
            return online_id;
        }

        public void setOnline_id(int online_id) {
            this.online_id = online_id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }
}
