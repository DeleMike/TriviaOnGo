package com.mikeinvents.triviaongo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikeinvents.triviaongo.R;

public class ResultActivity extends AppCompatActivity {
    private Button finishButton, retakeButton;
    boolean doubleBackToExitPressedOnce = false;
    private String [] messages = {"Try again...","Nice Attempt!", "Congratulations!"};
    private int [] imageResources = {R.drawable.green_frowny,R.drawable.result_page_pic,R.drawable.result_page_pic};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar= findViewById(R.id.result_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String courseTaken = intent.getStringExtra(QuizActivity.COURSE_TAKEN);
        //Log.i(QuizActivity.QUIZ_ACTIVITY_TAG,"Course Taken: "+ courseTaken);
        String courseScore = intent.getStringExtra(QuizActivity.COURSE_SCORE);
        //Log.i(QuizActivity.QUIZ_ACTIVITY_TAG,"Course Taken: "+ courseScore);
        String courseTotalNum = intent.getStringExtra(QuizActivity.COURSE_QUESTION_TOTAL);
        //Log.i(QuizActivity.QUIZ_ACTIVITY_TAG,"Course Taken: "+ courseTotalNum);

        TextView courseTaken_textView = findViewById(R.id.quiz_result_course);
        TextView courseScore_textView = findViewById(R.id.quiz_result_score);
        TextView specialMsg_textView = findViewById(R.id.quiz_result_special_msg);
        finishButton = findViewById(R.id.quiz_result_finish);
        retakeButton = findViewById(R.id.quiz_result_retake);
        TextView congratsMsg_textView = findViewById(R.id.quiz_result_congrats_msg);
        ImageView rewardImage = findViewById(R.id.quiz_reward_img);

        courseTaken = SelectQuestion.courseName;
        courseTaken_textView.append(" "+ courseTaken);
        String formatScore = getString(R.string.quiz_score_text) + " " + courseScore + "/" + courseTotalNum;
        courseScore_textView.setText(formatScore);


        if(Integer.parseInt(courseScore) == 0){
            congratsMsg_textView.setText(messages[0]);
            rewardImage.setImageResource(imageResources[0]);
        }else if(Integer.parseInt(courseScore) == Integer.parseInt(courseTotalNum)){
            specialMsg_textView.setVisibility(View.VISIBLE);
            congratsMsg_textView.setText(messages[2]);
            rewardImage.setImageResource(imageResources[2]);
        }else{
            congratsMsg_textView.setText(messages[1]);
            rewardImage.setImageResource(imageResources[1]);
        }

        SharedPreferences.Editor editor = WelcomeActivity.sharedPreferences.edit();
        editor.putString(WelcomeActivity.COURSE_TAKEN, courseTaken)
                .putString(WelcomeActivity.COURSE_SCORE,formatScore)
                .apply();

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, SelectQuestion.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(finishButton,"Please click BACK again to exit",Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
