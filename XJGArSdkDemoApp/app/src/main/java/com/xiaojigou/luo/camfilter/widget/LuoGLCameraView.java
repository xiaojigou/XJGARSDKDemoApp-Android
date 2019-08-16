package com.xiaojigou.luo.camfilter.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.xiaojigou.luo.camfilter.CameraEngine;
import com.xiaojigou.luo.camfilter.LuoGPUCameraInputFilter;
import com.xiaojigou.luo.camfilter.LuoGPUImgBaseFilter;
import com.xiaojigou.luo.camfilter.SavePictureTask;
import com.xiaojigou.luo.camfilter.utils.OpenGlUtils;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LuoGLCameraView extends LuoGLBaseView {

    private LuoGPUCameraInputFilter cameraInputFilter;

    private SurfaceTexture surfaceTexture;

    public LuoGLCameraView(Context context) {
        this(context, null);
    }



    public LuoGLCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);//基类的Opengl初始化工作
        this.getHolder().addCallback(this);//获取surfaceholder对象，并设置回调函数
        scaleType = ScaleType.CENTER_CROP;//设置相机的缩放类型
        XJGArSdkApi.XJGARSDKReleaseAllOpenglResources();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);//基类关于Opengl初始化的一些设置
        if(cameraInputFilter == null)
            cameraInputFilter = new LuoGPUCameraInputFilter();
        cameraInputFilter.init();

        if(filter == null)
            filter = new LuoGPUImgBaseFilter();
        filter.init();

        if (textureId == OpenGlUtils.NO_TEXTURE) {
            textureId = OpenGlUtils.getExternalOESTextureID();
            if (textureId != OpenGlUtils.NO_TEXTURE) {
                surfaceTexture = new SurfaceTexture(textureId);//把opengl纹理对象指定给surfacetexture，surfacetexture在纹理发生变化时，将会将纹理id代表的纹理更新
                surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);//设置纹理变化四的监听函数
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {//如果表面发生改变，则先通知基类，基类会通知各个过滤器大小变化了。然后打开相机。
        super.onSurfaceChanged(gl, width, height);
        openCamera();//打开相机，调整渲染尺寸，通知相机输入纹理尺寸发生变换，并绑定surfacetexture到相机用于预览
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);//调用基类清除屏幕内容，glClear
        if(surfaceTexture == null)
            return;
        surfaceTexture.updateTexImage();//从相机中更新纹理内容，
        float[] mtx = new float[16];
        surfaceTexture.getTransformMatrix(mtx);//从surfacetexture中获取纹理变换矩阵（列主序opengles可以直接用）
        cameraInputFilter.setTextureTransformMatrix(mtx);//将输入纹理过滤器的纹理变换矩阵设置为surfacetexture获取的矩阵
        int resultTexture=0;
        resultTexture = cameraInputFilter.onDrawToTexture(textureId,gLCubeBuffer,gLTextureBuffer);
//        int[] resultTex = new int[1];
//        XJGArSdkApi.XJGARSDKRenderGLTexToGLTex(resultTexture,imageWidth,imageHeight,resultTex);
//        resultTexture =  resultTex[0];
//        filter.onDrawFrame(resultTexture,gLCubeBuffer,gLTextureBuffer);

        //XJGArSdkApi.XJGARSDKSetInputTextrueFlips(1,0);

//        XJGArSdkApi.XJGARSDKRenderGLTexture(resultTexture,imageWidth,imageHeight);
        GLES20.glViewport(0,0,surfaceWidth, surfaceHeight);
        int finalTexture = XJGArSdkApi.XJGARSDKRenderGLTexToGLTex(resultTexture,imageWidth,imageHeight);
        GLES20.glViewport(0,0,surfaceWidth, surfaceHeight);
//        filter.onDrawFrame(finalTexture,gLCubeBuffer,gLTextureBuffer);
        filter.onDrawFrame(finalTexture,filter.mGLCubeBuffer,filter.mGLTextureBuffer);



        long timer = System.currentTimeMillis();
        timeCounter.add(timer);
        while (start < timeCounter.size() && timeCounter.get(start) < timer - 1000) {
            start++;
        }
        fps = timeCounter.size() - start;
        if (start > 100) {
            timeCounter = timeCounter.subList(start,
                    timeCounter.size() - 1);
            start = 0;
        }
        Log.i("cameraview","fsp:========="+String.valueOf(fps));

        int targetFPS = 30;
        if(fps>targetFPS)
        {
            float targetFrameTime = 1000.f/(float)targetFPS;
            float currentFrameTime = 1000.f/(float)fps;
            float timeToSleep =  targetFrameTime - currentFrameTime;
            if(timeToSleep>1.0)
            {
                try {
                    Thread.sleep((long)timeToSleep);//休眠
                }
                catch (InterruptedException e) {
                }
            }
        }

    }

    List<Long> timeCounter = new ArrayList<Long>();
    int start = 0;
    int fps =0;

    private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener
            = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            requestRender();
        }
    };

    //打开相机，同时通知相机输入过滤器大小发生了变化，然后调整视图与图像的大小，同时设置相机预览的对象为surfacetexture
    private void openCamera(){
        if(CameraEngine.getCamera() == null)
            CameraEngine.openCamera();
        CameraEngine.CameraEngineInfo info = CameraEngine.getCameraInfo();
        if(info.orientation == 90 || info.orientation == 270){
            imageWidth = info.previewHeight;
            imageHeight = info.previewWidth;
        }else{
            imageWidth = info.previewWidth;
            imageHeight = info.previewHeight;
        }
        cameraInputFilter.onInputSizeChanged(imageWidth, imageHeight);
        cameraInputFilter.initCameraFrameBuffer(imageWidth, imageHeight);
        filter.onInputSizeChanged(imageWidth,imageHeight);

//        adjustSize(info.orientation, info.isFront, true);

        int orientation = info.orientation;
//        orientation = (orientation + 180) %360;
        orientation = (orientation + 180) %360;
        adjustSize(orientation, info.isFront, true);

        if(surfaceTexture != null)
            CameraEngine.startPreview(surfaceTexture);//将表面纹理设置为相机的预览纹理，这样相机图像就会传递给surfacetexture进行处理
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        CameraEngine.releaseCamera();
        cameraInputFilter.destroyFramebuffers();
        XJGArSdkApi.XJGARSDKReleaseAllOpenglResources();
    }

    public void changeRecordingState(boolean isRecording) {
//        recordingEnabled = isRecording;
    }

    @Override
    public void savePicture(final SavePictureTask savePictureTask) {
        CameraEngine.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                CameraEngine.stopPreview();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap photo = drawPhoto(bitmap,CameraEngine.getCameraInfo().isFront);
                        GLES20.glViewport(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        if (photo != null) {
                            savePictureTask.execute(photo);
//                            savePictureTask.execute(bitmap);
                        }
                    }
                });
                CameraEngine.startPreview();
            }
        });
    }

    private Bitmap drawPhoto(Bitmap bitmap,boolean isRotated){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result;
        if(isRotated)// 自拍相机
            result = XJGArSdkApi.XJGARSDKRenderImage(bitmap,true);
        else
            result = XJGArSdkApi.XJGARSDKRenderImage(bitmap,false);

        return result;
    }


}
