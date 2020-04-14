package com.healthy.healthyaweaness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthy.healthyaweaness.DB.AnalyticsApplication;
import com.healthy.healthyaweaness.MainActivity;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.MedicineItem;
import com.healthy.healthyaweaness.Model.SharedPManger;
import com.healthy.healthyaweaness.Model.ToDoItem;
import com.healthy.healthyaweaness.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMedicinrsActivity extends BaseActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private Date mLastEdited;
    private EditText mToDoTextBodyEditText;
    private SwitchCompat mToDoDateSwitch;
    //    private TextView mLastSeenTextView;
    private LinearLayout mUserDateSpinnerContainingLinearLayout;
    private TextView mReminderTextView;

    private EditText mDateEditText;
    private EditText mTimeEditText;
    private EditText mMedicine_Description;
    private String mDefaultTimeOptions12H[];
    private String mDefaultTimeOptions24H[];

    private Button mChooseDateButton;
    private Button mChooseTimeButton;
    private MedicineItem mUserToDoItem;
    private Button mToDoSendFloatingActionButton;
    public static final String DATE_FORMAT = "MMM d, yyyy";
    public static final String DATE_FORMAT_MONTH_DAY = "MMM d";
    public static final String DATE_FORMAT_TIME = "H:m";

    private String mUserEnteredText;
    private boolean mUserHasReminder;
    private Toolbar mToolbar;
    private Date mUserReminderDate;
    private int mUserColor;
    private boolean setDateButtonClickedOnce = false;
    private boolean setTimeButtonClickedOnce = false;
    private LinearLayout mContainerLayout;
    private String theme;
    AnalyticsApplication app;
    boolean adding_from_medicine;

    SharedPreferences sharedPreferences;
    SharedPManger sharedPManger;
    private DatabaseReference mFirebaseDatabase;
    String Phone_with_plus;
    public String alert_id;
    public boolean update;
    String Alert_id_update;
    public String MedicineName,MedicineDescription;
    TextView mtitle;
    @Override
    protected void onResume() {
        super.onResume();
        app.send(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (AnalyticsApplication)getApplication();
        //Need references to these to change them during light/dark mode
        ImageButton reminderIconImageButton;
        TextView reminderRemindMeTextView;


        theme = getSharedPreferences(MainActivity.THEME_PREFERENCES, MODE_PRIVATE).getString(MainActivity.THEME_SAVED, MainActivity.LIGHTTHEME);
        if(theme.equals(MainActivity.LIGHTTHEME)){
            setTheme(R.style.CustomStyle_LightTheme);
            Log.d("OskarSchindler", "Light Theme");
        }
        else{
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        //Need references to these to change them during light/dark mode


        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_to_do);
        //Testing out a new layout
        setContentView(R.layout.activity_add_medicinrs);


        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Show an X in place of <-
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24dp);
        if(cross !=null){
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }


        if(getSupportActionBar()!=null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(cross );

        }


        sharedPreferences = getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        sharedPManger = new SharedPManger(this);
        Phone_with_plus=sharedPManger.getDataString("KEY_PHONE");
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Medicine");;
        alert_id=mFirebaseDatabase.push().getKey();
        mContainerLayout = (LinearLayout)findViewById(R.id.todoReminderAndDateContainerLayout);
        mUserDateSpinnerContainingLinearLayout = (LinearLayout)findViewById(R.id.toDoEnterDateLinearLayout);
        mToDoTextBodyEditText = (EditText)findViewById(R.id.userToDoEditText);
        mToDoDateSwitch = (SwitchCompat)findViewById(R.id.toDoHasDateSwitchCompat);
//        mLastSeenTextView = (TextView)findViewById(R.id.toDoLastEditedTextView);
        mToDoSendFloatingActionButton = findViewById(R.id.makeToDoFloatingActionButton);
        mReminderTextView = (TextView)findViewById(R.id.newToDoDateTimeReminderTextView);
        mMedicine_Description=findViewById(R.id.Medicine_Description);
        mtitle=findViewById(R.id.title);
        mUserToDoItem = (MedicineItem)getIntent().getSerializableExtra(com.healthy.healthyaweaness.Activity.MainActivity.TODOITEM);
        update=getIntent().getBooleanExtra("UPDATE",false);
        Log.e("UPDATE","UPDATE"+update);

        mUserEnteredText = mUserToDoItem.getToDoText();
        mUserHasReminder = mUserToDoItem.hasReminder();
        mUserReminderDate = mUserToDoItem.getToDoDate();
        mUserColor = mUserToDoItem.getTodoColor();
        Alert_id_update=mUserToDoItem.getmALETER_ID();
        MedicineDescription=mUserToDoItem.getMedcibe_desc();
        Alert_id_update=mUserToDoItem.getmALETER_ID();

        Log.e("UPDATE","UPDATE"+true);
        Log.e("itemdesc","itemdesc"+MedicineDescription);



//        if(mUserToDoItem.getLastEdited()==null) {
//            mLastEdited = new Date();
//        }
//        else{
//            mLastEdited = mUserToDoItem.getLastEdited();
//        }


        reminderIconImageButton = (ImageButton)findViewById(R.id.userToDoReminderIconImageButton);
        reminderRemindMeTextView = (TextView)findViewById(R.id.userToDoRemindMeTextView);



        mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mToDoTextBodyEditText);
            }
        });


        if(mUserHasReminder && (mUserReminderDate!=null)){
//            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
            setReminderTextView();
            setEnterDateLayoutVisibleWithAnimations(true);
        }
        if(mUserReminderDate==null){
            mToDoDateSwitch.setChecked(false);
            mReminderTextView.setVisibility(View.INVISIBLE);
        }

//        TextInputLayout til = (TextInputLayout)findViewById(R.id.toDoCustomTextInput);
//        til.requestFocus();
        mToDoTextBodyEditText.requestFocus();
        mToDoTextBodyEditText.setText(mUserEnteredText);
        InputMethodManager imm = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
//        imm.showSoftInput(mToDoTextBodyEditText, InputMethodManager.SHOW_IMPLICIT);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mToDoTextBodyEditText.setSelection(mToDoTextBodyEditText.length());
        adding_from_medicine=getIntent().getBooleanExtra("adding",false);
        update=getIntent().getBooleanExtra("UPDATE",false);
//        if(adding_from_medicine){
//            MedicineDescription=getIntent().getStringExtra("Medicine_Description");
//            mMedicine_Description.setText(MedicineDescription);
//            Alert_id_update=getIntent().getStringExtra("ID");
//            mMedicine_Description.setText(MedicineDescription);
//            mtitle.setText(getString(R.string.UPDATE_MEDICINE));
//            mToDoSendFloatingActionButton.setText(R.string.UPDATE_MEDICINE);
//
//        }

        if(update){
            alert_id=getIntent().getStringExtra("ID");
            MedicineDescription=getIntent().getStringExtra("Medicine_Description");
            mMedicine_Description.setText(MedicineDescription);
            MedicineDescription=getIntent().getStringExtra("Medicine_Description");
            mMedicine_Description.setText(MedicineDescription);
            Alert_id_update=getIntent().getStringExtra("ID");
            mMedicine_Description.setText(MedicineDescription);
            mtitle.setText(getString(R.string.UPDATE_MEDICINE));
            mToDoSendFloatingActionButton.setText(R.string.UPDATE_MEDICINE);


        }


        mToDoTextBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEnteredText = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        String lastSeen = formatDate(DATE_FORMAT, mLastEdited);
//        mLastSeenTextView.setText(String.format(getResources().getString(R.string.last_edited), lastSeen));

        setEnterDateLayoutVisible(mToDoDateSwitch.isChecked());

        mToDoDateSwitch.setChecked(mUserHasReminder && (mUserReminderDate != null));
        mToDoDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    app.send(this, "Action", "Reminder Set");
                }
                else{
                    app.send(this, "Action", "Reminder Removed");

                }

                if (!isChecked) {
                    mUserReminderDate = null;
                }
                mUserHasReminder = isChecked;
                setDateAndTimeEditText();
                setEnterDateLayoutVisibleWithAnimations(isChecked);
                hideKeyboard(mToDoTextBodyEditText);
            }
        });


        mToDoSendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToDoTextBodyEditText.length() <= 0){
                    mToDoTextBodyEditText.setError(getString(R.string.MedicineName_Requied));
                }
                else if(mUserReminderDate!=null && mUserReminderDate.before(new Date())){
                    app.send(this, "Action", "Date in the Past");
                    makeResult(RESULT_CANCELED);
                }
                else{
                    app.send(this, "Action", "Make Todo");
                    makeResult(RESULT_OK);
                    finish();
                }
                hideKeyboard(mToDoTextBodyEditText);
            }
        });



        mDateEditText = (EditText)findViewById(R.id.newTodoDateEditText);
        mTimeEditText = (EditText)findViewById(R.id.newTodoTimeEditText);

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                hideKeyboard(mToDoTextBodyEditText);
                if(mUserToDoItem.getToDoDate()!=null){
//                    date = mUserToDoItem.getToDoDate();
                    date = mUserReminderDate;
                }
                else{
                    date = new Date();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddMedicinrsActivity.this, year, month, day);

                datePickerDialog.show(getFragmentManager(), "DateFragment");
            }
        });


        mTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                hideKeyboard(mToDoTextBodyEditText);
                if(mUserToDoItem.getToDoDate()!=null){
//                    date = mUserToDoItem.getToDoDate();
                    date = mUserReminderDate;
                }
                else{
                    date = new Date();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddMedicinrsActivity.this, hour, minute, DateFormat.is24HourFormat(AddMedicinrsActivity.this));

                timePickerDialog.show(getFragmentManager(), "TimeFragment");
            }
        });

//        mDefaultTimeOptions12H = new String[]{"9:00 AM", "12:00 PM", "3:00 PM", "6:00 PM", "9:00 PM", "12:00 AM"};
//        mDefaultTimeOptions24H = new String[]{"9:00", "12:00", "15:00", "18:00", "21:00", "24:00"};
        setDateAndTimeEditText();

    }

    private void setDateAndTimeEditText(){

        if(mUserToDoItem.hasReminder() && mUserReminderDate!=null){
            String userDate = formatDate("d MMM, yyyy", mUserReminderDate);
            String formatToUse;
            if(DateFormat.is24HourFormat(this)){
                formatToUse = "k:mm";
            }
            else{
                formatToUse = "h:mm a";

            }
            String userTime = formatDate(formatToUse, mUserReminderDate);
            mTimeEditText.setText(userTime);
            mDateEditText.setText(userDate);

        }
        else{
            mDateEditText.setText(getString(R.string.date_reminder_default));
//            mUserReminderDate = new Date();
            boolean time24 = DateFormat.is24HourFormat(this);
            Calendar cal = Calendar.getInstance();
            if(time24){
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);
            }
            else{
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+1);
            }
            cal.set(Calendar.MINUTE, 0);
            mUserReminderDate = cal.getTime();
            Log.d("OskarSchindler", "Imagined Date: "+mUserReminderDate);
            String timeString;
            if(time24){
                timeString = formatDate("k:mm", mUserReminderDate);
            }
            else{
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            mTimeEditText.setText(timeString);

        }
    }


    public void hideKeyboard(EditText et){

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }



    public void setDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        int hour, minute;

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if(reminderCalendar.before(calendar)){
            Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mUserReminderDate!=null){
            calendar.setTime(mUserReminderDate);
        }

        if(DateFormat.is24HourFormat(this)){
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        else{

            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();
        setReminderTextView();
//        setDateAndTimeEditText();
        setDateEditText();
    }

    public void setTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        if(mUserReminderDate!=null){
            calendar.setTime(mUserReminderDate);
        }


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("OskarSchindler", "Time set: "+hour);
        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();

        setReminderTextView();
//        setDateAndTimeEditText();
        setTimeEditText();
    }

    public void  setDateEditText(){
        String dateFormat = "d MMM, yyyy";
        mDateEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void  setTimeEditText(){
        String dateFormat;
        if(DateFormat.is24HourFormat(this)){
            dateFormat = "k:mm";
        }
        else{
            dateFormat = "h:mm a";

        }
        mTimeEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void setReminderTextView(){
        if(mUserReminderDate!=null){
            mReminderTextView.setVisibility(View.VISIBLE);
            if(mUserReminderDate.before(new Date())){
                Log.d("OskarSchindler", "DATE is "+mUserReminderDate);
                mReminderTextView.setText(getString(R.string.date_error_check_again));
                mReminderTextView.setTextColor(Color.RED);
                return;
            }
            Date date = mUserReminderDate;
            String dateString = formatDate("d MMM, yyyy", date);
            String timeString;
            String amPmString = "";

            if(DateFormat.is24HourFormat(this)){
                timeString = formatDate("k:mm", date);
            }
            else{
                timeString = formatDate("h:mm", date);
                amPmString = formatDate("a", date);
            }
            String finalString = String.format(getResources().getString(R.string.remind_date_and_time), dateString, timeString, amPmString);
            mReminderTextView.setTextColor(getResources().getColor(R.color.secondary_text));
            mReminderTextView.setText(finalString);
        }
        else{
            mReminderTextView.setVisibility(View.INVISIBLE);

        }
    }

    public void makeResult(int result){
        Intent i = new Intent();
        if(mUserEnteredText.length()>0){

            String capitalizedString = Character.toUpperCase(mUserEnteredText.charAt(0))+mUserEnteredText.substring(1);
            mUserToDoItem.setToDoText(capitalizedString);
        }
        else{
            mUserToDoItem.setToDoText(mUserEnteredText);
        }
//        mUserToDoItem.setLastEdited(mLastEdited);
        if(mUserReminderDate!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mUserReminderDate);
            calendar.set(Calendar.SECOND, 0);
            mUserReminderDate = calendar.getTime();
        }
        mUserToDoItem.setHasReminder(mUserHasReminder);
        mUserToDoItem.setToDoDate(mUserReminderDate);
        mUserToDoItem.setTodoColor(mUserColor);
        mUserToDoItem.setmALETER_ID(alert_id);
        mUserToDoItem.setMedcibe_desc(mMedicine_Description.getText().toString());
        if(update){
            MedicineDescription=getIntent().getStringExtra("Medicine_Description");
            mMedicine_Description.setText(MedicineDescription);
            Alert_id_update=getIntent().getStringExtra("ID");
            mMedicine_Description.setText(MedicineDescription);
            mtitle.setText(getString(R.string.UPDATE_MEDICINE));
            mToDoSendFloatingActionButton.setText(R.string.UPDATE_MEDICINE);
            UpdateAletr(Alert_id_update,mToDoTextBodyEditText.getText().toString(),mMedicine_Description.getText().toString(),mUserHasReminder,mUserReminderDate);

        }
        else {
            addAletr(mToDoTextBodyEditText.getText().toString(),mMedicine_Description.getText().toString(),mUserHasReminder,mUserReminderDate);

        }











        i.putExtra(com.healthy.healthyaweaness.Activity.MainActivity.TODOITEM, mUserToDoItem);
        setResult(result, i);
    }

    @Override
    public void onBackPressed() {
        if(mUserReminderDate.before(new Date())){
            mUserToDoItem.setToDoDate(null);
        }
        makeResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(this)!=null){
                    app.send(this, "Action", "Discard Todo");
                    makeResult(RESULT_CANCELED);
                    NavUtils.navigateUpFromSameTask(this);

                }
                finish();
                hideKeyboard(mToDoTextBodyEditText);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String formatDate(String formatString, Date dateToFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        setTime(hour, minute);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        setDate(year, month, day);
    }

    public void setEnterDateLayoutVisible(boolean checked){
        if(checked){
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
        }
        else{
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setEnterDateLayoutVisibleWithAnimations(boolean checked){
        if(checked){
            setReminderTextView();
            mUserDateSpinnerContainingLinearLayout.animate().alpha(1.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }
            );
        }
        else{
            mUserDateSpinnerContainingLinearLayout.animate().alpha(0.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }
            );
        }

    }


    public  void addAletr(String todoBody,String medicine_desc, boolean hasReminder,Date toDoDate){

        final ToDoItem toDoItem = new ToDoItem(alert_id,todoBody,medicine_desc,hasReminder,toDoDate);
        mFirebaseDatabase.child(Phone_with_plus).child(alert_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mFirebaseDatabase.child(Phone_with_plus).child(alert_id).setValue(toDoItem).addOnCompleteListener
                            (new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(AddMedicinrsActivity.this, "هذا الدواء موجود  ", Toast.LENGTH_SHORT).show();

                                }
                            });

                } else {
                    mFirebaseDatabase.child(Phone_with_plus).child(alert_id).setValue(toDoItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActiviy(), "تم اضافة الدواء بنجاح ", Toast.LENGTH_SHORT).show();

                        }
                    });


                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public  void UpdateAletr(final String alert_id, String todoBody,String medicine_desc, boolean hasReminder,Date toDoDate){

        final ToDoItem toDoItem = new ToDoItem(alert_id,todoBody,medicine_desc,hasReminder,toDoDate);
        mFirebaseDatabase.child(Phone_with_plus).child(alert_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mFirebaseDatabase.child(Phone_with_plus).child(alert_id).setValue(toDoItem).addOnCompleteListener
                            (new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(AddMedicinrsActivity.this, "تم تعديل الدواء  ", Toast.LENGTH_SHORT).show();

                                }
                            });

                } else {
                    mFirebaseDatabase.child(Phone_with_plus).child(alert_id).setValue(toDoItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActiviy(), "تم اضافة الدواء بنجاح ", Toast.LENGTH_SHORT).show();

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

