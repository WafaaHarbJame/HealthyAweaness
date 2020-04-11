package com.healthy.healthyaweaness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.healthy.healthyaweaness.Activity.BaseActivity;
import com.healthy.healthyaweaness.Activity.LoginActivity;
import com.healthy.healthyaweaness.Fragment.AboutFragment;
import com.healthy.healthyaweaness.Fragment.AddingMedicineFragment;
import com.healthy.healthyaweaness.Fragment.HomeFragment;
import com.healthy.healthyaweaness.Fragment.MedicineListFragment;
import com.healthy.healthyaweaness.Model.AppConstants;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends BaseActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor_signUp;
    Toolbar toolbar;
    private Boolean saveLogin;
    boolean GO_to_Medicine;
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);

        Menu menu = navigationView.getMenu();

        if (saveLogin) {
            menu.findItem(R.id.nav_exitapp).setTitle(R.string.logout);



        } else {
            menu.findItem(R.id.nav_exitapp).setTitle(getString(R.string.login));



        }
        GO_to_Medicine=getIntent().getBooleanExtra("GO_to_Medicine",false);

        if(GO_to_Medicine){
            toolbar.setTitle(getString(R.string.Medicine_list));
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new MedicineListFragment(),
                "HomeFragment").commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment(),
                    "HomeFragment").commit();

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
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment(), "HomeFragment").commit();

        }

        else if (id == R.id.nav_aboutapp) {

            toolbar.setTitle(getString(R.string.menu_about));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new AboutFragment(), "HomeFragment").commit();

        }

        else if (id == R.id.nav_Medicine_list) {

            toolbar.setTitle(getString(R.string.Medicine_list));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new MedicineListFragment(), "HomeFragment").commit();

        }
        else if (id == R.id.nav_share) {
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


}
