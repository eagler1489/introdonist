package jp.co.eagler.nicole.introdonist;

import jp.co.eagler.nicole.introdonist.setting.MyPreferenceFragment;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MyService extends Service {
    private static final MusicReciever mMusicReceiver = new MusicReciever();
    private static final IntentFilter TARGET_INTENT_FILTER = new IntentFilter(); static {
        TARGET_INTENT_FILTER.addAction(MusicReciever.INTENT_GOOGLE_MUSIC_META_CHANGED);
        TARGET_INTENT_FILTER.addAction(MusicReciever.INTENT_GOOGLE_MUSIC_STATE_CHANGED);
        TARGET_INTENT_FILTER.addAction(MusicReciever.INTENT_GOOGLE_MUSIC_COMPLETE);
//        TARGET_INTENT_FILTER.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        TARGET_INTENT_FILTER.addAction(Intent.ACTION_MEDIA_BUTTON);
//        intentFilter.addAction(MediaControlIntent.ACTION_PLAY);
//        intentFilter.addAction(MediaControlIntent.ACTION_STOP);
//        intentFilter.addAction(MediaControlIntent.ACTION_END_SESSION);
//        intentFilter.addAction(MediaControlIntent.ACTION_START_SESSION);
        TARGET_INTENT_FILTER.addAction(MusicReciever.INTENT_XPERIA_MUSIC_STARTED);
        TARGET_INTENT_FILTER.addAction(MusicReciever.INTENT_XPERIA_MUSIC_PAUSED);
        TARGET_INTENT_FILTER.addAction(MusicReciever.INTENT_XPERIA_MUSIC_COMPLETED);
    }

    // 画面から常駐を解除したい場合のために，常駐インスタンスを保持
    private static MyService activeService = null;

    protected final IBinder binder = new Binder() {
        @Override
        protected boolean onTransact( int code, Parcel data, Parcel reply, int flags ) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Context context = getBaseContext();
        context.getApplicationContext().registerReceiver(mMusicReceiver, TARGET_INTENT_FILTER);
        //Toast.makeText(context, new Throwable().getStackTrace()[0].getMethodName(), Toast.LENGTH_SHORT).show();

        if (MyPreferenceFragment.isSetNotification(context)) {
            startForeGround(context);
        }

        return START_STICKY;        //サービスの強制終了後、再起動する
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);
        Context context = getBaseContext();
        context.getApplicationContext().unregisterReceiver(mMusicReceiver);
        //Toast.makeText(context, new Throwable().getStackTrace()[0].getMethodName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 常駐を開始
     */
    public static synchronized void start(final Context aContext) {
        if (activeService == null) {
            activeService = new MyService();
            Intent intent = new Intent(aContext, activeService.getClass());
            intent.putExtra("type", "start");
            aContext.startService(intent);

//            activeService.bindService(new Intent(aContext, activeService.getClass()), mConnection, BIND_AUTO_CREATE);

            new Caller(aContext).speech(aContext.getString(R.string.app_name) + "のサービスを起動したよ");
        }
        else {
            new Caller(aContext).speech("サービスは既に起動しているよ");
        }

        return;
    }

    /**
     * 常駐中止
     */
    public static synchronized void stop(final Context aContext) {
        if( activeService != null ) {
            Intent intent = new Intent(aContext, activeService.getClass());
            aContext.stopService(intent);
            activeService = null;

            new Caller(aContext).speech(aContext.getString(R.string.app_name) + "のサービスを停止したよ");
        }
        else {
            new Caller(aContext).speech("サービスは既に停止しているよ");
        }
    }

    public static synchronized void restart(final Context aContext) {
        if( activeService != null ) {
            Intent intent = new Intent(aContext, activeService.getClass());
            aContext.stopService(intent);
            activeService = null;
        }

        activeService = new MyService();
        Intent intent = new Intent(aContext, activeService.getClass());
        intent.putExtra("type", "start");
        aContext.startService(intent);

//        activeService.bindService(new Intent(aContext, activeService.getClass()), mConnection, BIND_AUTO_CREATE);

        new Caller(aContext).speech("サービスを再起動したよ");
    }

    public static synchronized boolean isActive() {
        return (activeService != null);
    }

    private void startForeGround(final Context aContext) {
        // サービスを永続化するために、通知を作成する
        Notification.Builder builder = new Notification.Builder(aContext);
        builder.setContentTitle(aContext.getString(R.string.app_name));
        builder.setContentText("作動中");
        builder.setSmallIcon(R.drawable.ic_launcher);

        // サービス永続化
        startForeground(R.string.service_title_call, builder.build());
    }
}
