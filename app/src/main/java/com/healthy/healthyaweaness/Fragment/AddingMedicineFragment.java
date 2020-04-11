package com.healthy.healthyaweaness.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.healthy.healthyaweaness.MainActivity;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.Medicine;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.R;
import com.healthy.healthyaweaness.databinding.FragmentAddingMedicineBinding;
import com.healthy.healthyaweaness.databinding.FragmentMedicineListBinding;

import java.security.PublicKey;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddingMedicineFragment extends Fragment {
    FragmentAddingMedicineBinding fragmentAddingMedicineBinding;
    SharedPreferences sharedPreferences;
    SharedPManger sharedPManger;
    private DatabaseReference mFirebaseDatabase;

    String Phone_with_plus;
    public String MedicineName,MedicineDescription;
    public AddingMedicineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentAddingMedicineBinding = FragmentAddingMedicineBinding.inflate(inflater,container,false);
        View view = fragmentAddingMedicineBinding.getRoot();
//        inflater.inflate(R.layout.fragment_adding_medicine, container, false);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Medicine");;
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        sharedPManger = new SharedPManger(getActivity());
        Phone_with_plus=sharedPManger.getDataString("KEY_PHONE");

        fragmentAddingMedicineBinding.ADDMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MedicineName=fragmentAddingMedicineBinding.MedicineName.getText().toString();
                MedicineDescription=fragmentAddingMedicineBinding.MedicineDescription.getText().toString();

                if(fragmentAddingMedicineBinding.MedicineName.getText().toString().matches("")){
                    fragmentAddingMedicineBinding.MedicineName.setError(getString(R.string.MedicineName_Requied));
                    fragmentAddingMedicineBinding.MedicineName.requestFocus();
                }
                else if(fragmentAddingMedicineBinding.MedicineDescription.getText().toString().matches("")){
                    fragmentAddingMedicineBinding.MedicineDescription.setError(getString(R.string.MedicineDescription_Requied));
                    fragmentAddingMedicineBinding.MedicineDescription.requestFocus();
                }
                else {
                    addMedicine(MedicineName,MedicineDescription);

                }
            }
        });

        return view;
    }
    public  void addMedicine(String medicineName,String medicineDescription){
        final Medicine medicine = new Medicine(medicineName,medicineDescription,mFirebaseDatabase.child(Phone_with_plus).push().getKey());



        mFirebaseDatabase.child(Phone_with_plus).push().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    mFirebaseDatabase.child(Phone_with_plus).push().setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getActivity(), "هذا الدواء موجود  ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            fragmentAddingMedicineBinding.MedicineDescription.setText("");
                            fragmentAddingMedicineBinding.MedicineName.setText("");
                            startActivity(intent);

                        }
                    });

                } else {
                    mFirebaseDatabase.child(Phone_with_plus).push().setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "تم اضافة الدواء بنجاح ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);

                        }
                    });


                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
