package com.xiaojigou.luo.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.xiaojigou.luo.camfilter.FilterTypeHelper;
import com.xiaojigou.luo.camfilter.GPUCamImgOperator;
import com.xiaojigou.luo.camfilter.widget.LuoGLCameraView;
import com.xiaojigou.luo.camfilter.FilterRecyclerViewAdapter;
import com.xiaojigou.luo.xjgarsdk.ZIP;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;
import com.xiaojigou.luo.camfilter.GPUCamImgOperator.GPUImgFilterType;
import com.xiaojigou.luo.xjgarsdk.xjgarsdkdemoapp.R;
//import com.xiaojigou.luo.faceEff.R;

import java.util.ArrayList;

/**
 * Created by jianxin luo on 2017/10/7.
 */
public class CameraWithFilterActivity extends Activity{
    private LinearLayout mFilterLayout;
    private LinearLayout mFaceSurgeryLayout;
    protected SeekBar mFaceSurgeryFaceShapeSeek;
    protected SeekBar mFaceSurgeryBigEyeSeek;
    protected SeekBar mSkinSmoothSeek;
    protected SeekBar mSkinWihtenSeek;
    protected SeekBar mRedFaceSeek;



    private ArrayList<MenuBean> mStickerData;
    private RecyclerView mMenuView;
    private MenuAdapter mStickerAdapter;

    private RecyclerView mFilterListView;
    private FilterRecyclerViewAdapter mAdapter;
    private GPUCamImgOperator GPUCamImgOperator;
    private boolean isRecording = false;
    private final int MODE_PIC = 1;
    private final int MODE_VIDEO = 2;
    private int mode = MODE_PIC;

    private ImageView btn_shutter;
    private ImageView btn_mode;

    private ObjectAnimator animator;

    private final GPUImgFilterType[] types = new GPUCamImgOperator.GPUImgFilterType[]{
            GPUImgFilterType.NONE,
            GPUImgFilterType.HEALTHY,
            GPUImgFilterType.NOSTALGIA,
            GPUImgFilterType.COOL,
            GPUImgFilterType.EMERALD,
            GPUImgFilterType.EVERGREEN,
            GPUImgFilterType.CRAYON
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_with_filter);
        GPUCamImgOperator =  new GPUCamImgOperator();
        LuoGLCameraView luoGLCameraView = (LuoGLCameraView)findViewById(R.id.glsurfaceview_camera);
        GPUCamImgOperator.context = luoGLCameraView.getContext();
        GPUCamImgOperator.luoGLBaseView = luoGLCameraView;
        initView();


        //options
//        //optimization mode for video
        XJGArSdkApi.XJGARSDKSetOptimizationMode(0);
        //optimization mode for video using asychronized thread
//        XJGArSdkApi.XJGARSDKSetOptimizationMode(2);
        //show sticker papers
//        XJGArSdkApi.XJGARSDKSetShowStickerPapers(true);
        XJGArSdkApi.XJGARSDKSetShowStickerPapers(false);
    }

    private void initView(){
        mFilterLayout = (LinearLayout)findViewById(R.id.layout_filter);

        mFaceSurgeryLayout = (LinearLayout)findViewById(R.id.layout_facesurgery);
        mFaceSurgeryFaceShapeSeek = (SeekBar)findViewById(R.id.faceShapeValueBar);
        mFaceSurgeryFaceShapeSeek.setProgress(20);
        mFaceSurgeryBigEyeSeek = (SeekBar)findViewById(R.id.bigeyeValueBar);
        mFaceSurgeryBigEyeSeek.setProgress(50);

        mSkinSmoothSeek = (SeekBar)findViewById(R.id.skinSmoothValueBar);
        mSkinSmoothSeek.setProgress(100);
        mSkinWihtenSeek = (SeekBar)findViewById(R.id.skinWhitenValueBar);
        mSkinWihtenSeek.setProgress(20);
        mRedFaceSeek = (SeekBar)findViewById(R.id.redFaceValueBar);
        mRedFaceSeek.setProgress(80);
        XJGArSdkApi.XJGARSDKSetSkinSmoothParam(100);
        XJGArSdkApi.XJGARSDKSetWhiteSkinParam(20);
        XJGArSdkApi.XJGARSDKSetRedFaceParam(80);
        mFaceSurgeryFaceShapeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int strength = value;//(int)(value*(float)1.0/100);
                XJGArSdkApi.XJGARSDKSetThinChinParam(strength);
            }
        });
        mFaceSurgeryBigEyeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int strength = value;//(int)(value*(float)1.0/100);
                XJGArSdkApi.XJGARSDKSetBigEyeParam(strength);
            }
        });
        mSkinSmoothSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int level = value;//(int)(value/18);
                XJGArSdkApi.XJGARSDKSetSkinSmoothParam(level);
//                GPUCamImgOperator.setBeautyLevel(level);
            }
        });

        mSkinWihtenSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int level = value;//(int)(value/18);
                XJGArSdkApi.XJGARSDKSetWhiteSkinParam(level);
//                GPUCamImgOperator.setBeautyLevel(level);
            }
        });
        mRedFaceSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int level = value;//(int)(value/18);
                XJGArSdkApi.XJGARSDKSetRedFaceParam(level);
//                GPUCamImgOperator.setBeautyLevel(level);
            }
        });

        mFilterListView = (RecyclerView) findViewById(R.id.filter_listView);

        btn_shutter = (ImageView)findViewById(R.id.btn_camera_shutter);
        btn_mode = (ImageView)findViewById(R.id.btn_camera_mode);

        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_closefilter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_shutter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_switch).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_mode).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_beauty).setOnClickListener(btn_listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);

        mAdapter = new FilterRecyclerViewAdapter(this, types);
        mFilterListView.setAdapter(mAdapter);
        mAdapter.setOnFilterChangeListener(onFilterChangeListener);

        animator = ObjectAnimator.ofFloat(btn_shutter,"rotation",0,360);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        LuoGLCameraView cameraView = (LuoGLCameraView)findViewById(R.id.glsurfaceview_camera);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        params.width = screenSize.x;
        params.height = screenSize.y;//screenSize.x * 4 / 3;
        cameraView.setLayoutParams(params);



        mMenuView= (RecyclerView)findViewById(R.id.mMenuView);
        mStickerData=new ArrayList<>();
        mMenuView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mStickerAdapter=new MenuAdapter(this,mStickerData);
        mStickerAdapter.setOnClickListener(new ClickUtils.OnClickListener() {
            @Override
            public void onClick(View v, int type, int pos, int child) {
                MenuBean m=mStickerData.get(pos);
                String name=m.name;
                String path = m.path;
                if (name.equals("无")) {
                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(false);
                    mStickerAdapter.checkPos=pos;
                    v.setSelected(true);
//                }else if(name.equals("测试2")){
//
//                    mStickerAdapter.checkPos=pos;
//                    v.setSelected(true);
                }else{
                    String stickerPaperdir = XJGArSdkApi.getPrivateResDataDir(getApplicationContext());
                    stickerPaperdir = stickerPaperdir +"/StickerPapers/"+ path;
                    ZIP.unzipAStickPaperPackages(stickerPaperdir);

                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(true);
                    XJGArSdkApi.XJGARSDKChangeStickpaper(path);
                    mStickerAdapter.checkPos=pos;
                    v.setSelected(true);
                }
                mStickerAdapter.notifyDataSetChanged();
            }
        });
        mMenuView.setAdapter(mStickerAdapter);
        initEffectMenu();
    }




    //初始化特效按钮菜单
    protected void initEffectMenu() {
//        "stpaper900224"     ,"草莓猫"
//        "stpaper900639"     ,"米奇老鼠"
//        "angel"             ,"天使"
//        "caishen"           ,"财神爷"
//        "cangou"            ,"罐头狗"
//        "daxiongmao"        ,"大熊猫"
//        "diving"            ,"潜水镜"
//        "flowermustach"     ,"花胡子"
//        "huahuan"           ,"花环"
//        "huangyamotuo"      ,"黄鸭摩托"
//        "hunli"             ,"婚礼"
//        "leisi"             ,"蕾丝"
//        "lufei"             ,"路飞"
//        "lvhua"             ,"鹿花"
//        "mengtu"            ,"梦兔"
//        "rabbit"            ,"大白兔"
//        "shumeng"           ,"萌狗"
//        "strawberry"        ,"草莓"
//        "xuezunv"           ,"血族女"

//

        MenuBean bean=new MenuBean();
        bean.name="无";
        bean.path="";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="天使";
        bean.path="angel";
        mStickerData.add(bean);

//        bean=new MenuBean();
//        bean.name="眼镜";
//        bean.path="eyes";
//        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="财神爷";
        bean.path="caishen";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="罐头狗";
        bean.path="cangou";
        mStickerData.add(bean);

//        bean=new MenuBean();
//        bean.name="大熊猫";
//        bean.path="daxiongmao";
//        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="潜水镜";
        bean.path="diving";
        mStickerData.add(bean);

//        bean=new MenuBean();
//        bean.name="花胡子";
//        bean.path="flowermustach";
//        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="花环";
        bean.path="huahuan";
        mStickerData.add(bean);
//
//        bean=new MenuBean();
//        bean.name="黄鸭摩托";
//        bean.path="huangyamotuo";
//        mStickerData.add(bean);
//
//        bean=new MenuBean();
//        bean.name="婚礼";
//        bean.path="hunli";
//        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="蕾丝";
        bean.path="leisi";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="路飞";
        bean.path="lufei";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="鹿花";
        bean.path="lvhua";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="梦兔";
        bean.path="mengtu";
        mStickerData.add(bean);

//        bean=new MenuBean();
//        bean.name="大白兔";
//        bean.path="rabbit";
//        mStickerData.add(bean);
//
//        bean=new MenuBean();
//        bean.name="萌狗";
//        bean.path="shumeng";
//        mStickerData.add(bean);
//
//        bean=new MenuBean();
//        bean.name="草莓";
//        bean.path="strawberry";
//        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="血族女";
        bean.path="xuezunv";
        mStickerData.add(bean);


        bean=new MenuBean();
        bean.name=" 西瓜猫";
        bean.path="stpaper900224";
        mStickerData.add(bean);

//        bean=new MenuBean();
//        bean.name="米奇老鼠";
//        bean.path="stpaper900639";
//        mStickerData.add(bean);

        mStickerAdapter.notifyDataSetChanged();
    }

    private FilterRecyclerViewAdapter.onFilterChangeListener onFilterChangeListener = new FilterRecyclerViewAdapter.onFilterChangeListener(){

        @Override
        public void onFilterChanged(GPUImgFilterType filterType) {
//            GPUCamImgOperator.setFilter(filterType);
            String filterName = FilterTypeHelper.FilterType2FilterName(filterType);
            XJGArSdkApi.XJGARSDKChangeFilter(filterName);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length != 1 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(mode == MODE_PIC)
                takePhoto();
            else
                takeVideo();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    static boolean bShowFaceSurgery = false;
    static boolean bShowImgFilters = false;
    private View.OnClickListener btn_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int buttonId = v.getId();
            if( buttonId == R.id.btn_camera_mode) {
                switchMode();
            }
            else if (buttonId == R.id.btn_camera_shutter) {
                if (PermissionChecker.checkSelfPermission(CameraWithFilterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(CameraWithFilterActivity.this, new String[] {
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            v.getId());
                } else {
                    if(mode == MODE_PIC)
                        takePhoto();
                    else
                        takeVideo();
                }
            }
            else if (buttonId == R.id.btn_camera_filter) {
                bShowImgFilters = !bShowImgFilters;
                if(bShowImgFilters)
                    showFilters();
                else
                    hideFilters();
            }
            else if (buttonId == R.id.btn_camera_switch) {
                GPUCamImgOperator.switchCamera();
            }
            else if (buttonId == R.id.btn_camera_beauty) {
                bShowFaceSurgery = ! bShowFaceSurgery;
                if(bShowFaceSurgery)
                    showFaceSurgery();
                else
                    hideFaceSurgery();
            }
            else if (buttonId ==  R.id.btn_camera_closefilter) {
                if(bShowImgFilters) {
                    hideFilters();
                    bShowImgFilters = false;
                }
            }
        }
    };

    private void switchMode(){
        if(mode == MODE_PIC){
            mode = MODE_VIDEO;
            btn_mode.setImageResource(R.drawable.icon_camera);
        }else{
            mode = MODE_PIC;
            btn_mode.setImageResource(R.drawable.icon_video);
        }
    }

    private void takePhoto(){
        GPUCamImgOperator.savePicture();
    }

    private void takeVideo(){
        if(isRecording) {
            animator.end();
            GPUCamImgOperator.stopRecord();
        }else {
            animator.start();
            GPUCamImgOperator.startRecord();
        }
        isRecording = !isRecording;
    }

    //显示面部整形
    private void showFaceSurgery()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFaceSurgeryLayout, "translationY", mFaceSurgeryLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.btn_camera_shutter).setClickable(false);
                mFaceSurgeryLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();

    }
    //隐藏面部整形
    private void hideFaceSurgery()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFaceSurgeryLayout, "translationY", 0 ,  mFaceSurgeryLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mFaceSurgeryLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mFaceSurgeryLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }
        });
        animator.start();

    }

    private void showFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", mFilterLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.btn_camera_shutter).setClickable(false);
                mFilterLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }

    private void hideFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", 0 ,  mFilterLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mFilterLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mFilterLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }
        });
        animator.start();
    }

}
