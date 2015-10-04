package jp.co.eagler.nicole.introdonist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.eagler.nicole.introdonist.setting.MyPreferenceActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {
    /**
     *  ##################################################################################
     *  # ExpandableListView用 共通データ
     *  ##################################################################################
     */
    private static final String KEY_TITLE = "title";
    private static final String KEY_SUMMARY = "summary";
    private static final String KEY_ACTION = "action";
//    private static final String KEY_TOAST = "toast";
    private static final String ACTION_VALUE_NONE = "0";
    private static final String ACTION_VALUE_OPEN_GOOGLE_PLAY = "1";
    private static final String ACTION_VALUE_SHOW_TTS_ENGINE = "2";
    private static final String ACTION_VALUE_OPEN_TTS_SETTING = "3";

    /**
     *  ##################################################################################
     *  # ExpandableListView用 親リスト
     *  ##################################################################################
     */
    private static final Map<String, String> HELP_ABOUT_APP = new HashMap<String, String>(); {
        HELP_ABOUT_APP.put(KEY_TITLE, "このアプリについて");
    }
    private static final Map<String, String> HELP_ABOUT_TTS = new HashMap<String, String>(); {
        HELP_ABOUT_TTS.put(KEY_TITLE, "音声合成について");
    }
    private static final List<Map<String, String>> HELP_LIST = new ArrayList<Map<String, String>>(); {
        if (HELP_LIST.size() == 0) {
            HELP_LIST.add(HELP_ABOUT_APP);
            HELP_LIST.add(HELP_ABOUT_TTS);
        }
    }
    /**
     *  ##################################################################################
     *  # ExpandableListView用 子リスト
     *  ##################################################################################
     */
    private final static Map<String, String> HELP_ABOUT_APP_CHILD_1 = new HashMap<String, String>(); {
        HELP_ABOUT_APP_CHILD_1.put(KEY_TITLE, "このAndroidで音楽を聴く人のためのアプリです");
        HELP_ABOUT_APP_CHILD_1.put(KEY_SUMMARY, "Androidで音楽を聞こう！");
        HELP_ABOUT_APP_CHILD_1.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static Map<String, String> HELP_ABOUT_APP_CHILD_2 = new HashMap<String, String>(); {
        HELP_ABOUT_APP_CHILD_2.put(KEY_TITLE, "音楽再生時に音声合成でアーティスト名や曲名をお知らせします");
        HELP_ABOUT_APP_CHILD_2.put(KEY_SUMMARY, "わざわざ画面見なくていいよ");
        HELP_ABOUT_APP_CHILD_2.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static Map<String, String> HELP_ABOUT_APP_CHILD_5 = new HashMap<String, String>(); {
        HELP_ABOUT_APP_CHILD_5.put(KEY_TITLE, "使い方はサービスを起動するだけ");
        HELP_ABOUT_APP_CHILD_5.put(KEY_SUMMARY, "後はプレイヤーで再生すると勝手に喋ってくれるよ");
        HELP_ABOUT_APP_CHILD_5.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static Map<String, String> HELP_ABOUT_APP_CHILD_3 = new HashMap<String, String>(); {
        HELP_ABOUT_APP_CHILD_3.put(KEY_TITLE, "音声合成は別アプリを使用します");
        HELP_ABOUT_APP_CHILD_3.put(KEY_SUMMARY, "音声合成のアプリをinstallしておいてね\n変な読み上げ方する場合は音声合成アプリを変更してね");
        HELP_ABOUT_APP_CHILD_3.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static Map<String, String> HELP_ABOUT_APP_CHILD_4 = new HashMap<String, String>(); {
        HELP_ABOUT_APP_CHILD_4.put(KEY_TITLE, "対応プレイヤーは次の通りです");
        HELP_ABOUT_APP_CHILD_4.put(KEY_SUMMARY, "Google標準プレイヤー\nXperiaのウォークマンアプリ\n※要望があれば他のプレイヤーも対応するよ");
        HELP_ABOUT_APP_CHILD_4.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static List<Map<String, String>> HELP_ABOUT_APP_CHILD_LIST = new ArrayList<Map<String, String>>(); {
        if (HELP_ABOUT_APP_CHILD_LIST.size() == 0) {
            HELP_ABOUT_APP_CHILD_LIST.add(HELP_ABOUT_APP_CHILD_1);
            HELP_ABOUT_APP_CHILD_LIST.add(HELP_ABOUT_APP_CHILD_2);
            HELP_ABOUT_APP_CHILD_LIST.add(HELP_ABOUT_APP_CHILD_3);
            HELP_ABOUT_APP_CHILD_LIST.add(HELP_ABOUT_APP_CHILD_4);
            HELP_ABOUT_APP_CHILD_LIST.add(HELP_ABOUT_APP_CHILD_5);
        }
    }
    private final static Map<String, String> HELP_ABOUT_TTS_CHILD_1 = new HashMap<String, String>(); {
        HELP_ABOUT_TTS_CHILD_1.put(KEY_TITLE, "音声合成とは文字を音声に変換するアプリです");
        HELP_ABOUT_TTS_CHILD_1.put(KEY_SUMMARY, "アプリを探してinstallしてね(無料もあるよ)");
        HELP_ABOUT_TTS_CHILD_1.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static Map<String, String> HELP_ABOUT_TTS_CHILD_2 = new HashMap<String, String>(); {
        HELP_ABOUT_TTS_CHILD_2.put(KEY_TITLE, "端末に設定されている音声合成アプリを表示します");
        HELP_ABOUT_TTS_CHILD_2.put(KEY_SUMMARY, "ここをタップしてね");
        HELP_ABOUT_TTS_CHILD_2.put(KEY_ACTION, ACTION_VALUE_SHOW_TTS_ENGINE);
    }
    private final static Map<String, String> HELP_ABOUT_TTS_CHILD_3 = new HashMap<String, String>(); {
        HELP_ABOUT_TTS_CHILD_3.put(KEY_TITLE, "音声合成アプリは一つはinstallしてください");
        HELP_ABOUT_TTS_CHILD_3.put(KEY_SUMMARY, "日本語のおススメは\"N2 TTS\"だよ");
        HELP_ABOUT_TTS_CHILD_3.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static Map<String, String> HELP_ABOUT_TTS_CHILD_4 = new HashMap<String, String>(); {
        HELP_ABOUT_TTS_CHILD_4.put(KEY_TITLE, "音声合成アプリをinstallする");
        HELP_ABOUT_TTS_CHILD_4.put(KEY_SUMMARY, "ここをタップするとGoogle Playを開くよ");
        HELP_ABOUT_TTS_CHILD_4.put(KEY_ACTION, ACTION_VALUE_OPEN_GOOGLE_PLAY);
    }
    private final static Map<String, String> HELP_ABOUT_TTS_CHILD_5 = new HashMap<String, String>(); {
        HELP_ABOUT_TTS_CHILD_5.put(KEY_TITLE, "installしたら設定します");
        HELP_ABOUT_TTS_CHILD_5.put(KEY_SUMMARY, "ここをタップすると設定画面を開くよ");
        HELP_ABOUT_TTS_CHILD_5.put(KEY_ACTION, ACTION_VALUE_OPEN_TTS_SETTING);
    }
    private final static Map<String, String> HELP_ABOUT_TTS_CHILD_6 = new HashMap<String, String>(); {
        HELP_ABOUT_TTS_CHILD_6.put(KEY_TITLE, "音声合成は設定画面でテストできます");
        HELP_ABOUT_TTS_CHILD_6.put(KEY_SUMMARY, "メニューの設定をクリックしてね");
        HELP_ABOUT_TTS_CHILD_6.put(KEY_ACTION, ACTION_VALUE_NONE);
    }
    private final static List<Map<String, String>> HELP_ABOUT_TTS_CHILD_LIST = new ArrayList<Map<String, String>>(); {
        if (HELP_ABOUT_TTS_CHILD_LIST.size() == 0) {
            HELP_ABOUT_TTS_CHILD_LIST.add(HELP_ABOUT_TTS_CHILD_1);
            HELP_ABOUT_TTS_CHILD_LIST.add(HELP_ABOUT_TTS_CHILD_2);
            HELP_ABOUT_TTS_CHILD_LIST.add(HELP_ABOUT_TTS_CHILD_3);
            HELP_ABOUT_TTS_CHILD_LIST.add(HELP_ABOUT_TTS_CHILD_4);
            HELP_ABOUT_TTS_CHILD_LIST.add(HELP_ABOUT_TTS_CHILD_5);
        }
    }
    private final static List<List<Map<String, String>>> HELP_ALL_CHILD_LIST = new ArrayList<List<Map<String, String>>>(); {
        if (HELP_ALL_CHILD_LIST.size() == 0) {
            HELP_ALL_CHILD_LIST.add(HELP_ABOUT_APP_CHILD_LIST);
            HELP_ALL_CHILD_LIST.add(HELP_ABOUT_TTS_CHILD_LIST);
        }
    }

    private AdView mAdView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setHelpList();
        setAdmob();
        if( !Caller.isCallable(this) ) {
            showAlertDialog();
        }
    }

    @Override
    protected void onResume() {// アダプタを作る
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
        mAdView.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            startSetting();
            return true;
        }
        else if (id == R.id.show_credit) {
            showCredit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickStartService(final View v) {
        MyService.start(this);
    }

    public void onClickStopService(final View v) {
        MyService.stop(this);
    }

    private void startSetting() {
        Intent intent = new Intent(this, MyPreferenceActivity.class);
        startActivity(intent);
    }

    private void showCredit() {
        Intent intent = new Intent(this, CreditActivity.class);
        startActivity(intent);
    }

    private void setHelpList() {
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                                                this,
                                                HELP_LIST,
                                                R.layout.expandable_list_parent,
                                                new String[] { KEY_TITLE },
                                                new int[] { android.R.id.text1, android.R.id.text2 },
                                                HELP_ALL_CHILD_LIST,
                                                R.layout.expandable_list_child,
                                                new String[] { KEY_TITLE, KEY_SUMMARY },
                                                new int[] { android.R.id.text1, android.R.id.text2 });

        //生成した情報をセット
        ExpandableListView elv = (ExpandableListView) findViewById(R.id.app_help_list);
        elv.setAdapter(adapter);

        elv.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view,
                    int groupPosition, int childPosition, long id) {
                ExpandableListAdapter adapter = parent
                        .getExpandableListAdapter();

                // クリックされた場所の内容情報を取得
                @SuppressWarnings("unchecked")
                Map<String, String> item = (Map<String, String>) adapter.getChild(groupPosition, childPosition);
                // アクション実行
                doAction(item.get(KEY_ACTION));

                return false;
            }

            private void doAction(final String action) {
                if (action != null) {
                    if (action.contentEquals(ACTION_VALUE_OPEN_GOOGLE_PLAY)) {
                        gotoGooglePlay();
                    }
                    else if (action.contentEquals(ACTION_VALUE_SHOW_TTS_ENGINE)) {
                        Toast.makeText(getApplicationContext(), Caller.getDefaultEngine(getApplicationContext()), Toast.LENGTH_SHORT).show();
                    }
                    else if (action.contentEquals(ACTION_VALUE_OPEN_TTS_SETTING)) {
                        Intent intent = new Intent();
                        intent.setAction("com.android.settings.TTS_SETTINGS");
                        try {
                            startActivity(intent);
                        }
                        catch(ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "開けませんでした。\n[設定] - [言語と入力] からテキスト読み上げを開いてください", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        // nothing to do.
                    }
                }
            }
        });
    }

    private void setAdmob() {
        mAdView = new AdView(this);
        mAdView.setAdUnitId("ca-app-pub-8112869724384811/4356976485");
        mAdView.setAdSize(AdSize.BANNER);

        LinearLayout layout_ad = (LinearLayout) findViewById(R.id.main_admob);
        layout_ad.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void showAlertDialog() {
        // 確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("本アプリは音声合成アプリが必要です。");
        alertDlg.setMessage("この端末には音声合成アプリがインストールされていません。Google Play でダウンロードしてください。");
        alertDlg.setPositiveButton(
        "ﾀﾞｳﾝﾛｰﾄﾞしてみる",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                gotoGooglePlay();
            }
        });
        alertDlg.setNegativeButton(
        "後で",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // nothing to do.
            }
        });

        // 表示
        alertDlg.create().show();
    }

    private void gotoGooglePlay() {
        Uri uri = Uri.parse("market://search?q=音声合成");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
