package com.healthy.healthyaweaness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.Model.Users;
import com.healthy.healthyaweaness.R;

public class SignUpActivity extends BaseActivity {

    boolean select_country = false;
    String CountryCode = "+966";
    SharedPreferences sharedPreferences;
    SharedPManger sharedPManger;
    private EditText mEtSignUpFullName;
    private EditText met_Age;
    private EditText mEtSignUpPassword;
    private EditText mEtSignUpConfirmPassword;
    private EditText mEtEmail;
    private CountryCodePicker mCcp;
    private EditText mEtSignUpPhone;
    private Button mButtonSignUpSign;
    private Button mButtonSignUpClickHere;
    private DatabaseReference mFirebaseDatabase;
    private CountryCodePicker ccp;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // شاشة التسجيل الخاص بالمستخدم
//        //هذة R.layout.activity_sign_up  الخاصة بتصميم الشاشة يمكنك الذهاب اليها بالضغط على ctrl+b لرؤية التصميم

        mEtSignUpFullName = findViewById(R.id.etSignUpFullName);
        met_Age = findViewById(R.id.et_Age);
        mEtSignUpPassword = findViewById(R.id.etSignUpPassword);
        mEtSignUpConfirmPassword = findViewById(R.id.etSignUpConfirmPassword);
        mCcp = findViewById(R.id.ccp);
        mEtSignUpPhone = findViewById(R.id.etSignUpPhone);
        mButtonSignUpSign = findViewById(R.id.buttonSignUpSign);
        mButtonSignUpClickHere = findViewById(R.id.buttonSignUpClickHere);
        // جدول Users لتخزين بيانات المستخدم
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mBack = findViewById(R.id.back);
        sharedPreferences = getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        sharedPManger = new SharedPManger(getActiviy());
        mEtEmail = findViewById(R.id.et_Email);


        mCcp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                select_country = true;
                return false;
            }
        });

        mCcp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                select_country = true;
                CountryCode = mCcp.getSelectedCountryCodeWithPlus();
            }
        });


        mButtonSignUpClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        mButtonSignUpSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// فحص البيانات اذا فارغة او لا
                if (mEtSignUpFullName.getText().toString().equals(null) || mEtSignUpFullName.getText().toString().equals("")) {
                    mEtSignUpFullName.setError(getString(R.string.fullNameRequired));
                    mEtSignUpFullName.requestFocus();


                } else if (met_Age.getText().toString().equals(null) || met_Age.getText().toString().equals("")) {
                    met_Age.setError(getString(R.string.met_Age_required));
                    met_Age.requestFocus();

                }
                else if (mEtEmail.getText().toString().equals(null) || mEtEmail.getText().toString().equals("")) {
                    mEtEmail.setError(getString(R.string.emailRequired));
                    mEtEmail.requestFocus();

                }

                else if (!isEmailValid(mEtEmail.getText().toString()) ) {
                    mEtEmail.setError(getString(R.string.enter__correct_email));
                    mEtEmail.requestFocus();

                }



                else if (mEtSignUpPassword.getText().toString().equals(null) || mEtSignUpPassword.getText().toString().equals("")) {
                    mEtSignUpPassword.setError(getString(R.string.passwordRequired));
                    mEtSignUpPassword.requestFocus();


                    Toast.makeText(SignUpActivity.this, "" + getString(R.string.passwordRequired), Toast.LENGTH_SHORT).show();
                } else if (mEtSignUpConfirmPassword.getText().toString().equals(null) || mEtSignUpConfirmPassword.getText().toString().equals("")) {
                    mEtSignUpConfirmPassword.setError(getString(R.string.confirmPasswordRequired));
                    mEtSignUpConfirmPassword.requestFocus();


                } else if (!mEtSignUpConfirmPassword.getText().toString().equals(mEtSignUpPassword.getText().toString())) {

                    mEtSignUpConfirmPassword.setError(getString(R.string.confirmPasswordNotMatchesPassword));


                } else if (mEtSignUpPhone.getText().toString().equals(null) || mEtSignUpPhone.getText().toString().equals("")) {

                    mEtSignUpPhone.setError(getString(R.string.phoneRequired));
                    mEtSignUpPhone.requestFocus();

                } else {
                    showProgreesDilaog(getActiviy(), getString(R.string.signUp), getString(R.string.signUptext));
                    final String name = mEtSignUpFullName.getText().toString();
                    final String id = mFirebaseDatabase.push().getKey();
                    final String Age = met_Age.getText().toString();
                    final String phone = CountryCode + mEtSignUpPhone.getText().toString();

                    final Users users = new Users(mEtSignUpFullName.getText().toString(), Age,
                            mEtSignUpPassword.getText().toString(), CountryCode + mEtSignUpPhone.getText().toString(),mEtEmail.getText().toString());
                    // البدء بفحص عملية التسجيل وتخزين البيانات في الفابيرس

                    mFirebaseDatabase.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(SignUpActivity.this, ""+getString(R.string.phone_numberexist), Toast.LENGTH_SHORT).show();
                                met_Age.setText("");
                                mEtSignUpFullName.setText("");
                                mEtSignUpPassword.setText("");
                                mEtSignUpPhone.setText("");
                                mEtSignUpConfirmPassword.setText("");

                            } else {
                                mFirebaseDatabase.child(phone).setValue(users);
                                Toast.makeText(SignUpActivity.this, getString(R.string.registersucess), Toast.LENGTH_SHORT).show();
                                sharedPManger.SetData(AppConstants.KEY_passward, mEtSignUpPassword.getText().toString());
                                sharedPManger.SetData(AppConstants.KEY_PHONE, CountryCode + mEtSignUpPhone.getText().toString());
                                sharedPManger.SetData(AppConstants.KEY_username, mEtSignUpFullName.getText().toString());
                                sharedPManger.SetData(AppConstants.ISLOGIN, true);
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.putExtra(AppConstants.KEY_passward, mEtSignUpPassword.getText().toString());
                                intent.putExtra(AppConstants.KEY_PHONE, CountryCode + mEtSignUpPhone.getText().toString());
                                intent.putExtra(AppConstants.KEY_username, mEtSignUpFullName.getText().toString());

                                startActivity(intent);

                            }
                            hideProgreesDilaog(getActiviy(), getString(R.string.signUp), getString(R.string.signUptext));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
        });

    }
}
