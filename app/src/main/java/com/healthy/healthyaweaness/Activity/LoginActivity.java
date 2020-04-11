package com.healthy.healthyaweaness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.localechanger.LocaleChanger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;
import com.healthy.healthyaweaness.MainActivity;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.R;

import java.util.Locale;

public class LoginActivity extends BaseActivity {


    String email, password, phone;
    String comparepassward;
    String fcm_token;
    boolean select_country = false;
    String CountryCode = "+966";
    SharedPreferences sharedPreferences;
    SharedPManger sharedPManger;
    String choosing_langauge;
    SharedPreferences.Editor editor_signUp;
    private CountryCodePicker mCcp;
    private EditText mEtSignUpPhone;
    private EditText mEtSignInPassword;
    private TextView mForgetpassward;
    private Button mButtonSignInSign;
    private Button mButtonSignInSignUp;
    private DatabaseReference mFirebaseDatabase;
    private Button mAddingDataForAplication;
    private RadioButton mEnglishlang;
    private RadioButton mARABIClang;
    private RadioGroup mGroupradio;
    int code=966;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCcp = findViewById(R.id.ccp);
        mEtSignUpPhone = findViewById(R.id.etSignUpPhone);
        mEtSignInPassword = findViewById(R.id.etSignInPassword);
        mForgetpassward = findViewById(R.id.forgetpassward);
        mButtonSignInSign = findViewById(R.id.buttonSignInSign);
        mButtonSignInSignUp = findViewById(R.id.buttonSignInSignUp);
        sharedPManger = new SharedPManger(getActiviy());
        FirebaseInstanceId.getInstance().getToken();
        FirebaseApp.initializeApp(this);
        sharedPreferences = getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);

        Configuration config = new Configuration();

        sharedPManger = new SharedPManger(LoginActivity.this);




        mCcp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                select_country = true;
                return false;
            }
        });


         code=sharedPManger.getDataInt(AppConstants.code);
        mCcp.setCountryForPhoneCode(code);
        mCcp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                select_country = true;
                CountryCode = mCcp.getSelectedCountryCodeWithPlus();
                code=mCcp.getSelectedCountryCodeAsInt();
                sharedPManger.SetData(AppConstants.code,code);
            }
        });



      ;
        sharedPreferences = getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        if (sharedPreferences != null) {
            if (!((sharedPreferences.getString(AppConstants.KEY_EMAIL, "")).isEmpty() && (sharedPreferences.getString(AppConstants.KEY_passward, "")).isEmpty() && (sharedPreferences.getString(AppConstants.KEY_PHONE, "")).isEmpty())) {
                email = sharedPreferences.getString(AppConstants.KEY_EMAIL, "");
                password = sharedPreferences.getString(AppConstants.KEY_passward, "");
                String PHONE_without_code = sharedPreferences.getString(AppConstants.KEY_PHONE_without_code, "");
                mEtSignUpPhone.setText(PHONE_without_code);
                mEtSignInPassword.setText(password);

                Intent intent = new Intent(LoginActivity.this, com.healthy.healthyaweaness.MainActivity.class);
                startActivity(intent);
            }
        }
        // فحص بيانات اليوزر المدخلة مع البيانات المخزنة في الفابيرس

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(mEtSignUpPhone.getText().toString());
        mButtonSignInSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtSignUpPhone.getText().toString().equals(null) || mEtSignUpPhone.getText().toString().equals("")) {
                    mEtSignUpPhone.setError(getString(R.string.phoneRequired));
                    mEtSignUpPhone.requestFocus();

                } else if (mEtSignInPassword.getText().toString().equals(null) || mEtSignInPassword.getText().toString().equals("")) {
                    mEtSignInPassword.setError(getString(R.string.passwordRequired));
                    mEtSignInPassword.requestFocus();
                } else {
                    showProgreesDilaog(getActiviy(), getString(R.string.login), getString(R.string.logintxt));

                    final String phone = mEtSignUpPhone.getText().toString();
                    final String password = mEtSignInPassword.getText().toString();

                    // فحص بيانات الدخول باستخدام الفابيرس
                    mFirebaseDatabase.child(CountryCode + mEtSignUpPhone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if (dataSnapshot.exists()) {
                                    comparepassward = String.valueOf(dataSnapshot.child("password").getValue());
                                    String username = String.valueOf(dataSnapshot.child("username").getValue());
                                    sharedPManger.SetData(AppConstants.KEY_username, username);

                                    if (password.matches(comparepassward)) {
                                        sharedPManger.SetData(AppConstants.KEY_PHONE, CountryCode + mEtSignUpPhone.getText().toString());
                                        sharedPManger.SetData(AppConstants.KEY_passward, mEtSignInPassword.getText().toString());
                                        sharedPManger.SetData(AppConstants.KEY_username, username);
                                        sharedPManger.SetData(AppConstants.ISLOGIN, true);

                                        sharedPManger.SetData(AppConstants.KEY_PHONE_without_code, mEtSignUpPhone.getText().toString());

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        hideProgreesDilaog(getActiviy(), getString(R.string.login), getString(R.string.logintxt));


                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.passwardwrong), Toast.LENGTH_SHORT).show();
                                        mEtSignInPassword.setText("");
                                        hideProgreesDilaog(getActiviy(), getString(R.string.login), getString(R.string.logintxt));


                                    }

                                    hideProgreesDilaog(getActiviy(), getString(R.string.login), getString(R.string.logintxt));




                            } else {
                                hideProgreesDilaog(getActiviy(), getString(R.string.login), getString(R.string.logintxt));

                                // mFirebaseDatabase.child(name).setValue(users);
                                Toast.makeText(LoginActivity.this, "" + getString(R.string.accountnotexist), Toast.LENGTH_SHORT).show();
                                mEtSignUpPhone.setText("");
                                mEtSignInPassword.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        // اذا كان ليس لديه حساب سوف يذهب الى شاشة التسجيل
        mButtonSignInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // اذا كان  لديه حساب ونسي كلمة المرو سوف يذهب الى شاشة تغير كلمة المرور

        mForgetpassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mEtSignUpPhone.requestFocus();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

}
