/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepicker;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yzh.mylibrary.R;

/**
 * <b>desc your class</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class UilImagePresenter implements ImagePresenter {

    //加载图片 不需要默认拉伸方式：centerCrop()
    @Override
    public void onImage(ImageView imageView, String imageUri) {
        Glide.with(imageView.getContext())
                .load(imageUri)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.crossFade()
                .thumbnail(0.5f)
                .error(R.drawable.default_img)
                .into(imageView);
    }


    public void onPresentImage(ImageView imageView, String imageUri) {

        Glide.with(imageView.getContext())
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.crossFade()
                .thumbnail(0.4f)
                //.centerCrop()
                .fitCenter()
                .error(R.drawable.default_img)
                .into(imageView);
    }

    //常规的加载方式
    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        Glide.with(imageView.getContext())
                // .load(new File(imageUri))
                .load(imageUri)
                //   .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.5f)
                .override(size/4*3, size/4*3)
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView);
    }

    @Override
    public void onPresentCircleImage(ImageView imageView, String imageUri, int size) {
        //用 Glide 加载圆形图片
        Glide.with(imageView.getContext())
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.placeholder(R.drawable.icons)
                //.error(R.mipmap.ic_launcher)
                .thumbnail(0.5f)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }


    //多选预览时使用该加载方式
    public void onPresentImage2(ImageView imageView, String imageUri, int size) {
//        ImageDownloader.Scheme scheme = ImageDownloader.Scheme.FILE;
//
//        ImageLoader.getInstance().displayImage(scheme.wrap(imageUri), imageView, new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_img)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .showImageOnFail(R.drawable.default_img)
//                .showImageForEmptyUri(R.drawable.default_img)
//                .showImageOnLoading(R.drawable.default_img)
//                .build());

        Glide.with(imageView.getContext())
                // .load(new File(imageUri))
                .load(imageUri)
                //   .priority(Priority.HIGH)
                .crossFade()
                // .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //  .centerCrop()
                //.dontAnimate()
                //.thumbnail(0.5f)
                //.override(size/4*3, size/4*3)
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView);
    }


    /**
     * 加载GIF图片
     *
     * @param imageUri
     * @param imageView
     */
    public void displayGif(ImageView imageView, String imageUri,int size) {
        Glide.with(imageView.getContext()).
                load(imageUri)
                .asGif()
                .dontAnimate()
                .thumbnail(0.5f)
                .override(size/4*3, size/4*3)
                .centerCrop()
                .error(R.drawable.default_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

        //.into(new GlideDrawableImageViewTarget(holder.img, 2));//显示循序播放gif 为2次。默认GIF播放是无限循环的，
        //设置播放次数和播放监听的时候，不应加上.asGif()
    }

    public void displayImg(ImageView imageView, String imageUri,int size) {
        Glide.with(imageView.getContext()).
                load(imageUri)
                .asBitmap()
                .dontAnimate()
                .thumbnail(0.5f)
                .override(size/4*3, size/4*3)
                .centerCrop()
                .error(R.drawable.default_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

        //.into(new GlideDrawableImageViewTarget(holder.img, 2));//显示循序播放gif 为2次。默认GIF播放是无限循环的，
        //设置播放次数和播放监听的时候，不应加上.asGif()
    }


    @Override
    public void displayCircleDrawable(int resId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(resourceIdToUri(imageView.getContext(), resId))
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载drawable图片
     *
     * @param resId
     * @param imageView
     */
    public void displayDrawable(int resId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(resourceIdToUri(imageView.getContext(), resId))
                .centerCrop()
                .crossFade()
                //.transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    //将资源ID转为Uri
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId);
    }

}
