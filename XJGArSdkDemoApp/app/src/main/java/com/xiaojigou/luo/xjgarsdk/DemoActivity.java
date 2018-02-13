package com.xiaojigou.luo.xjgarsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.xiaojigou.luo.xjgarsdk.xjgarsdkdemoapp.R;

import java.nio.IntBuffer;


public class DemoActivity extends AppCompatActivity  implements View.OnClickListener {

    private static String TAG = "DemoActivity";
    ImageView demoImageInput;
    ImageView demoImageOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_image);
        findViewById(R.id.start_render_image_bitmap_btn).setOnClickListener(this);
        findViewById(R.id.start_render_image_pixels_btn).setOnClickListener(this);
        demoImageInput = (ImageView) findViewById(R.id.iv_demo_input);
        demoImageOutput = (ImageView) findViewById(R.id.iv_demo_output);

        //step 1 prepare a face image
        Bitmap bitmap =  XJGArSdkApi.getResourceBitmap(this,R.drawable.test);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        demoImageInput .setImageBitmap(bitmap);
//        demoImageOutput.setImageBitmap(bitmap);

        //step 3 setup opengl environmtent
        //if the activity don't have a opengl environment, setup a virtual one
        XJGArSdkApi.XJGARSDKInitOpenglEnvironment(width, height);

        //step 4 other options
        //optimization mode for single image
        XJGArSdkApi.XJGARSDKSetOptimizationMode(1);
        //show sticker papers
        XJGArSdkApi.XJGARSDKSetShowStickerPapers(true);

        //default param
        XJGArSdkApi.XJGARSDKSetWhiteSkinParam(0);
        XJGArSdkApi.XJGARSDKSetRedFaceParam(80);
        XJGArSdkApi.XJGARSDKSetSkinSmoothParam(100);

    }


    @Override
    public void onClick(View v) {
        String stickerPaperdir = XJGArSdkApi.getPrivateResDataDir(getApplicationContext());
        switch (v.getId()){
            case R.id.start_render_image_bitmap_btn:

                stickerPaperdir = stickerPaperdir +"/StickerPapers/"+ "stpaper900224";
                ZIP.unzipAStickPaperPackages(stickerPaperdir);

                Bitmap bitmap = XJGArSdkApi.getResourceBitmap(this, R.drawable.test); //get resource picture
                //step 5 render to get result
                XJGArSdkApi.XJGARSDKChangeStickpaper("stpaper900224");
                XJGArSdkApi.XJGARSDKChangeFilter("filter_none");
                Bitmap modelBitmap = XJGArSdkApi.XJGARSDKRenderImage(bitmap);
                demoImageOutput.setImageBitmap(modelBitmap);
                break;
            case R.id.start_render_image_pixels_btn:

                stickerPaperdir = stickerPaperdir +"/StickerPapers/"+ "caishen";
                ZIP.unzipAStickPaperPackages(stickerPaperdir);

                Bitmap bitmap2 = XJGArSdkApi.getResourceBitmap(this, R.drawable.test); //get resource picture
                //step 5 render to get result
                XJGArSdkApi.XJGARSDKChangeStickpaper("caishen");
                XJGArSdkApi.XJGARSDKChangeFilter("filter_cool");
                Bitmap modelBitmap2 = XJGArSdkApi.XJGARSDKRenderImage(bitmap2);
                demoImageOutput.setImageBitmap(modelBitmap2);
                break;
        }
    }

    private static IntBuffer RGBABuffer;

}
