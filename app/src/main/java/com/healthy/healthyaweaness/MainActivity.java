package com.healthy.healthyaweaness;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.healthy.healthyaweaness.Activity.BaseActivity;
import com.healthy.healthyaweaness.Activity.LoginActivity;
import com.healthy.healthyaweaness.Activity.ReminderActivity;
import com.healthy.healthyaweaness.All.CustomRecyclerScrollViewListener;
import com.healthy.healthyaweaness.DB.AnalyticsApplication;
import com.healthy.healthyaweaness.DB.StoreRetrieveData;
import com.healthy.healthyaweaness.Fragment.AboutFragment;
import com.healthy.healthyaweaness.Fragment.AddingMedicineFragment;
import com.healthy.healthyaweaness.Fragment.HomeFragment;
import com.healthy.healthyaweaness.Fragment.MedicineListFragment;
import com.healthy.healthyaweaness.Fragment.MedicinesListFragmentFragment;
import com.healthy.healthyaweaness.Model.AppConstants;
import com.healthy.healthyaweaness.Model.ToDoItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends BaseActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor_signUp;
    Toolbar toolbar;
    private Boolean saveLogin;
    boolean GO_to_Medicine;
    private AnalyticsApplication app;

    public static final String TODOITEM = "com.healthy.healthyaweaness.MainActivity";
    private com.healthy.healthyaweaness.Activity.MainActivity.BasicListAdapter adapter;
    private static final int REQUEST_ID_TODO_ITEM = 100;
    private ToDoItem mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    public static final String DATE_TIME_FORMAT_12_HOUR = "MMM d, yyyy  h:mm a";
    public static final String DATE_TIME_FORMAT_24_HOUR = "MMM d, yyyy  k:mm";
    public static final String FILENAME = "todoitems.json";
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
    public static final String DARKTHEME = "com.avjindersekon.darktheme";
    public static final String LIGHTTHEME = "com.avjindersekon.lighttheme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(AppConstants.KEY_FILE, MODE_PRIVATE);
        saveLogin = sharedPreferences.getBoolean(AppConstants.ISLOGIN, false);
        getSupportActionBar().setTitle(getString(R.string.menu_home));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        app = (AnalyticsApplication)getApplication();

        navigationView.setItemIconTintList(null);
        String fcm_token = FirebaseInstanceId.getInstance().getToken();
        Log.e("fcm_token", "fcm_token" + fcm_token);

        Menu menu = navigationView.getMenu();

        if (saveLogin) {
            menu.findItem(R.id.nav_exitapp).setTitle(R.string.logout);


        } else {
            menu.findItem(R.id.nav_exitapp).setTitle(getString(R.string.login));


        }
        GO_to_Medicine = getIntent().getBooleanExtra("practice", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
        }
        Log.e("practice","practice"+GO_to_Medicine);

        if (GO_to_Medicine) {
            toolbar.setTitle(getString(R.string.Pracctice));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment(), "HomeFragment").commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new MedicinesListFragmentFragment(), "HomeFragment").commit();

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//
//
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            toolbar.setTitle(getString(R.string.menu_home));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new MedicinesListFragmentFragment(), "HomeFragment").commit();

        } else if (id == R.id.nav_aboutapp) {

            toolbar.setTitle(getString(R.string.menu_about));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new AboutFragment(), "HomeFragment").commit();

        } else if (id == R.id.nav_Medicine_list) {

            toolbar.setTitle(getString(R.string.Pracctice));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment(), "HomeFragment").commit();

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "share the app : https://play.google.com/store/apps/details?id=com.healthy.healthyaweaness");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_exitapp) {
            Toast("تم تسجيل الخروج بنجاح");
            if (saveLogin) {
                editor_signUp = sharedPreferences.edit();
                editor_signUp.clear();
                editor_signUp.apply();
                editor_signUp.commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra(AppConstants.ISLOGIN, false);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);


            }


        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static ArrayList<ToDoItem> getLocallyStoredData(StoreRetrieveData storeRetrieveData) {
        ArrayList<ToDoItem> items = null;

        try {
            items = storeRetrieveData.loadFromFile();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    @Override
    protected void onResume() {
        super.onResume();
        app.send(this);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(ReminderActivity.EXIT, false)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ReminderActivity.EXIT, false);
            editor.apply();
            finish();
            finish();
        }


    }

}
