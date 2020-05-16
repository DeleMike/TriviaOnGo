package com.mikeinvents.triviaongo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeinvents.triviaongo.R;

import java.util.Calendar;

public class WelcomeActivity extends AppCompatActivity {
    public static final String REPORT_CARD = "REPORT";
    public static final String COURSE_TAKEN = "COURSE";
    public static final String COURSE_SCORE = "COURSE_SCORE";

    public static SharedPreferences sharedPreferences;

    private Button welcomeButton;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = findViewById(R.id.welcome_toolbar);
        setSupportActionBar(toolbar);

        welcomeButton = findViewById(R.id.welcome_button);
        welcomeText = findViewById(R.id.welcome_greetings_text);
        TextView courseTaken_text = findViewById(R.id.welcome_last_course_taken);
        TextView courseScore_text = findViewById(R.id.welcome_last_course_score);

        getUserDetails();

        sharedPreferences = getSharedPreferences(REPORT_CARD,MODE_PRIVATE);
        String courseTaken = sharedPreferences.getString(COURSE_TAKEN," Not Available");
        String courseScore = sharedPreferences.getString(COURSE_SCORE," Not Available");

        courseTaken_text.append(" " + courseTaken);
        courseScore_text.append(" " + courseScore);


    }

    private void getUserDetails(){
        String textToAppend = "Ready for the challenge or do you think you worth answering this questions?";
        //get the saved user's name
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.FILE_NAME,MODE_PRIVATE);
        String userName = sharedPreferences.getString(MainActivity.USER_NAME,"USER!");

        //to get local time of user
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay <6) {
            welcomeText.append("Early Bird- ");
        }else if(timeOfDay >= 6 && timeOfDay <12){
            welcomeText.append("Good Morning, ");
        }else if (timeOfDay >= 12 && timeOfDay < 16){
            welcomeText.append("Good Afternoon, ");
        }else if(timeOfDay >= 16 && timeOfDay < 24){
            welcomeText.append("Good Evening, ");
        }

        welcomeText.append(userName + "! ");
        welcomeText.append(textToAppend);

        //launch fragment to launch the select-Question-Type
        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    Intent intent = new Intent(WelcomeActivity.this, SelectQuestion.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    QuizDialog dialog = new QuizDialog();
                    dialog.showDialog(WelcomeActivity.this,"Network Error",
                            "Please ensure you are connected to a Wifi network or the Internet"
                            ,true);
                }

            }
        });
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welcome_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ic_welcome_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
