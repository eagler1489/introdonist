package jp.co.eagler.nicole.introdonist.setting;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import jp.co.eagler.nicole.introdonist.MyService;
import jp.co.eagler.nicole.introdonist.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * PreferenceFragmentを継承したクラス
 * 個々で定義したPreferenceのリソースを設定します
 */
public class MyPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private static final boolean IS_AUTO_START_DEFAULT_VALUE = true;
    private static final boolean IS_SET_NOTIFICATION_DEFAULT_VALUE = false;
    private static final boolean IS_LOWER_CASE_DEFAULT_VALUE = true;
    private static final boolean IS_DELETE_PREFIX_NUM_DEFAULT_VALUE = true;
    private static final Set<String> WHAT_CONTENTS_SPEECH_DEFAULT_VALUE = new HashSet<String>(); {
        WHAT_CONTENTS_SPEECH_DEFAULT_VALUE.add("2");
    }

    private Context mContext = null;

    public MyPreferenceFragment(final Context aContext) {
        mContext = aContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        Preference ttsTestPref = (Preference) findPreference(getString(R.string.tts_test_key));
        ttsTestPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(mContext, SampleSpeechActivity.class);
                startActivity(intent);
                return false;
            }
        });


        setSummary();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.contentEquals(getString(R.string.set_notification_key))) {
            if (MyService.isActive()) {
                MyService.restart(mContext);
            }
        }

        setSummary();
    }

    public static boolean isAutoStart(final Context aContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(aContext);
        return sp.getBoolean(aContext.getString(R.string.auto_start_key), IS_AUTO_START_DEFAULT_VALUE);
    }

    public static boolean isSetNotification(final Context aContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(aContext);
        return sp.getBoolean(aContext.getString(R.string.set_notification_key), IS_SET_NOTIFICATION_DEFAULT_VALUE);
    }

    public static boolean isLowerCase(final Context aContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(aContext);
        return sp.getBoolean(aContext.getString(R.string.lower_case_key), IS_LOWER_CASE_DEFAULT_VALUE);
    }

    public static boolean isDeletePrefixNum(final Context aContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(aContext);
        return sp.getBoolean(aContext.getString(R.string.delete_prefix_num_key), IS_DELETE_PREFIX_NUM_DEFAULT_VALUE);
    }

    public static boolean isSpeechArtist(final Context aContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(aContext);
        Set<String> whatContents = sp.getStringSet(aContext.getString(R.string.what_contents_speech_key), WHAT_CONTENTS_SPEECH_DEFAULT_VALUE);
        return whatContents.contains("0");
    }

    public static boolean isSpeechAlbum(final Context aContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(aContext);
        Set<String> whatContents = sp.getStringSet(aContext.getString(R.string.what_contents_speech_key), WHAT_CONTENTS_SPEECH_DEFAULT_VALUE);
        return whatContents.contains("1");
    }

    public static boolean isSpeechTitle(final Context aContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(aContext);
        Set<String> whatContents = sp.getStringSet(aContext.getString(R.string.what_contents_speech_key), WHAT_CONTENTS_SPEECH_DEFAULT_VALUE);
        return whatContents.contains("2");
    }

    public static String getSpeechContents(final Context aContext, final String aArtist, final String aAlbum, final String aTitle) {
        StringBuilder builder = new StringBuilder();

        boolean isSpeechArtist = isSpeechArtist(aContext);
        boolean isSpeechAlbum = isSpeechAlbum(aContext);
        boolean isSpeechTitle = isSpeechTitle(aContext);

        if (isSpeechArtist) {
            builder.append(aArtist);

            if (isSpeechAlbum || isSpeechTitle) {
                builder.append("で、");
            }
        }
        if (isSpeechAlbum) {
            builder.append(aAlbum);

            if (isSpeechTitle) {
                builder.append("、");
            }
        }
        if (isSpeechTitle) {
            builder.append(aTitle);
        }

        String ret = builder.toString();

        // 小文字化
        if (isLowerCase(aContext)) {
            ret = ret.toLowerCase(Locale.JAPAN);
        }
        // 先頭の曲番号削除
        if (isDeletePrefixNum(aContext)) {
            ret = ret.replaceFirst("^[0-3][0-9][\\s\\.\\-]", "");
        }
        return ret;
    }

    /**
     * 各設定のサマリーを設定
     */
    private void setSummary() {
        CheckBoxPreference cbPref = (CheckBoxPreference) getPreferenceScreen().findPreference(mContext.getString(R.string.auto_start_key));
        if (isAutoStart(mContext)) {
            cbPref.setSummary(mContext.getString(R.string.auto_start_summary_on));
        }
        else {
            cbPref.setSummary(mContext.getString(R.string.auto_start_summary_off));
        }

        cbPref = (CheckBoxPreference) getPreferenceScreen().findPreference(mContext.getString(R.string.set_notification_key));
        if (isSetNotification(mContext)) {
            cbPref.setSummary(mContext.getString(R.string.set_notification_summary_on));
        }
        else {
            cbPref.setSummary(mContext.getString(R.string.set_notification_summary_off));
        }

        cbPref = (CheckBoxPreference) getPreferenceScreen().findPreference(mContext.getString(R.string.lower_case_key));
        if (isLowerCase(mContext)) {
            cbPref.setSummary(mContext.getString(R.string.lower_case_summary_on));
        }
        else {
            cbPref.setSummary(mContext.getString(R.string.lower_case_summary_off));
        }

        cbPref = (CheckBoxPreference) getPreferenceScreen().findPreference(mContext.getString(R.string.delete_prefix_num_key));
        if (isDeletePrefixNum(mContext)) {
            cbPref.setSummary(mContext.getString(R.string.delete_prefix_num_summary_on));
        }
        else {
            cbPref.setSummary(mContext.getString(R.string.delete_prefix_num_summary_off));
        }

        MultiSelectListPreference msPref = (MultiSelectListPreference) getPreferenceScreen().findPreference(mContext.getString(R.string.what_contents_speech_key));
        msPref.setSummary("発話例）「" + getSpeechContents(mContext, "(ｱｰﾃｨｽﾄ名)", "(ｱﾙﾊﾞﾑ名)", "(ﾀｲﾄﾙ名)") + "」");
    }
}