package com.mikeinvents.triviaongo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikeinvents.triviaongo.R;
import com.mikeinvents.triviaongo.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FILE_NAME = "com.mikeinvents.triviaongo.FILE_NAME";
    public static final String LOGIN_STATUS = "ISLOGGEDIN";
    public static final String USER_NAME = "User";

    EditText editText;
    Button button;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
       // drawer.setVisibility(View.GONE);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setVisibility(View.GONE);

        findViewsByIdes();

        sharedPreferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(LOGIN_STATUS, false);

        if(isLoggedIn){
            Intent changeIntent = getIntent();
            String name = changeIntent.getStringExtra(SettingsFragment.CHANGE_NAME);
            if(name == null){
                // start activity
                Intent intent = new Intent(this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //end main activity
            }

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void findViewsByIdes(){
        editText = findViewById(R.id.edit_main_user_name);
        button = findViewById(R.id.main_button_save);
    }

    public void saveUserName(View view) {
        /* collect user data because this only occurs if user is a first time user
        * and then start the Welcome activity*/
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME,editText.getText().toString());
        //save the boolean state
        editor.putBoolean(LOGIN_STATUS, true);
        editor.apply();
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();

    }

}
