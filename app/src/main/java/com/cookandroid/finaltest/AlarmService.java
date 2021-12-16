package com.cookandroid.finaltest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Foreground 에서 실행되면 Notification 을 보여줘야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1){
            // Oreo(26)버젼 이후 버젼부터는 channel이 필요함
            String channelId = createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setCategory(Notification.CATEGORY_SERVICE)
            .build();
            startForeground(1, notification);
        }

        String state = intent.getStringExtra("state");
        if (!this.isRunning && state.equals("on")) {
            // 알람음 재생 OFF, 알람음 시작 상태
            this.mediaPlayer = MediaPlayer.create(this, R.raw.beo);
            this.mediaPlayer.start();

            this.isRunning = true;

            Log.d("AlarmService", "Alarm Start");
        } else if (this.isRunning & state.equals("off"));
        {
            // 알람음 재생 ON, 알람음 중지 상태
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
            this.mediaPlayer.release();

            this.isRunning = false;
            Log.d("AlarmService", "Alarm Stop");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1){
                stopForeground(true);
            }
        }

        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.ECLAIR_0_1)
    private String createNotificationChannel() {
        String channelId = "Alarm";
        String cnannelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, cnannelName, NotificationManager.IMPORTANCE_NONE);
        //Channel.setDescription(channelName);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }
}
