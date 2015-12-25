package jp.co.eagler.nicole.introdonist;

import jp.co.eagler.nicole.introdonist.setting.MyPreferenceFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MusicReciever extends BroadcastReceiver {
    public static final String INTENT_GOOGLE_MUSIC_META_CHANGED = "com.android.music.metachanged";
    public static final String INTENT_GOOGLE_MUSIC_STATE_CHANGED = "com.android.music.playstatechanged";
    public static final String INTENT_GOOGLE_MUSIC_COMPLETE = "com.android.music.playbackcomplete";
    public static final String INTENT_XPERIA_MUSIC_STARTED = "com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED";
    public static final String INTENT_XPERIA_MUSIC_PAUSED = "com.sonyericsson.music.playbackcontrol.ACTION_PAUSED";
    public static final String INTENT_XPERIA_MUSIC_COMPLETED = "com.sonyericsson.music.playbackcontrol.TRACK_COMPLETED";

    private String mLastArtist = "";
    private String mLastAlbum= "";
    private String mLastTitle = "";
//    private String mLastSpeechText = "";

    @Override
    public void onReceive(final Context aContext, final Intent aIntent) {
        String action = aIntent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (MyPreferenceFragment.isAutoStart(aContext)) {
                MyService.start(aContext);
                //new Caller(aContext).speech(aContext.getString(R.string.app_name) + "、起動完了したよ");
            }
        }
        else if (action.contentEquals(INTENT_GOOGLE_MUSIC_META_CHANGED)) {
            Bundle bundle = aIntent.getExtras();
            String artist = bundle.getString("artist");
            String album = bundle.getString("album");
            String track = bundle.getString("track");

            speakTitle(aContext, artist, album, track);
        }
        else if (action.contentEquals(INTENT_GOOGLE_MUSIC_STATE_CHANGED)) {
            Bundle bundle = aIntent.getExtras();
            Boolean isPlaying = bundle.getBoolean("playing");

            if (isPlaying) {
                String artist = bundle.getString("artist");
                String album = bundle.getString("album");
                String track = bundle.getString("track");

                speakTitle(aContext, artist, album, track);
            }
            else {
                initInfo();
            }
        }
        else if (action.contentEquals(INTENT_GOOGLE_MUSIC_COMPLETE)) {

        }
        else if (action.contentEquals(INTENT_XPERIA_MUSIC_STARTED)) {
            String artist = aIntent.getExtras().getString("ARTIST_NAME");
            String album = aIntent.getExtras().getString("ALBUM_NAME");
            String track =aIntent.getExtras().getString("TRACK_NAME");

            speakTitle(aContext, artist, album, track);
        }
        else if (action.contentEquals(INTENT_XPERIA_MUSIC_PAUSED)) {
            initInfo();
        }

    }

    private synchronized void initInfo() {
        mLastArtist = "";
        mLastAlbum= "";
        mLastTitle = "";
    }

    private synchronized void speakTitle(final Context aContext, final String aArtist, final String aAlbum, final String aTitle) {
        // 再生済みチェック
        if ((mLastArtist.contentEquals(aArtist))
            && (mLastAlbum.contentEquals(aAlbum))
            && (mLastTitle.contentEquals(aTitle))) {
            return;
        }

        if( aTitle != null ) {
            String speechText = MyPreferenceFragment.getSpeechContents(aContext, aArtist, aAlbum, aTitle);
            new Caller(aContext).speech(speechText);
//            mLastSpeechText = speechText;

            mLastArtist = aArtist;
            mLastAlbum = aAlbum;
            mLastTitle = aTitle;
        }
    }

}
