package com.cookandroid.finaltest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sIntent = new Intent(context, AlarmService.class);
        sIntent.putExtra("start", intent.getStringExtra("state"));

        // Oreo(26) 버젼 이후부터는 Background에서 실행을 금지하기에 Foreground에서 실행해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1) {
            context.startForegroundService(sIntent);
        } else {
            context.startService(sIntent);
        }
    }
}
