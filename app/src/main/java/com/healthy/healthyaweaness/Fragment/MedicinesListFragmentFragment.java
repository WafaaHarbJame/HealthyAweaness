package com.healthy.healthyaweaness.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.franmontiel.localechanger.LocaleChanger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthy.healthyaweaness.Activity.AddMedicinrsActivity;
import com.healthy.healthyaweaness.Activity.ReminderActivity;
import com.healthy.healthyaweaness.All.CustomRecyclerScrollViewListener;
import com.healthy.healthyaweaness.All.ItemTouchHelperClass;
import com.healthy.healthyaweaness.All.RecyclerViewEmptySupport;
import com.healthy.healthyaweaness.DB.AnalyticsApplication;
import com.healthy.healthyaweaness.DB.StoreRetrieveData;
import com.healthy.healthyaweaness.MainActivity;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.MedicineItem;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.Model.ToDoItem;
import com.healthy.healthyaweaness.R;
import com.healthy.healthyaweaness.Service.TodoNotificationService;
import com.healthy.healthyaweaness.ui.home.HomeViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class MedicinesListFragmentFragment extends Fragment {

        private HomeViewModel homeViewModel;
        private RecyclerViewEmptySupport mRecyclerView;
        private FloatingActionButton mAddToDoItemFAB;
        private ArrayList<MedicineItem> mToDoItemsArrayList;
        private CoordinatorLayout mCoordLayout;
        public static final String MEDICINEitem = "com.healthy.healthyaweaness.Activity.MainActivity";
        private BasicListAdapter adapter;
        private static final int REQUEST_ID_TODO_ITEM = 100;
        private MedicineItem mJustDeletedToDoItem;
        private int mIndexOfDeletedToDoItem;
        public static final String FILENAME = "MEDICINE_items.json";
        private StoreRetrieveData storeRetrieveData;
        public ItemTouchHelper itemTouchHelper;
        private CustomRecyclerScrollViewListener customRecyclerScrollViewListener;
        public static final String SHARED_PREF_DATA_SET_CHANGED = "com.avjindersekhon.datasetchanged";
        public static final String CHANGE_OCCURED = "com.avjinder.changeoccured";
        private int mTheme = -1;
        private String theme = "name_of_the_theme";
        public static final String THEME_PREFERENCES = "com.avjindersekhon.themepref";
        public static final String RECREATE_ACTIVITY = "com.avjindersekhon.recreateactivity";
        public static final String THEME_SAVED = "com.avjindersekhon.savedtheme";
        public static final String LIGHTTHEME = "com.avjindersekon.lighttheme";
        private AnalyticsApplication app;
        SharedPreferences sharedPreferences;
        SharedPManger sharedPManger;
        private DatabaseReference mFirebaseDatabase;
        String Phone_with_plus;
    public static final String TODOITEM = "com.healthy.healthyaweaness.Activity.MainActivity";


        public static ArrayList<MedicineItem> getLocallyStoredData(StoreRetrieveData storeRetrieveData) {
            ArrayList<MedicineItem> items = null;

            try {
                items = storeRetrieveData.loadFromFileMedcine();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            if (items == null) {
                items = new ArrayList<>();
            }
            return items;

        }

        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
            View root = inflater.inflate(R.layout.fragment_medicines_list_fragment, container, false);
            app = (AnalyticsApplication)getActivity().getApplication();
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Aller_Regular.tff").setFontAttrId(R.attr.fontPath).build());

            //We recover the theme we've set and setTheme accordingly
            theme = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getString(THEME_SAVED, LIGHTTHEME);
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getResources().updateConfiguration(config, null);
            LocaleChanger.setLocale(new Locale("ar"));

            if(theme.equals(LIGHTTHEME)){
                mTheme = R.style.CustomStyle_LightTheme;
            }
            else{
                mTheme = R.style.CustomStyle_DarkTheme;
            }
            getActivity().setTheme(mTheme);

            sharedPreferences = getActivity().getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
            sharedPManger = new SharedPManger(getActivity());
            Phone_with_plus=sharedPManger.getDataString("KEY_PHONE");
            mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Medicine");;



            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHANGE_OCCURED, false);
            editor.apply();

            storeRetrieveData = new StoreRetrieveData(getActivity(), FILENAME);
            mToDoItemsArrayList =  getLocallyStoredData(storeRetrieveData);
            adapter = new BasicListAdapter(mToDoItemsArrayList);
            setAlarms();


//        adapter.notifyDataSetChanged();
//        storeRetrieveData = new StoreRetrieveData(this, FILENAME);
//
//        try {
//            mToDoItemsArrayList = storeRetrieveData.loadFromFile();
////            Log.d("OskarSchindler", "Arraylist Length: "+mToDoItemsArrayList.size());
//        } catch (IOException | JSONException e) {
////            Log.d("OskarSchindler", "IOException received");
//            e.printStackTrace();
//        }
//
//        if(mToDoItemsArrayList==null){
//            mToDoItemsArrayList = new ArrayList<>();
//        }
//

//        mToDoItemsArrayList = new ArrayList<>();
//        makeUpItems(mToDoItemsArrayList, testStrings.length);

            mCoordLayout = root.findViewById(R.id.myCoordinatorLayout);
            mAddToDoItemFAB = root.findViewById(R.id.addToDoItemFAB);

            mAddToDoItemFAB.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    app.send(getActivity(), "Action", "FAB pressed");
                    Intent newTodo = new Intent(getActivity(), AddMedicinrsActivity.class);
                    MedicineItem item = new MedicineItem("","","", false, null);
                    int color = ColorGenerator.MATERIAL.getRandomColor();
                    item.setTodoColor(color);
                    newTodo.putExtra(MEDICINEitem, item);
                    startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);
                }
            });


//        mRecyclerView = (RecyclerView)findViewById(R.id.toDoRecyclerView);
            mRecyclerView = (RecyclerViewEmptySupport)root.findViewById(R.id.toDoRecyclerView);
            if(theme.equals(LIGHTTHEME)){
                mRecyclerView.setBackgroundColor(getResources().getColor(R.color.primary_lightest));
            }
            mRecyclerView.setEmptyView(root.findViewById(R.id.toDoEmptyView));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



            customRecyclerScrollViewListener = new CustomRecyclerScrollViewListener() {
                @Override
                public void show() {

                    mAddToDoItemFAB.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2.0f)).start();
                }

                @Override
                public void hide() {

                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)mAddToDoItemFAB.getLayoutParams();
                    int fabMargin = lp.bottomMargin;
                    mAddToDoItemFAB.animate().translationY(mAddToDoItemFAB.getHeight()+fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
                }
            };
            mRecyclerView.addOnScrollListener(customRecyclerScrollViewListener);


            ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);


            mRecyclerView.setAdapter(adapter);
//        setUpTransitions();


            return root;
        }

        @Override
        public void onResume() {
            super.onResume();
            app.send(getActivity());

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(ReminderActivity.EXIT, false)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(ReminderActivity.EXIT, false);
                editor.apply();
                getActivity().finish();
            }
            if(getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getBoolean(RECREATE_ACTIVITY, false)){
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(RECREATE_ACTIVITY, false);
                editor.apply();
                getActivity(). recreate();
            }



        }

        @Override
        public void onStart() {
            app = (AnalyticsApplication)getActivity().getApplication();
            super.onStart();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
            if(sharedPreferences.getBoolean(CHANGE_OCCURED, false)){

                mToDoItemsArrayList = getLocallyStoredData(storeRetrieveData);
                adapter = new BasicListAdapter(mToDoItemsArrayList);
                mRecyclerView.setAdapter(adapter);
                setAlarms();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(CHANGE_OCCURED, false);
//            editor.commit();
                editor.apply();


            }

//
//            if(mToDoItemsArrayList.isEmpty()){
//                ReadFromfirebase();
//            }
        }

    public void ReadFromfirebase(){

       // mToDoItemsArrayList.clear();
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mFirebaseDatabase.child(Phone_with_plus).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot livenapshot:dataSnapshot.getChildren()) {
                        MedicineItem medicinelistitem = livenapshot.getValue(MedicineItem.class);
                        mToDoItemsArrayList.add(medicinelistitem);

                        adapter = new BasicListAdapter(mToDoItemsArrayList);
                        mRecyclerView.setAdapter(adapter);
                        setAlarms();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(CHANGE_OCCURED, false);
//            editor.commit();
                        editor.apply();




                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

        private void setAlarms(){
            if(mToDoItemsArrayList!=null){
                for(MedicineItem item : mToDoItemsArrayList){
                    if(item.hasReminder() && item.getToDoDate()!=null){
                        if(item.getToDoDate().before(new Date())){
                            item.setToDoDate(null);
                            continue;
                        }
                        Intent i = new Intent(getActivity(), TodoNotificationService.class);
                        i.putExtra(TodoNotificationService.TODOUUID, item.getIdentifier());
                        i.putExtra(TodoNotificationService.TODOTEXT, item.getToDoText());
                        createAlarm(i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
                    }
                }
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

//       mToDoItemsArrayList.clear();

            if (resultCode != RESULT_CANCELED && requestCode == REQUEST_ID_TODO_ITEM) {
                MedicineItem item = (MedicineItem) data.getSerializableExtra(TODOITEM);
                if (item.getToDoText().length() <= 0) {
                    return;
                }
                boolean existed = false;

                if (item.hasReminder() && item.getToDoDate() != null) {
                    Intent i = new Intent(getActivity(), TodoNotificationService.class);
                    i.putExtra(TodoNotificationService.TODOTEXT, item.getToDoText());
                    i.putExtra(TodoNotificationService.TODOUUID, item.getIdentifier());
                    createAlarm(i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
//                Log.d("OskarSchindler", "Alarm Created: "+item.getToDoText()+" at "+item.getToDoDate());
                }

                for (int i = 0; i < mToDoItemsArrayList.size(); i++) {
                    if (item.getIdentifier().equals(mToDoItemsArrayList.get(i).getIdentifier())) {
                        mToDoItemsArrayList.set(i, item);
                        existed = true;
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                if (!existed) {
                    addToDataStore(item);
                }


            }
        }

        private AlarmManager getAlarmManager(){
            return (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
        }

        private boolean doesPendingIntentExist(Intent i, int requestCode){
            PendingIntent pi = PendingIntent.getService(getActivity(),requestCode, i, PendingIntent.FLAG_NO_CREATE);
            return pi!=null;
        }

        private void createAlarm(Intent i, int requestCode, long timeInMillis){
            AlarmManager am = getAlarmManager();
            PendingIntent pi = PendingIntent.getService(getActivity(),requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
//        Log.d("OskarSchindler", "createAlarm "+requestCode+" time: "+timeInMillis+" PI "+pi.toString());
        }
        private void deleteAlarm(Intent i, int requestCode){
            if(doesPendingIntentExist(i, requestCode)){
                PendingIntent pi = PendingIntent.getService(getActivity(), requestCode,i, PendingIntent.FLAG_NO_CREATE);
                pi.cancel();
                getAlarmManager().cancel(pi);
                Log.d("OskarSchindler", "PI Cancelled " + doesPendingIntentExist(i, requestCode));
            }
        }

        private void addToDataStore(MedicineItem item){
            mToDoItemsArrayList.add(item);
            adapter.notifyItemInserted(mToDoItemsArrayList.size() - 1);

        }

        public class BasicListAdapter extends RecyclerView.Adapter<com.healthy.healthyaweaness.Fragment.MedicinesListFragmentFragment.BasicListAdapter.ViewHolder>
                implements ItemTouchHelperClass.ItemTouchHelperAdapter{
            private ArrayList<MedicineItem> items;

            @Override
            public void onItemMoved(int fromPosition, int toPosition) {
                if(fromPosition<toPosition){
                    for(int i=fromPosition; i<toPosition; i++){
                        Collections.swap(items, i, i+1);
                    }
                }
                else{
                    for(int i=fromPosition; i > toPosition; i--){
                        Collections.swap(items, i, i-1);
                    }
                }

                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRemoved(final int position) {
                //Remove this line if not using Google Analytics
                app.send(getActivity(), "Action", "Swiped Todo Away");

                String toShow = items.get(position).getToDoText();
                String id=items.get(position).getmALETER_ID();
                Log.e("id","id"+id);
                if(id!=null){
                    DeleteAletr(id);

                }

                mJustDeletedToDoItem =  items.remove(position);
                mIndexOfDeletedToDoItem = position;
                Intent i = new Intent(getActivity(),TodoNotificationService.class);
                //DeleteAletr(id);
                deleteAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode());
                notifyItemRemoved(position);

//            String toShow = (mJustDeletedToDoItem.getToDoText().length()>20)?mJustDeletedToDoItem.getToDoText().substring(0, 20)+"...":mJustDeletedToDoItem.getToDoText();
                Snackbar.make(mCoordLayout, "تم الحذف  "+toShow,Snackbar.LENGTH_SHORT).show();
//                    .setAction("تراجع ", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            //Comment the line below if not using Google Analytics
//                            app.send(getActivity(), "Action", "UNDO Pressed");
//                            items.add(mIndexOfDeletedToDoItem, mJustDeletedToDoItem);
//                            if(mJustDeletedToDoItem.getToDoDate()!=null && mJustDeletedToDoItem.hasReminder()){
//                                Intent i = new Intent(getActivity(), TodoNotificationService.class);
//                                i.putExtra(TodoNotificationService.TODOTEXT, mJustDeletedToDoItem.getToDoText());
//                                i.putExtra(TodoNotificationService.TODOUUID, mJustDeletedToDoItem.getIdentifier());
//                                createAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode(), mJustDeletedToDoItem.getToDoDate().getTime());
//                            }
//                            notifyItemInserted(mIndexOfDeletedToDoItem);
//                        }
//                    }).show();
            }

            @Override
            public BasicListAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_medicine_try, parent, false);
                return new BasicListAdapter.ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
                MedicineItem item = items.get(position);
//            if(item.getToDoDate()!=null && item.getToDoDate().before(new Date())){
//                item.setToDoDate(null);
//            }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE);
                //Background color for each to-do item. Necessary for night/day mode
                int bgColor;
                //color of title text in our to-do item. White for night mode, dark gray for day mode
                int todoTextColor;
                if(sharedPreferences.getString(THEME_SAVED, LIGHTTHEME).equals(LIGHTTHEME)){
                    bgColor = Color.WHITE;
                    todoTextColor = getResources().getColor(R.color.secondary_text);
                }
                else{
                    bgColor = Color.DKGRAY;
                    todoTextColor = Color.WHITE;
                }
                holder.linearLayout.setBackgroundColor(bgColor);

                if(item.hasReminder() && item.getToDoDate()!=null){
                    holder.mToDoTextview.setMaxLines(1);
                    holder.mTimeTextView.setVisibility(View.VISIBLE);
//                holder.mToDoTextview.setVisibility(View.GONE);
                }
                else{
                    holder.mTimeTextView.setVisibility(View.GONE);
                    holder.mToDoTextview.setMaxLines(2);
                }
                holder.mToDoTextview.setText(item.getToDoText());
                holder.mToDoTextview.setTextColor(todoTextColor);
                holder.mtodoListItemTimeDesc.setText(item.getMedcibe_desc());

//            holder.mColorTextView.setBackgroundColor(Color.parseColor(item.getTodoColor()));

//            TextDrawable myDrawable = TextDrawable.builder().buildRoundRect(item.getToDoText().substring(0,1),Color.RED, 10);
                //We check if holder.color is set or not
//            if(item.getTodoColor() == null){
//                ColorGenerator generator = ColorGenerator.MATERIAL;
//                int color = generator.getRandomColor();
//                item.setTodoColor(color+"");
//            }
//            Log.d("OskarSchindler", "Color: "+item.getTodoColor());
                TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .toUpperCase()
                        .endConfig()
                        .buildRound(item.getToDoText().substring(0,1),item.getTodoColor());

//            TextDrawable myDrawable = TextDrawable.builder().buildRound(item.getToDoText().substring(0,1),holder.color);
                holder.mColorImageView.setImageDrawable(myDrawable);
                if(item.getToDoDate()!=null){
                    String timeToShow;
                    if(android.text.format.DateFormat.is24HourFormat(getActivity())){
                        timeToShow = AddMedicinrsActivity.formatDate(MainActivity.DATE_TIME_FORMAT_24_HOUR, item.getToDoDate());
                    }
                    else{
                        timeToShow = AddMedicinrsActivity.formatDate(MainActivity.DATE_TIME_FORMAT_12_HOUR, item.getToDoDate());
                    }
                    holder.mTimeTextView.setText(timeToShow);
                }
            }



            @Override
            public int getItemCount() {
                return items.size();
            }

            BasicListAdapter(ArrayList<MedicineItem> items){

                this.items = items;
            }


            @SuppressWarnings("deprecation")
            public class ViewHolder extends RecyclerView.ViewHolder{

                View mView;
                LinearLayout linearLayout;
                TextView mToDoTextview;
                //            TextView mColorTextView;
                ImageView mColorImageView;
                TextView mTimeTextView;
                TextView mtodoListItemTimeDesc;

//            int color = -1;

                public ViewHolder(View v){
                    super(v);
                    mView = v;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MedicineItem item = items.get(BasicListAdapter.ViewHolder.this.getAdapterPosition());
                            Intent i = new Intent(getActivity(), AddMedicinrsActivity.class);
                            i.putExtra(MEDICINEitem, item);
                            i.putExtra("ID",item.getmALETER_ID());
                            i.putExtra("UPDATE",true);
                            Log.e("UPDATE","UPDATE"+true);
                            i.putExtra("ID",item.getmALETER_ID());
                            i.putExtra("Medicine_Description",item.getMedcibe_desc());

                            i.putExtra("UPDATE",true);
                            Log.e("itemdesc","itemdesc"+item.getMedcibe_desc());

                            startActivityForResult(i, REQUEST_ID_TODO_ITEM);
                        }
                    });
                    mToDoTextview = (TextView)v.findViewById(R.id.toDoListItemTextview);
                    mTimeTextView = (TextView)v.findViewById(R.id.todoListItemTimeTextView);
//                mColorTextView = (TextView)v.findViewById(R.id.toDoColorTextView);
                    mColorImageView = (ImageView)v.findViewById(R.id.toDoListItemColorImageView);
                    linearLayout = (LinearLayout)v.findViewById(R.id.listItemLinearLayout);
                    mtodoListItemTimeDesc=v.findViewById(R.id.todoListItemTimeDesc);

                }


            }
        }

        @Override
        public void onPause() {
            super.onPause();
            try {
                storeRetrieveData.saveMedcineToFile(mToDoItemsArrayList);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onDestroy() {

            super.onDestroy();
            mRecyclerView.removeOnScrollListener(customRecyclerScrollViewListener);
        }

        public void DeleteAletr(String id){

            mFirebaseDatabase.child(Phone_with_plus).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "تم الحذف بنجاح ", Toast.LENGTH_SHORT).show();
//                    notifyDataSetChanged();
////                                medicines.remove(position);
//                    notifyDataSetChanged();
//                    notifyItemRemoved(position);

                    }

                }
            });
        }
    }
