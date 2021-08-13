package com.android.hideinstalltest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private static final String INSTALL_RESULT_ACTION = "android.intent.action.INSTALL_APP_HIDE";
    private static final String UNINSTALL_RESULT_ACTION = "android.intent.action.UNINSTALL_APP_HIDE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "action = " + intent.getAction());
        if (INSTALL_RESULT_ACTION.equals(intent.getAction())) {
            String packageName = intent.getStringExtra("packageName");
            int respCode = intent.getIntExtra("respCode", -100);
            String updateId = intent.getStringExtra("updateId");
            Log.d(TAG, "basewin_sw install packageName = " + packageName + ", respCode = " + respCode + ", updateId = " + updateId);
        } else if (UNINSTALL_RESULT_ACTION.equals(intent.getAction())) {
            String packageName = intent.getStringExtra("packageName");
            int respCode = intent.getIntExtra("respCode", -100);
            Log.d(TAG, "basewin_sw uninstall packageName = " + packageName + ", respCode = " + respCode);
        }

    }
}
