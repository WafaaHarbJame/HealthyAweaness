package com.healthy.healthyaweaness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthy.healthyaweaness.MainActivity;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.Medicine;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.R;
import com.healthy.healthyaweaness.databinding.ActivityAddMedicineBinding;

import static android.content.Context.MODE_PRIVATE;

public class AddMedicineActivity extends BaseActivity {
    ActivityAddMedicineBinding activityAddMedicineBinding;
    SharedPreferences sharedPreferences;
    SharedPManger sharedPManger;
    private DatabaseReference mFirebaseDatabase;
    private Toolbar mToolbar;
    private String theme;
    String Phone_with_plus;
     Drawable cross;
    public String MedicineName,MedicineDescription;
    boolean Update;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddMedicineBinding=ActivityAddMedicineBinding.inflate(getLayoutInflater());
        View view=activityAddMedicineBinding.getRoot();
        theme = getSharedPreferences(com.healthy.healthyaweaness.Activity.MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(com.healthy.healthyaweaness.Activity.MainActivity.THEME_SAVED, com.healthy.healthyaweaness.Activity.MainActivity.LIGHTTHEME);
        if(theme.equals(com.healthy.healthyaweaness.Activity.MainActivity.LIGHTTHEME)){
            setTheme(R.style.CustomStyle_LightTheme);
            Log.d("OskarSchindler", "Light Theme");
        }
        else{
            setTheme(R.style.CustomStyle_DarkTheme);
        }


        setContentView(view);
        sharedPreferences = getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        sharedPManger = new SharedPManger(this);
        Phone_with_plus=sharedPManger.getDataString("KEY_PHONE");
         cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if(cross !=null){
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Medicine");;


        if(getSupportActionBar()!=null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(cross );

        }

        Update=getIntent().getBooleanExtra("Update",false);
        if(Update){
            activityAddMedicineBinding.UPDATEMedicine.setVisibility(View.VISIBLE);
            activityAddMedicineBinding.ADDMedicine.setVisibility(View.GONE);
            MedicineName=getIntent().getStringExtra("medicine_Name");
            MedicineDescription=getIntent().getStringExtra("Medicine_Description");
            id=getIntent().getStringExtra("ID");
            activityAddMedicineBinding.MedicineName.setText(MedicineName);
            activityAddMedicineBinding.MedicineDescription.setText(MedicineDescription);
            activityAddMedicineBinding.title.setText(getString(R.string.UPDATE_MEDICINE));

        }
        activityAddMedicineBinding.UPDATEMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineName=activityAddMedicineBinding.MedicineName.getText().toString();
                MedicineDescription=activityAddMedicineBinding.MedicineDescription.getText().toString();
                id=getIntent().getStringExtra("ID");
                if(activityAddMedicineBinding.MedicineName.getText().toString().matches("")){
                    activityAddMedicineBinding.MedicineName.setError(getString(R.string.MedicineName_Requied));
                    activityAddMedicineBinding.MedicineName.requestFocus();
                }
                else if(activityAddMedicineBinding.MedicineDescription.getText().toString().matches("")){
                    activityAddMedicineBinding.MedicineDescription.setError(getString(R.string.MedicineDescription_Requied));
                    activityAddMedicineBinding.MedicineDescription.requestFocus();
                }
                else {
                    UpdateMedicine(MedicineName,MedicineDescription,id);

                }
            }
        });
        activityAddMedicineBinding.ADDMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MedicineName=activityAddMedicineBinding.MedicineName.getText().toString();
                MedicineDescription=activityAddMedicineBinding.MedicineDescription.getText().toString();

                if(activityAddMedicineBinding.MedicineName.getText().toString().matches("")){
                    activityAddMedicineBinding.MedicineName.setError(getString(R.string.MedicineName_Requied));
                    activityAddMedicineBinding.MedicineName.requestFocus();
                }
                else if(activityAddMedicineBinding.MedicineDescription.getText().toString().matches("")){
                    activityAddMedicineBinding.MedicineDescription.setError(getString(R.string.MedicineDescription_Requied));
                    activityAddMedicineBinding.MedicineDescription.requestFocus();
                }
                else {
                    addMedicine(MedicineName,MedicineDescription);

                }
            }
        });


    }

    public  void addMedicine(String medicineName,String medicineDescription){
       final String medicine_id=mFirebaseDatabase.push().getKey();

        final Medicine medicine = new Medicine(medicineName,medicineDescription,medicine_id );
        mFirebaseDatabase.child(Phone_with_plus).child(medicine_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mFirebaseDatabase.child(Phone_with_plus).child(medicine_id).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddMedicineActivity.this, "هذا الدواء موجود  ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActiviy(), com.healthy.healthyaweaness.MainActivity.class);
                            activityAddMedicineBinding.MedicineDescription.setText("");
                            activityAddMedicineBinding.MedicineName.setText("");
                            intent.putExtra("GO_to_Medicine",true);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                         //   startActivity(intent);


                        }
                    });

                } else {
                    mFirebaseDatabase.child(Phone_with_plus).child(medicine_id).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActiviy(), "تم اضافة الدواء بنجاح ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActiviy(), MainActivity.class);
                            intent.putExtra("GO_to_Medicine",true);
                           // startActivity(intent);
                            setResult(Activity.RESULT_OK, intent);
                            finish();

                        }
                    });


                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public  void UpdateMedicine(final String medicineName, final String medicineDescription, final String id){
        final Medicine medicine = new Medicine(medicineName,medicineDescription,mFirebaseDatabase.getKey() );
        mFirebaseDatabase.child(Phone_with_plus).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    mFirebaseDatabase.child(Phone_with_plus).child(id).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(AddMedicineActivity.this, "تم تعديل الدواء   ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActiviy(), com.healthy.healthyaweaness.MainActivity.class);
                            activityAddMedicineBinding.MedicineDescription.setText("");
                            activityAddMedicineBinding.MedicineName.setText("");
                            intent.putExtra("GO_to_Medicine",true);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            //   startActivity(intent);


                        }
                    });

                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
