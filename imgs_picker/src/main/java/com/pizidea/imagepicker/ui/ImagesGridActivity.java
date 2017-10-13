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

package com.pizidea.imagepicker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzh.mylibrary.R;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.crop.CropActivity;
import com.pizidea.imagepicker.crop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ImagesGridActivity extends FragmentActivity implements View.OnClickListener,AndroidImagePicker.OnImageSelectedListener {
    private static final String TAG = ImagesGridActivity.class.getSimpleName();

    private TextView mBtnOk;

    ImagesGridFragment mFragment;
    AndroidImagePicker androidImagePicker;
    String imagePath;
     boolean isCrop=false;//是否为剪裁
    // 剪切后图像文件
    private Uri mDestinationUri;
    private Context context;
    String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_grid);
        context=this;
        mDestinationUri = Uri.fromFile(new File(this.getCacheDir(), "cropImage.jpeg"));
        androidImagePicker = AndroidImagePicker.getInstance();
        androidImagePicker.clearSelectedImages();//most of the time you need to clear the last selected images or you can comment out this line

        mBtnOk = (TextView) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE){
            mBtnOk.setVisibility(View.GONE);
        }else{
            mBtnOk.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.btn_backpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         tag=getIntent().getStringExtra("tag");
        isCrop = getIntent().getBooleanExtra("isCrop",false);
        imagePath = getIntent().getStringExtra(AndroidImagePicker.KEY_PIC_PATH);
        mFragment =  ImagesGridFragment.newInstance(isCrop);
//        Bundle data = new Bundle();
//        data.putString(AndroidImagePicker.KEY_PIC_PATH,imagePath);
//        mFragment.setArguments(data);

        mFragment.setOnImageItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position = androidImagePicker.isShouldShowCamera() ? position-1 : position;

                if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_MULTI){
                    go2Preview(position);
                }else if(androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_SINGLE){
                    if(isCrop){

                        File file=new File(androidImagePicker.getImageItemsOfCurrentImageSet().get(position).path);
                        toCrop(file);

                    }else{
                        androidImagePicker.clearSelectedImages();
                        androidImagePicker.addSelectedImageItem(position, androidImagePicker.getImageItemsOfCurrentImageSet().get(position));
                        setResult(RESULT_OK);
                        finish();
                    }

                }

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();

        androidImagePicker.addOnImageSelectedListener(this);

        int selectedCount = androidImagePicker.getSelectImageCount();
        onImageSelected(0, null, selectedCount, androidImagePicker.getSelectLimit());

    }


    public void toCrop(File file){
        if("16:9".equals(tag)){
            startCropActivity2(Uri.fromFile(file));
        }else if("1:1".equals(tag)){
            startCropActivity(Uri.fromFile(file));
        }
    }

    public  int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public  int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri) {
        int width=getScreenWidth()*4/5;
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(1, 1)//剪裁的宽高比为1:1
//                .withMaxResultSize(600, 600)//剪裁最大尺寸为600*600
                .withMaxResultSize(width, width)
                .withTargetActivity(CropActivity.class)
                .start(this);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity2(Uri uri) {
        int width=getScreenWidth();
        int height=getScreenHeight();
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(9, 16)//剪裁的宽高比为1:1
//                .withMaxResultSize(600, 600)//剪裁最大尺寸为600*600
                .withMaxResultSize(width*3/4, height*3/4)
                .withTargetActivity(CropActivity.class)
                .setTag(tag)
                .start(this);
    }
    /**
     * 预览页面
     * @param position
     */
    private void go2Preview(int position) {
        Intent intent = new Intent();
        intent.putExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, position);
        intent.setClass(ImagesGridActivity.this, ImagePreviewActivity.class);
        startActivityForResult(intent, AndroidImagePicker.REQ_PREVIEW);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
       if (i == R.id.btn_ok) {
            finish();
            androidImagePicker.notifyOnImagePickComplete(androidImagePicker.getSelectedImages());
            //setResult(RESULT_OK);

        } else {
        }

    }


    @Override
    public void onImageSelected(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit) {
        if(selectedItemsCount > 0){
            mBtnOk.setEnabled(true);
            //mBtnOk.setText("完成("+selectedItemsCount+"/"+maxSelectLimit+")");
            mBtnOk.setText(getResources().getString(R.string.select_complete,selectedItemsCount,maxSelectLimit));
        }else{
            mBtnOk.setText(getResources().getString(R.string.complete));
            mBtnOk.setEnabled(false);
        }
        Log.i(TAG, "=====EVENT:onImageSelected");
    }

    @Override
    protected void onDestroy() {
        androidImagePicker.removeOnImageItemSelectedListener(this);
        Log.i(TAG, "=====removeOnImageItemSelectedListener");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == AndroidImagePicker.REQ_CAMERA){
                Bitmap bmp = (Bitmap)data.getExtras().get("bitmap");
                Log.i(TAG,"=====get Bitmap:"+bmp.hashCode());
            }else if(requestCode == AndroidImagePicker.REQ_PREVIEW){
                setResult(RESULT_OK);
                finish();

            }else if(requestCode == UCrop.REQUEST_CROP){  // 裁剪图片结果
                handleCropResult(data);
            }else if(requestCode == UCrop.RESULT_ERROR){// 裁剪图片错误
                handleCropError(data);
        }

        }

    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    public void handleCropResult(Intent result) {

        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finish();
            AndroidImagePicker.getInstance().getCropCompleteListener().cropComplete(resultUri, bitmap);

        } else {
            Toast.makeText(context, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    public void handleCropError(Intent result) {

        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("", "handleCropError: ", cropError);
            Toast.makeText(context, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }


}
