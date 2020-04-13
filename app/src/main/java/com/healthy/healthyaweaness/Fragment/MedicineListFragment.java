package com.healthy.healthyaweaness.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthy.healthyaweaness.Activity.AddMedicineActivity;
import com.healthy.healthyaweaness.Activity.AddToDoActivity;
import com.healthy.healthyaweaness.Activity.LoginActivity;
import com.healthy.healthyaweaness.Adapter.MedicineAdapter;
import com.healthy.healthyaweaness.All.CustomRecyclerScrollViewListener;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.Medicine;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.R;
import com.healthy.healthyaweaness.databinding.FragmentMedicineListBinding;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicineListFragment extends Fragment {
    FragmentMedicineListBinding fragmentMedicineListBinding;
    SharedPreferences sharedPreferences;
    SharedPManger sharedPManger;
    private DatabaseReference mFirebaseDatabase;
    ArrayList<Medicine> medicines;
    MedicineAdapter medicineAdapter;
    private static final int ADDing_code = 100;
    private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;

    String Phone_with_plus;
    public MedicineListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentMedicineListBinding=FragmentMedicineListBinding.inflate(inflater,container,false);
        View view=fragmentMedicineListBinding.getRoot();
        // Inflate the layout for this fragment
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Medicine");;
        sharedPreferences = getActivity().getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        sharedPManger = new SharedPManger(getActivity());
        Phone_with_plus=sharedPManger.getDataString("KEY_PHONE");
        fragmentMedicineListBinding.allswip.setColorSchemeResources
                (R.color.colorPrimary, android.R.color.holo_green_dark,
                        android.R.color.holo_orange_dark,
                        android.R.color.holo_blue_dark);

        medicines = new ArrayList<>();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        fragmentMedicineListBinding.MedicineRecycler.setLayoutManager(manager);
        fragmentMedicineListBinding.allswip.setRefreshing(false);

        fragmentMedicineListBinding.allswip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });


//        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
//            @Override
//            public void show() {
//
//                fragmentMedicineListBinding.ADDMedicine.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
////                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2.0f)).start();
//            }
//
//            @Override
//            public void hide() {
//
//                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)fragmentMedicineListBinding.ADDMedicine.getLayoutParams();
//                int fabMargin = lp.bottomMargin;
//                fragmentMedicineListBinding.ADDMedicine.animate().translationY(fragmentMedicineListBinding.ADDMedicine.getHeight()+fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
//            }
//        };
//
//
//        fragmentMedicineListBinding.MedicineRecycler.addOnScrollListener(customRecyclerScrollViewListener);

        fragmentMedicineListBinding.ADDMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new AddingMedicineFragment(), "HomeFragment").commit();
                Intent newMedicine = new Intent(getActivity(), AddMedicineActivity.class);
              //  startActivity(newMedicine);
                startActivityForResult(newMedicine, ADDing_code);


            }
        });
//        customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
//            @Override
//            public void show() {
//
//                fragmentMedicineListBinding.ADDMedicine.animate().translationY(0).
//                        setInterpolator(new DecelerateInterpolator(2)).start();
////                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2.0f)).start();
//            }
//
//            @Override
//            public void hide() {
//
//                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)
//                        fragmentMedicineListBinding.ADDMedicine.getLayoutParams();
//                int fabMargin = lp.bottomMargin;
//                fragmentMedicineListBinding.ADDMedicine.animate().translationY(
//                        fragmentMedicineListBinding.ADDMedicine.getHeight()+fabMargin).setInterpolator
//                        (new AccelerateInterpolator(2.0f)).start();
//            }
//        };
//        fragmentMedicineListBinding.MedicineRecycler.addOnScrollListener(customRecyclerScrollViewListener);



        return view;


    }

    @Override
    public void onStart() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            medicines.clear();
            fragmentMedicineListBinding.allswip.setRefreshing(true);
            mFirebaseDatabase.child(Phone_with_plus).addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    medicines.clear();
                    for(DataSnapshot livenapshot:dataSnapshot.getChildren()) {
                        Medicine medicinelistitem = livenapshot.getValue(Medicine.class);
                        medicines.add(medicinelistitem);




                    }


                    medicineAdapter = new MedicineAdapter( getActivity(),medicines);
                    fragmentMedicineListBinding.MedicineRecycler.setAdapter(medicineAdapter);
                    fragmentMedicineListBinding.allswip.setRefreshing(false);
                    medicineAdapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });}
        else {

            fragmentMedicineListBinding.allswip.setRefreshing(false);
        }
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ADDing_code && resultCode == RESULT_OK) {
            //Write your code if there's no result

        }
    }
}
