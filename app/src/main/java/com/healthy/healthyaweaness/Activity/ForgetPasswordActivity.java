package com.healthy.healthyaweaness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.healthy.healthyaweaness.R;

public class ForgetPasswordActivity extends BaseActivity {

    private EditText mEtPhoneNumber;
    private EditText mEtSignUpPassword;
    private EditText mEtSignUpConfirmPassword;
    private Button mButtonSignUpSign;
    private DatabaseReference mFirebaseDatabase;
    boolean select_country = false;
    String CountryCode = "+966";
    private CountryCodePicker ccp;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // شاشة لاسترجاع كلمة المرور
        //هذة R.layout activity_forget_password   الخاصة بتصميم الشاشة يمكنك الذهاب اليها بالضغط على ctrl+b عند اسم layout  لرؤية التصميم

        setContentView(R.layout.activity_forget_password);

        mEtPhoneNumber = findViewById(R.id.etPhoneNumber);
        mEtSignUpPassword = findViewById(R.id.etSignUpPassword);
        mEtSignUpConfirmPassword = findViewById(R.id.etSignUpConfirmPassword);
        mButtonSignUpSign = findViewById(R.id.buttonSignUpSign);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(mEtPhoneNumber.getText().toString());
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        mBack = findViewById(R.id.back);

        ccp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                select_country = true;
                return false;
            }
        });
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                select_country = true;
                CountryCode = ccp.getSelectedCountryCodeWithPlus();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mButtonSignUpSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtPhoneNumber.getText().toString().equals(null) || mEtPhoneNumber.getText().toString().equals("")) {

                    mEtPhoneNumber.setError(getString(R.string.phoneRequired));
                    mEtPhoneNumber.requestFocus();

                }
                if (mEtSignUpPassword.getText().toString().equals(null) || mEtSignUpPassword.getText().toString().equals("")) {
                    mEtSignUpPassword.setError(getString(R.string.passwordRequired));
                    mEtSignUpPassword.requestFocus();

                } else if (mEtSignUpConfirmPassword.getText().toString().equals(null) || mEtSignUpConfirmPassword.getText().toString().equals("")) {

                    mEtSignUpConfirmPassword.setError(getString(R.string.confirmPasswordRequired));
                    mEtSignUpConfirmPassword.requestFocus();
                } else if (!mEtSignUpConfirmPassword.getText().toString().equals(mEtSignUpPassword.getText().toString())) {
                    mEtSignUpConfirmPassword.setError(getString(R.string.confirmPasswordNotMatchesPassword));
                } else {
                    mFirebaseDatabase.child(CountryCode + mEtPhoneNumber.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("passward").setValue(mEtSignUpPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ForgetPasswordActivity.this, "تم تغير كلمة المرور بنجاح", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();


                                }
                            });


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
