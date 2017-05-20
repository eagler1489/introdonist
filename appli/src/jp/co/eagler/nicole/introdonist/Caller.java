package jp.co.eagler.nicole.introdonist;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;

public class Caller extends UtteranceProgressListener implements OnInitListener, OnAudioFocusChangeListener {
    enum Status{
        INITING,
        READY,
        NOT_AVAILABLE
    }

    /* Called when the activity is first created. */
    private TextToSpeech mTts = null;
    private Status mStatus = Status.INITING;
    private String mBuffer = null;
    private AudioManager mAudioManager = null;
    private Context mContext = null;

    public Caller(Context aContext) {
        mContext  = aContext;
        mTts = new TextToSpeech(mContext.getApplicationContext(), this);
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        int durationHint = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK;

        // 再生のためにオーディオフォーカスを要求します。
        int result = mAudioManager.requestAudioFocus(this,
                                         AudioManager.STREAM_MUSIC,                 // 音楽ストリームを使用します。
                                         durationHint);                             // 永続的なフォーカスを要求します。
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioManager = null;
        }
    }

    @Override
    public void onInit(int status) {
        mTts.setOnUtteranceProgressListener(this);

        if (status == TextToSpeech.SUCCESS) {
            mStatus = Status.READY;
            speech(mBuffer);
        }
        else {
            mStatus = Status.NOT_AVAILABLE;
            mTts = null;

            if (mAudioManager != null) {
                mAudioManager.abandonAudioFocus(this);
            }
        }
    }

    public synchronized void speech(final String aMsg) {

        if (aMsg != null) {
            switch (mStatus) {
            case INITING:
                mBuffer = aMsg;
                break;
            case READY:
                Bundle params = new Bundle();
                mTts.speak(aMsg, TextToSpeech.QUEUE_FLUSH, params, aMsg);
                break;
            default:
                // ignore
                break;
            }
        }
    }

    public static String getDefaultEngine(final Context aContext) {
        return new TextToSpeech(aContext.getApplicationContext(), null).getDefaultEngine();
    }

    public static boolean isCallable(final Context aContext) {
        return (new TextToSpeech(aContext.getApplicationContext(), null).getEngines().size()) > 0;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // フォーカス一時ロス
        }
        else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // フォーカス再獲得
        }
        else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            // フォーカスロス
            if (mAudioManager != null) {
                mAudioManager.abandonAudioFocus(this);
            }
        }
        else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // ボリュームを下げます。
        }
        else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // ボリュームをノーマルへ戻します
        }
    }

    @Override
    public void onStart(String utteranceId) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void onDone(String utteranceId) {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(this);
        }
    }

    @Override
    public void onError(String utteranceId) {
        // TODO 自動生成されたメソッド・スタブ

    }
}
