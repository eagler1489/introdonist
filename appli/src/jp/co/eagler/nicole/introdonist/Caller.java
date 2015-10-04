package jp.co.eagler.nicole.introdonist;

import java.util.HashMap;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class Caller implements OnInitListener {
    enum Status{
        INITING,
        READY,
        NOT_AVAILABLE
    }

    /* Called when the activity is first created. */
    private TextToSpeech mTts = null;
    private Status mStatus = Status.INITING;
    private String mBuffer = null;

    public Caller(Context aContext) {
        mTts = new TextToSpeech(aContext.getApplicationContext(), this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mStatus = Status.READY;
            speak(mBuffer);
        }
        else {
            mStatus = Status.NOT_AVAILABLE;
            mTts = null;
        }
    }

    @SuppressWarnings("deprecation")
    public synchronized void speak(final String aMsg) {

        if (aMsg != null) {
            switch (mStatus) {
            case INITING:
                mBuffer = aMsg;
                break;
            case READY:
                //if (Build.VERSION.SDK_INT < 21) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, String.valueOf(1.0f));
                    params.put(TextToSpeech.Engine.KEY_PARAM_PAN, String.valueOf(1.0f));
                    mTts.speak(aMsg, TextToSpeech.QUEUE_FLUSH, params);
                //}
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
}
