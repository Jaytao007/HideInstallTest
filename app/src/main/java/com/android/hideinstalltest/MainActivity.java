package com.android.hideinstalltest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;

public class MainActivity extends Activity implements OnClickListener  {

    private static final String BROADCAST_UNINSTALLAPP_STATE = "com.basewin.mdm.aidl.JsonAppReceiver.AppUnInstallState";
    private static final String EXTRA_PACKAGE = "packageName";
    private static final String EXTRA_STATE = "state";

    private Button mHideInstallBtn;
    private Button mHideUninstallBtn;
    private Button mInstallBtn;

    private Button mEnableUninstallBtn;
    private Button mDisableUninstallBtn;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        mHideInstallBtn = findViewById(R.id.hide_install_btn);
        mHideInstallBtn.setOnClickListener(this);
        mHideUninstallBtn = findViewById(R.id.hide_uninstall_btn);
        mHideUninstallBtn.setOnClickListener(this);
        mInstallBtn = findViewById(R.id.install_btn);
        mInstallBtn.setOnClickListener(this);

        mEnableUninstallBtn = findViewById(R.id.uninstall_enable_btn);
        mEnableUninstallBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppUninstallState(0);
            }
        });
        mDisableUninstallBtn = findViewById(R.id.uninstall_disable_btn);
        mDisableUninstallBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppUninstallState(1);
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.hide_install_btn:
                Intent intent = new Intent("android.intent.action.VIEW.HIDE");
                intent.setDataAndType(Uri.fromFile(new File("/sdcard/yingyongbao_7402130.apk")), "application/vnd.android.package-archive");
                intent.putExtra("updateId", "test1234");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }
                break;
            case R.id.hide_uninstall_btn:
                Intent intent1 = new Intent("android.intent.action.UNINSTALL.SILENT");
                intent1.putExtra("packageName", "com.tencent.android.qqdownloader");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(intent1);
                } else {
                    startService(intent1);
                }
                break;
            case R.id.install_btn:
                String apkPath = "/sdcard/yingyongbao_7402130.apk";
                installApk(mContext, apkPath);
                break;
        }
    }

    private void installApk(Context mContext, String apkPath) {
        if(mContext == null || TextUtils.isEmpty(apkPath)) return;
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.android.hideinstall.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    private void installApkTest(Context mContext, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(Uri.fromFile(new File(apkPath)));
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //mContext.startActivityForResult(intent, Constants.APP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void setAppUninstallState(int enable) {
        Intent intent = new Intent(BROADCAST_UNINSTALLAPP_STATE);
        intent.putExtra(EXTRA_PACKAGE, "com.tencent.android.qqdownloader");
        intent.putExtra(EXTRA_STATE, enable);
        mContext.sendBroadcast(intent);
    }
}
