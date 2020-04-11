package com.healthy.healthyaweaness.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.franmontiel.localechanger.LocaleChanger;
import com.healthy.healthyaweaness.DB.AnalyticsApplication;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.R;


import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedPManger sharedPManger;
    AwesomeProgressDialog awesomeProgressDialog;
    SharedPreferences sharedPreferences;
    private AnalyticsApplication app;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (AnalyticsApplication)getApplication();
        app.send(this);
        sharedPManger=new SharedPManger(BaseActivity.this);
        sharedPreferences = getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        LocaleChanger.setLocale(new Locale("ar"));
        forceRTLIfSupported();


    }



    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }


    protected  Activity  Toast(){
        return this;

    }

    protected  Activity  getActiviy(){
        return this;

    }

    public void hideDialog() {

        if (progressDialog.isShowing()) progressDialog.dismiss();
    }
    public void Toast(String msg) {

        Toast.makeText(getActiviy(), msg, Toast.LENGTH_SHORT).show();
    }

    public void Toast(int resId) {

        Toast.makeText(getActiviy(), getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected boolean CheckInternet() {
        boolean connected = false;

        ConnectivityManager conMgr = (ConnectivityManager) BaseActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            connected = true;

        } else {
            connected = false;

        }

        return connected;

    }
    public void showProgreesDilaog(Activity activity, String tittle, String message) {
        awesomeProgressDialog = new AwesomeProgressDialog(activity);
        awesomeProgressDialog.setTitle(tittle).setMessage(message).
                setColoredCircle(R.color.colorPrimaryDark).setDialogIconAndColor(R.drawable.ic_dialog_info,
                R.color.white).setCancelable(true).show();

    }

    public void hideProgreesDilaog(Activity activity, String tittle, String message) {
        if(awesomeProgressDialog!=null){
            awesomeProgressDialog.hide();

        }


    }

    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

}

