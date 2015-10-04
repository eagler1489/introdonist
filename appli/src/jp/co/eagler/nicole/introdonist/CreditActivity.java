package jp.co.eagler.nicole.introdonist;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class CreditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        TextView view = (TextView) findViewById(R.id.app_version_text);
        view.setText("Version : " + getVersionName());
    }

    /**
     * バージョン名を取得する
     *
     * @param context
     * @return
     */
    private String getVersionName(){
        PackageManager pm = getPackageManager();
        String versionName = "";
        try{
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        }catch(NameNotFoundException e){
            e.printStackTrace();
        }
        return versionName;
    }
}
