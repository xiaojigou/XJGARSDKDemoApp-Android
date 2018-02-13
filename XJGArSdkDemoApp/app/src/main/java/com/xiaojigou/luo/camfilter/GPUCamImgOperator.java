package com.xiaojigou.luo.camfilter;
import android.content.Context;
import android.os.Environment;

import com.xiaojigou.luo.camfilter.widget.LuoGLBaseView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class GPUCamImgOperator {

    public enum GPUImgFilterType {
        NONE,
        COOL,
        HEALTHY,
        EMERALD,
        NOSTALGIA,
        CRAYON,
        EVERGREEN
    }

    public static Context context;
    public static LuoGLBaseView luoGLBaseView;


    public GPUCamImgOperator(){ }

    public void savePicture(){
        SavePictureTask savePictureTask = new SavePictureTask(getOutputMediaFile(), null);
        luoGLBaseView.savePicture(savePictureTask);
    }

    public void startRecord(){
    }

    public void stopRecord(){
    }


    public void switchCamera(){
        CameraEngine.switchCamera();
    }


    public File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "XJGArSdkDemo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

}
