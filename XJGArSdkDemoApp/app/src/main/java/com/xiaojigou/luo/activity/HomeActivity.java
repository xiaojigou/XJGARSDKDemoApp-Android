package com.xiaojigou.luo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xiaojigou.luo.xjgarsdk.DemoActivity;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;
import com.xiaojigou.luo.xjgarsdk.xjgarsdkdemoapp.R;

/**
 * Created by luojianxin on 2017/10/6.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private static final int REQUEST_PERMISSION = 233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkPermission(Manifest.permission.CAMERA,REQUEST_PERMISSION))
            init();
        else
            finish();
    }

    private void init(){
        setContentView(R.layout.home_content);
        intent=new Intent();
        findViewById(R.id.camera_view_beautify_filter_btn).setOnClickListener(this);
        findViewById(R.id.test_btn).setOnClickListener(this);


        //init sdk function with license
//        String licenseText = "Xb2SGQvurZeKn5kjjQr0S5K9skHCzvhIIZs4sVYidel9ZcA/L6SKdN+yrDilBdd2uJMwCiVT+ztQcGajt6jvvDNE/pw32pGwbcNRopqFULhKnIogTXCbiyET4zL6LAhsvnYwhs/94hDjmsfrNkurQnf7GPgYpoom6KMXB2S2ARYj0ZrG7pvn1R9sOU9xNW0DxZ74xD3x/xMBklRojzuRDEZm4y/Dwh+ebF5SZJE54dBPPpuBNYg9tS9uvApGwEAYwBTZxhLok67qx5vpkkwHNHY8MgW+uRRnS12d7eGgAsIeEPdRS4eu15Sy7y/HmnbtW+HKnoJktzV3E7ASe6/O1w==";
//        XJGArSdkApi.XJGARSDKInitialization(this,licenseText,"testuser", "测试用户公司");

        String licenseText = "hMPthC0oBIbtMp515TWb9jZvrLAKWIMvA4Dhf03n51QvnJr7jZowVe86d0WwU0NK9QGRFaXQn628fRu941qyr3FtsI5R7Y6v1XEpL6YvQNWQCkFEt1SAb0hyawimOYf1tfG2lIaNE63c5e+OxXssOVUWvw8tOr2glVwWVzh79NmZMahrnS8l69SoeoXLMKCYlvAt/qJFFk4+6Aq3QvOv3o72fq5p90yty+YWg7o0HirZpMSP9P5/DHYPFqR/ud7twTJ+Yo2+ZzYvodqRQbGG0HseZn8Xpt7fZdFuZbc2HGRMVk56vNDMRlcGZZXAjENk7m2UMhi1ohhuSf4WmIgXCZFiJXvYFByaY625gXKtEI7+b7t81nWQYHP9BEbzURwL";
        XJGArSdkApi.XJGARSDKInitialization(this,licenseText,"DoctorLuoInvitedUser:teacherluo", "LuoInvitedCompany:www.xiaojigou.cn");

    }

    private void start(){
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        bShowFaceMakeup =true;
        switch (v.getId()){
            case R.id.camera_view_beautify_filter_btn:
                intent.setClass(HomeActivity.this, CameraWithFilterActivity.class);
                break;
            case R.id.test_btn:
                intent.setClass(HomeActivity.this, DemoActivity.class);
                break;
        }
        start();
    }

    public static boolean bShowFaceMakeup =true;

    private boolean checkPermission(String permission,int requestCode){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (!checkPermission()){
                Log.d("home","checkPermission:no pass!");
                requestPermission();
            }else {
                Log.d("home","checkPermission:pass!");
            }
        }

        return true;
    }


    private boolean checkPermission(){
        Log.d("home","checkPermission");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Log.d("home","CAMERA");
            return false;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            Log.d("home","WRITE_EXTERNAL_STORAGE");
            return false;
        }

        return true;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION);

        ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Log.e("home","相机权限申请被拒绝");
                finish();
            }
        }
    }

    private void showHint(String hint){
        Toast.makeText(this,hint , Toast.LENGTH_LONG).show();
    }
}
