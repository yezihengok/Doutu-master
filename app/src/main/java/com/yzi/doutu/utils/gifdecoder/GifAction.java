package com.yzi.doutu.utils.gifdecoder;

/**
 * Created by yzh-t105 on 2016/11/16.
 */

public abstract interface GifAction {

    /**
     * gif 接口
     * @param parseStatus 解析成功true
     * @param frameIndex -1时说明已经全部解析完成，当大于0时为解析得到的gif帧编号
     */
    public abstract void parseOk(boolean parseStatus,int frameIndex);
}
