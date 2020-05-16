package com.mikeinvents.triviaongo.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mikeinvents.triviaongo.R;
import com.mikeinvents.triviaongo.model.QuestionFormOne;
import com.mikeinvents.triviaongo.model.QuestionFormTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String QUIZ_ACTIVITY_TAG = QuizActivity.class.getSimpleName();
    private static final long COUNTDOWN_IN_MILLIS_HARD = 41000;
    private static final long COUNTDOWN_IN_MILLIS_MEDIUM = 31000;
    private static final long COUNTDOWN_IN_MILLIS_EASY = 21000;

    public static final String COURSE_TAKEN = "course_taken";
    public static final String COURSE_SCORE = "course_score";
    public static final String COURSE_QUESTION_TOTAL = "total_questions_taken";

    private ArrayList<QuestionFormOne> mQuestionFormOneLists;
    private ArrayList<QuestionFormTwo> mQuestionFormTwoLists;

    private RequestQueue mRequestQueue;

    public static String correctAnswer;
    public static ArrayList<String> correctMultipleAnswers;
    public CharSequence formattedCorrectAnswer;

    private String amount, category, difficulty, type, url;
    
    private int questionCountTotal;
    private int questionCounter;
    private int score;
    private boolean answered;

    private TextView mQuestion, mQuestionNum, mScore, mCount;
    private RadioButton mOptionA;
    private RadioButton mOptionB;
    private RadioButton mOptionC;
    private RadioButton mOptionD;
    private Button mButtonNext;
    private RelativeLayout quizLayoutOne, quizLayoutTwo;

    private RadioGroup radioGroup;
    RadioButton supposedCorrectAnswer = null;
    public static ProgressDialog progressDialog;
    public ProgressBar progressBar;
    private ColorStateList textColorDefault;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private Intent intentForLayout;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentForLayout = getIntent();
        if(intentForLayout.getStringExtra(SelectQuestion.TYPE).equalsIgnoreCase("boolean")){
            setContentView(R.layout.quiz_activity_two);
            findSecondViewsByIdes();
        }else{
            setContentView(R.layout.activity_qiuz);
            findViewsByIdes();
        }

        Toolbar toolbar = findViewById(R.id.quiz_toolbar);
        setSupportActionBar(toolbar);

        mRequestQueue = Volley.newRequestQueue(this);
        textColorDefault = mOptionA.getTextColors();

        mQuestionFormOneLists = new ArrayList<>();
        correctMultipleAnswers = new ArrayList<>();
        questionCounter = 0;
        score = 0;

        mQuestionFormTwoLists = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Message...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        new QuizAsyncTask(progressDialog).execute(); //this where we parse our JSONObject

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showNextQuestion();
                if(!answered){
                    if(mOptionA.isChecked() || mOptionB.isChecked() || mOptionC.isChecked() || mOptionD.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showNextQuestion();
                }
            }
        });
        
    }

    /** Used to check if answer is right or wrong*/
    private void checkAnswer() {
        String selectedAnswer, markedAnswer;
        answered = true;
        countDownTimer.cancel();
        RadioButton selectedButton = findViewById(radioGroup.getCheckedRadioButtonId());

        //to get correct radio button
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        int count = radioGroup.getChildCount();
        for(int i=0; i<count; i++){
            View obj = radioGroup.getChildAt(i);
            if(obj instanceof RadioButton){
                radioButtons.add((RadioButton) obj);
            }
        }

        for(int j=0; j<radioButtons.size(); j++){
            supposedCorrectAnswer = findViewById(radioButtons.get(j).getId());
            markedAnswer = supposedCorrectAnswer.getText().toString();
            if(markedAnswer.equalsIgnoreCase(correctAnswer)){
                break;
            }

        }


        if(selectedButton == null){
            selectedAnswer = "";
        }else{
            selectedAnswer = selectedButton.getText().toString();
            //Log.i(QUIZ_ACTIVITY_TAG,"Selected answer = "+selectedAnswer);
        }

       // Log.i(QUIZ_ACTIVITY_TAG, "Correct answer = "+correctAnswer);

        if(correctAnswer.equalsIgnoreCase(selectedAnswer)){
            score++;
            String str = getString(R.string.quiz_score_text) + score;
            mScore.setText(str);
            if (selectedButton != null) {
                selectedButton.setBackground(getDrawable(R.drawable.correct_answer_marking));
            }

        }else{
            if (selectedButton != null) {
                selectedButton.setBackground(getDrawable(R.drawable.wrong_answer_marking));
                if (supposedCorrectAnswer != null) {
                    supposedCorrectAnswer.setBackground(getDrawable(R.drawable.correct_answer_marking));
                }
            }
        }

        showAnotherQuestion();

    }

    private void showNextQuestion() {
        //make question visible
        if(intentForLayout.getStringExtra(SelectQuestion.TYPE).equalsIgnoreCase("boolean")){
            quizLayoutTwo.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            showLayoutTwoQuestion();
        }else{
            quizLayoutOne.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            showLayoutOneQuestion();
        }

    }

    /**Used to display questions for boolean question set */
    private void showLayoutTwoQuestion(){
        if(mOptionA.isChecked() || mOptionB.isChecked()){
            mOptionA.setTextColor(textColorDefault);
            mOptionB.setTextColor(textColorDefault);
            mOptionA.setBackground(getDrawable(R.drawable.radio_button_background));
            mOptionB.setBackground(getDrawable(R.drawable.radio_button_background));
            radioGroup.clearCheck();
        }

        if(questionCounter < questionCountTotal){
            QuestionFormTwo currentQuestionTypeTwo = mQuestionFormTwoLists.get(questionCounter);

            mQuestion.setText(currentQuestionTypeTwo.getQuestionText());
            mOptionA.setText(currentQuestionTypeTwo.getAnswerOne());
            mOptionB.setText(currentQuestionTypeTwo.getAnswerTwo());

            correctAnswer = correctMultipleAnswers.get(questionCounter);
            questionCounter++;
            String str = getString(R.string.quiz_question_text) + questionCounter + "/" + questionCountTotal;
            mQuestionNum.setText(str);
            answered = false;

            if(questionCounter == questionCountTotal){
                mButtonNext.setText(getString(R.string.quiz_finish));
            }
            else{
                mButtonNext.setText(getString(R.string.quiz_confirm));
            }

            if(difficulty.equalsIgnoreCase("easy")){
                timeLeftInMillis = COUNTDOWN_IN_MILLIS_EASY;
            }else if(difficulty.equalsIgnoreCase("medium")){
                timeLeftInMillis = COUNTDOWN_IN_MILLIS_MEDIUM;
            }else{
                timeLeftInMillis = COUNTDOWN_IN_MILLIS_HARD;
            }

            startCountDown();

        }else {
            finishQuiz();
        }

       // Log.i(QUIZ_ACTIVITY_TAG,"Question counter: "+questionCounter);
       // Log.i(QUIZ_ACTIVITY_TAG,"Question Total: "+questionCountTotal);
    }

    /**Used to display questions for multiple question set*/
    private void showLayoutOneQuestion(){
        if(mOptionA.isChecked() || mOptionB.isChecked() ||
                mOptionC.isChecked() || mOptionD.isChecked()){

            mOptionA.setTextColor(textColorDefault);
            mOptionB.setTextColor(textColorDefault);
            mOptionC.setTextColor(textColorDefault);
            mOptionD.setTextColor(textColorDefault);
            mOptionA.setBackground(getDrawable(R.drawable.radio_button_background));
            mOptionB.setBackground(getDrawable(R.drawable.radio_button_background));
            mOptionC.setBackground(getDrawable(R.drawable.radio_button_background));
            mOptionD.setBackground(getDrawable(R.drawable.radio_button_background));
            radioGroup.clearCheck();
        }

        if(questionCounter < questionCountTotal){
            QuestionFormOne currentQuestionTypeOne = mQuestionFormOneLists.get(questionCounter);

            mQuestion.setText(currentQuestionTypeOne.getQuestionText());
            mOptionA.setText(currentQuestionTypeOne.getOptionA());
            mOptionB.setText(currentQuestionTypeOne.getOptionB());
            mOptionC.setText(currentQuestionTypeOne.getOptionC());
            mOptionD.setText(currentQuestionTypeOne.getOptionD());

            correctAnswer = correctMultipleAnswers.get(questionCounter);
            questionCounter++;
            String str = getString(R.string.quiz_question_text) + questionCounter + "/" + questionCountTotal;
            mQuestionNum.setText(str);
            answered = false;

            if(questionCounter == questionCountTotal){
                mButtonNext.setText(getString(R.string.quiz_finish));
            }
            else{
                mButtonNext.setText(getString(R.string.quiz_confirm));
            }

            if(difficulty.equalsIgnoreCase("easy")){
                timeLeftInMillis = COUNTDOWN_IN_MILLIS_EASY;
            }else if(difficulty.equalsIgnoreCase("medium")){
                timeLeftInMillis = COUNTDOWN_IN_MILLIS_MEDIUM;
            }else{
                timeLeftInMillis = COUNTDOWN_IN_MILLIS_HARD;
            }

            startCountDown();

        }else {
            finishQuiz();
        }

        //Log.i(QUIZ_ACTIVITY_TAG,"Question counter: "+questionCounter);
       // Log.i(QUIZ_ACTIVITY_TAG,"Question Total: "+questionCountTotal);
    }

    /**Used to end quiz and pass the results from quiz to next the next Activity */
    private void finishQuiz() {
        Intent finishIntent = new Intent(QuizActivity.this, ResultActivity.class);
        finishIntent.putExtra(COURSE_TAKEN,category);
        finishIntent.putExtra(COURSE_SCORE,String.valueOf(score));
        finishIntent.putExtra(COURSE_QUESTION_TOTAL,amount);
        startActivity(finishIntent);
        finish();
    }

    private void showAnotherQuestion() {
        if(questionCounter < questionCountTotal){
            mButtonNext.setText(getString(R.string.quiz_next));
        }else {
            mButtonNext.setText(getString(R.string.quiz_finish));
        }
    }

    /**Used to set countdown on screen */
    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                //updateCountText();
                checkAnswer();
                Snackbar.make(mButtonNext, "No option selected", Snackbar.LENGTH_LONG).show();
                supposedCorrectAnswer.setBackground(getDrawable(R.drawable.correct_answer_marking));
            }
        }.start();
    }

    /**Used to update timer on screen */
    private void updateCountText(){

        if(timeLeftInMillis < 10000){
            mCount.setBackgroundResource(R.drawable.time_up_drawable);
            YoYo.with(Techniques.Shake)
                    .duration(1000)
                    .playOn(mCount);

            ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_MUSIC,1000);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP,1000);
        }else{
            mCount.setBackgroundResource(R.drawable.time_drawable);
        }

        int time = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(),"%02d",time);
        mCount.setText(timeFormatted);

    }

    private void findViewsByIdes(){
        mQuestion = findViewById(R.id.question_card_one_text_view);
        mOptionA = findViewById(R.id.question_card_one_option_a);
        mOptionB = findViewById(R.id.question_card_one_option_b);
        mOptionC = findViewById(R.id.question_card_one_option_c);
        mOptionD = findViewById(R.id.question_card_one_option_d);
        mButtonNext = findViewById(R.id.question_card_one_next_button);

        mQuestionNum = findViewById(R.id.quiz_text_view_question_num);
        radioGroup = findViewById(R.id.question_card_one_radio_group);
        mScore = findViewById(R.id.quiz_text_view_score);
        mCount = findViewById(R.id.quiz_text_view_counter);
        quizLayoutOne = findViewById(R.id.quiz_card_one);
        progressBar = findViewById(R.id.quiz_card_one_progress);

    }

    private void findSecondViewsByIdes(){
        mQuestion = findViewById(R.id.question_card_two_text_view);
        mOptionA = findViewById(R.id.question_card_two_option_a);
        mOptionB = findViewById(R.id.question_card_two_option_b);
        mButtonNext = findViewById(R.id.question_card_two_next_button);

        mQuestionNum = findViewById(R.id.quiz_text_view_question_num_two);
        radioGroup = findViewById(R.id.question_card_two_radio_group);
        mScore = findViewById(R.id.quiz_text_view_score_two);
        mCount = findViewById(R.id.quiz_text_view_counter_two);
        quizLayoutTwo = findViewById(R.id.quiz_card_two);
        progressBar = findViewById(R.id.quiz_card_two_progress);

    }

    /**Used to get questions from the online response */
    public void parseJSON() {
        //get selected question format
        Intent intent = getIntent();
        amount = intent.getStringExtra(SelectQuestion.AMOUNT);
        category = intent.getStringExtra(SelectQuestion.CATEGORY);
        difficulty = intent.getStringExtra(SelectQuestion.DIFFICULTY);
        type = intent.getStringExtra(SelectQuestion.TYPE);

        if(category.equalsIgnoreCase("any category")){
            amount = "";
        }

        urlBuilder(amount,category,difficulty,type); //build url
      //  parseQuestions(url);


        //making api call request to online database
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    JSONArray temp;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get the results from JSONArray
                            JSONArray jsonArray = response.getJSONArray("results");
                           // Log.i(QUIZ_ACTIVITY_TAG,"jsonArray Length = "+jsonArray.length());

                            if(jsonArray.length() == 0){
                                //get the questions
                                getDefaultQuestions();
                            }else {
                                //get a particular result from JSONObject
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject result = jsonArray.getJSONObject(i);

                                    //get question and correct answer
                                    String questionText = result.getString("question");
                                    CharSequence formattedQuestion = Html.fromHtml(questionText);
                                    correctAnswer = result.getString("correct_answer");
                                    formattedCorrectAnswer = Html.fromHtml(correctAnswer);
                                    correctMultipleAnswers.add(formattedCorrectAnswer.toString());

                                   temp = result.getJSONArray("incorrect_answers");

                                    //for boolean questions
                                    if (temp.length() == 1) {
                                    String wrongAnswer = temp.getString(0);
                                    CharSequence formattedWrongAnswer = Html.fromHtml(wrongAnswer);

                                    //shuffle options
                                    ArrayList<String> booleanArr = shuffleOptionBoolean(
                                            formattedCorrectAnswer.toString(),
                                            formattedWrongAnswer.toString());

                                    String firstOption = booleanArr.get(0);
                                    String secondOption = booleanArr.get(1);

                                    //add to adapter
                                    mQuestionFormTwoLists.add(new QuestionFormTwo(formattedQuestion.toString(),
                                            firstOption,secondOption));
                                   // Log.i(QUIZ_ACTIVITY_TAG,"BOOLEAN QUESTION ADDED");
                                    } else {
                                        String[] incorrectAnswers = new String[temp.length()];
                                        for (int j = 0; j < temp.length(); j++) {
                                            incorrectAnswers[j] = temp.getString(j);
                                        }

                                        String firstIncorrectOption = incorrectAnswers[2];
                                        String secondIncorrectOption = incorrectAnswers[0];
                                        String thirdIncorrectOption = incorrectAnswers[1];

                                        //format html tags here
                                        CharSequence formatFirstOption = Html.fromHtml(firstIncorrectOption);
                                        CharSequence formatSecondOption = Html.fromHtml(secondIncorrectOption);
                                        CharSequence formatThirdOption = Html.fromHtml(thirdIncorrectOption);

                                        //shuffle options
                                        ArrayList<String> multipleArr =
                                                shuffleOptionMultiple(formatFirstOption.toString(),
                                                        formattedCorrectAnswer.toString(),
                                                        formatSecondOption.toString(),
                                                        formatThirdOption.toString());


                                        String firstOption = multipleArr.get(0);
                                        String secondOption = multipleArr.get(1);
                                        String thirdOption = multipleArr.get(2);
                                        String fourthOption = multipleArr.get(3);

                                        //pass to form to list
                                        mQuestionFormOneLists.add(new QuestionFormOne(
                                                formattedQuestion.toString(), firstOption, secondOption,
                                                thirdOption, fourthOption));

                                   // Log.i(QUIZ_ACTIVITY_TAG,"MULTIPLE QUESTION ADDED");
                                    }

                                }

                               // Log.i(QUIZ_ACTIVITY_TAG, "Finished parsing");


                                if(temp.length() == 1){
                                   // Log.i(QUIZ_ACTIVITY_TAG, "List size is: " + mQuestionFormTwoLists.size());
                                    questionCountTotal = mQuestionFormTwoLists.size();
                                    showNextQuestion();
                                }else {
                                   // Log.i(QUIZ_ACTIVITY_TAG, "List size is: " + mQuestionFormOneLists.size());
                                    questionCountTotal = mQuestionFormOneLists.size();
                                    showNextQuestion();
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //an error occurred while parsing JSONObject
               // Log.i(QUIZ_ACTIVITY_TAG,error.toString());
                responseForError(error);
            }
        });

        mRequestQueue.add(request);
    }

    /**Used to shuffle the boolean options */
    private ArrayList<String> shuffleOptionBoolean(String firstOption, String secondOption){
        ArrayList<String> booleanAnswers = new ArrayList<>();
        booleanAnswers.add(firstOption);
        booleanAnswers.add(secondOption);

        Collections.shuffle(booleanAnswers);

        return booleanAnswers;
    }

    /**Used to shuffle the multiple options */
    private ArrayList<String> shuffleOptionMultiple(String firstOption, String secondOption, String thirdOption,
                                       String fourthOption){
        ArrayList<String> multipleAnswers = new ArrayList<>();
        multipleAnswers.add(firstOption);
        multipleAnswers.add(secondOption);
        multipleAnswers.add(thirdOption);
        multipleAnswers.add(fourthOption);

        Collections.shuffle(multipleAnswers);

        return multipleAnswers;
    }
    
    /**Used to get questions from Any Category Section.*/
    private void getDefaultQuestions(){
        //Get the the 'Any Category' questions
        QuizDialog dialog = new QuizDialog();
        dialog.showDialog(QuizActivity.this,"MESSAGE"
                ,"The question you selected does not exist.\n\nDon't worry we have selected some nice ones for you!"
                , false);
        category = "";
        urlBuilder(amount,category,difficulty,type);
        parseQuestions(url);

    }

    /** To build url that gets questions from database
     *@link opentdb.com*/
    private void urlBuilder(String amount, String category, String difficulty, String type){
        //build url or link to database
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("opentdb.com")
                .appendPath("api.php")
                .appendQueryParameter("amount",amount)
                .appendQueryParameter("category",category)
                .appendQueryParameter("difficulty",difficulty)
                .appendQueryParameter("type",type);

        url = builder.build().toString();
    }

    /**Used as a default, if original parseJSON() method fails - does the same work as original */
    private void parseQuestions(String url){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    JSONArray temp;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get the results from JSONArray
                            JSONArray jsonArray = response.getJSONArray("results");

                            //get a particular result from JSONObject
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                //get question and correct answer
                                String questionText = result.getString("question");
                                CharSequence formattedQuestion = Html.fromHtml(questionText);
                                correctAnswer = result.getString("correct_answer");
                                formattedCorrectAnswer = Html.fromHtml(correctAnswer);
                                correctMultipleAnswers.add(formattedCorrectAnswer.toString());

                                temp = result.getJSONArray("incorrect_answers");

                                //for boolean questions
                                if(temp.length() == 1){
                                    String wrongAnswer = temp.getString(0);
                                    CharSequence formattedWrongAnswer = Html.fromHtml(wrongAnswer);

                                    //shuffle options
                                    ArrayList<String> booleanArr = shuffleOptionBoolean(
                                            formattedCorrectAnswer.toString(),
                                            formattedWrongAnswer.toString());

                                    String firstOption = booleanArr.get(0);
                                    String secondOption = booleanArr.get(1);

                                    //add to adapter
                                    mQuestionFormTwoLists.add(new QuestionFormTwo(formattedQuestion.toString(),
                                            firstOption,secondOption));
                                    //Log.i(QUIZ_ACTIVITY_TAG,"BOOLEAN QUESTION ADDED");
                                }else {
                                    String [] incorrectAnswers = new String[temp.length()];
                                    for(int j=0; j<temp.length(); j++){
                                        incorrectAnswers[j] = temp.getString(j);
                                    }

                                    String firstIncorrectOption = incorrectAnswers[2];
                                    String secondIncorrectOption = incorrectAnswers[0];
                                    String thirdIncorrectOption = incorrectAnswers[1];

                                    //format html tags here
                                    CharSequence formatFirstOption = Html.fromHtml(firstIncorrectOption);
                                    CharSequence formatSecondOption = Html.fromHtml(secondIncorrectOption);
                                    CharSequence formatThirdOption = Html.fromHtml(thirdIncorrectOption);

                                    //shuffle options
                                    ArrayList<String> multipleArr =
                                            shuffleOptionMultiple(formatFirstOption.toString(),
                                                    formattedCorrectAnswer.toString(),
                                                    formatSecondOption.toString(),
                                                    formatThirdOption.toString());


                                    String firstOption = multipleArr.get(0);
                                    String secondOption = multipleArr.get(1);
                                    String thirdOption = multipleArr.get(2);
                                    String fourthOption = multipleArr.get(3);

                                    //pass to form to list
                                    mQuestionFormOneLists.add(new QuestionFormOne(
                                            formattedQuestion.toString(), firstOption,secondOption,
                                            thirdOption, fourthOption));


                                    //Log.i(QUIZ_ACTIVITY_TAG,"MULTIPLE QUESTION ADDED");
                                }

                            }

                            if(temp.length() == 1){
                                //Log.i(QUIZ_ACTIVITY_TAG, "List size is: " + mQuestionFormTwoLists.size());
                                questionCountTotal = mQuestionFormTwoLists.size();
                                showNextQuestion();
                            }else {
                                //Log.i(QUIZ_ACTIVITY_TAG, "List size is: " + mQuestionFormOneLists.size());
                                questionCountTotal = mQuestionFormOneLists.size();
                                showNextQuestion();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i(QUIZ_ACTIVITY_TAG,error.toString());
                responseForError(error);
            }
        });

        mRequestQueue.add(request);
    }

    /**Used to check for error while parsing data.*/
    private void responseForError(VolleyError error){

        if(error instanceof NoConnectionError){
            //thrown if there is no network connection
            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SelectQuestion.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            //thrown if there connection timed out due to slow parsing of data or slow network connection
            Toast.makeText(this, getString(R.string.error_time_out), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SelectQuestion.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class QuizAsyncTask extends AsyncTask<Void,Void,Void> {
        QuizAsyncTask(ProgressDialog progressDialog){
            QuizActivity.progressDialog = progressDialog;
        }


        @Override
        protected void onPreExecute() {
           progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseJSON();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(mButtonNext,"Please click BACK again to exit",Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
