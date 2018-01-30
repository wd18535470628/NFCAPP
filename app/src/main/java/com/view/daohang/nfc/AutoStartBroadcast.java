package com.view.daohang.nfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/1/16 0016. 用于监听开机广播，进行开机自启动
 */

public class AutoStartBroadcast extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action)){
            Intent intentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            intentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentForPackage);
        }

    }
}
