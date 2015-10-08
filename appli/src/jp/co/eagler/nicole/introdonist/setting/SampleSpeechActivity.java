package jp.co.eagler.nicole.introdonist.setting;

import jp.co.eagler.nicole.introdonist.Caller;
import jp.co.eagler.nicole.introdonist.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class SampleSpeechActivity extends Activity {
    private static final String SAMPLE_SPEECH_PREF_KEY = "sample_speech_pref_key";

    private TextView mTvArtist = null;
    private TextView mTvAlbum = null;
    private TextView mTvTitle = null;
    private SharedPreferences mSharedPref = null;

    private AdView mAdView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_speech);

        mTvArtist = (TextView)findViewById(R.id.sample_artist_text);
        mTvAlbum = (TextView)findViewById(R.id.sample_album_text);
        mTvTitle = (TextView)findViewById(R.id.sample_title_text);

        mSharedPref = getSharedPreferences(SAMPLE_SPEECH_PREF_KEY, MODE_PRIVATE);
        mTvArtist.setText(mSharedPref.getString(getString(R.string.sample_artist_key), getString(R.string.sample_artist_default_value)));
        mTvAlbum.setText(mSharedPref.getString(getString(R.string.sample_album_key), getString(R.string.sample_album_default_value)));
        mTvTitle.setText(mSharedPref.getString(getString(R.string.sample_title_key), getString(R.string.sample_title_default_value)));

        setAdmob();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    public void onClickSpeechTest(final View v) {
        String artist = mTvArtist.getText().toString();
        String album = mTvAlbum.getText().toString();
        String title = mTvTitle.getText().toString();

        Editor edit = mSharedPref.edit();
        edit.putString(getString(R.string.sample_artist_key), artist);
        edit.putString(getString(R.string.sample_album_key), album);
        edit.putString(getString(R.string.sample_title_key), title);
        edit.commit();

        new Caller(this).speech(MyPreferenceFragment.getSpeechContents(this, artist, album, title));
    }

    private void setAdmob() {
        mAdView  = new AdView(this);
        mAdView.setAdUnitId("ca-app-pub-8112869724384811/4356976485");
        mAdView.setAdSize(AdSize.BANNER);

        LinearLayout layout_ad = (LinearLayout) findViewById(R.id.sample_speech_admob);
        layout_ad.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
