package com.mikeinvents.triviaongo.ui;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikeinvents.triviaongo.R;
import com.mikeinvents.triviaongo.model.Category;
import com.mikeinvents.triviaongo.model.Difficulty;
import com.mikeinvents.triviaongo.model.InputFilterMinMax;
import com.mikeinvents.triviaongo.model.SelectType;

import java.util.ArrayList;

public class SelectQuestion extends AppCompatActivity {
    public static final String AMOUNT = SelectQuestion.class.getSimpleName() + "_AMOUNT";
    public static final String CATEGORY = SelectQuestion.class.getSimpleName() + "_CATEGORY";
    public static final String DIFFICULTY = SelectQuestion.class.getSimpleName() + "_DIFFICULTY";
    public static final String TYPE = SelectQuestion.class.getSimpleName() + "_TYPE";

    private Button doneButton;
    private EditText edit_numOfQuestion;
    private Spinner selectCategory, selectDifficulty, selectType;

    private String categoryStr, difficultyStr, selectTypeStr;
    public static String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_question);

        Toolbar toolbar = findViewById(R.id.select_question_toolbar);
        setSupportActionBar(toolbar);

        findViewByIdes();

        setCategoryData();
        setDifficultyData();
        setSelectTypeData();

        edit_numOfQuestion.setFilters(new InputFilter[]{
                new InputFilterMinMax(SelectQuestion.this,"1","50")});

        selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category)parent.getSelectedItem();
                courseName = category.getCategoryName();
                categoryStr = String.valueOf(category.getCategoryId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Difficulty difficulty = (Difficulty)parent.getSelectedItem();
                difficultyStr = difficulty.getDifficultyId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectType selectType = (SelectType)parent.getSelectedItem();
                selectTypeStr = selectType.getSelectTypeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()){
                    //pass preferred question format into intent
                    Intent intent = new Intent(SelectQuestion.this, QuizActivity.class);
                    intent.putExtra(AMOUNT, edit_numOfQuestion.getText().toString());
                    intent.putExtra(CATEGORY, categoryStr);
                    intent.putExtra(DIFFICULTY, difficultyStr);
                    intent.putExtra(TYPE, selectTypeStr);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else {
                    QuizDialog quizDialog = new QuizDialog();
                    quizDialog.showDialog(SelectQuestion.this,"Network Error",
                            "Please ensure you are connected to a Wifi network or the Internet", true);
                }
            }
        });
    }


    /**Used to fill in the category spinner and assign numbers to them */
    private void setCategoryData(){
        ArrayList<Category> categories = new ArrayList<>();
        //add categories
        categories.add(new Category("Any Category",0));
        categories.add(new Category("General Knowledge", 9));
        categories.add(new Category("Entertainment: Books", 10));
        categories.add(new Category("Entertainment: Film", 11));
        categories.add(new Category("Entertainment: Music", 12));
        categories.add(new Category("Entertainment: Musicals & Theaters", 13));
        categories.add(new Category("Entertainment: Television", 14));
        categories.add(new Category("Entertainment: Video Games", 15));
        categories.add(new Category("Entertainment: Board Games", 16));
        categories.add(new Category("Science & Nature", 17));
        categories.add(new Category("Science: Computers", 18));
        categories.add(new Category("Science: Mathematics", 19));
        categories.add(new Category("Mythology", 20));
        categories.add(new Category("Sport", 21));
        categories.add(new Category("Geography", 22));
        categories.add(new Category("History", 23));
        categories.add(new Category("Politics", 24));
        categories.add(new Category("Art", 25));
        categories.add(new Category("Celebrities", 26));
        categories.add(new Category("Animals", 27));
        categories.add(new Category("Vehicles", 28));
        categories.add(new Category("Entertainment: Comics", 29));
        categories.add(new Category("Science: Gadgets", 30));
        categories.add(new Category("Entertainment: Japanese Anime & Manga", 31));
        categories.add(new Category("Entertainment: Cartoon & Animations", 32));

        //fill data in selectCategory Spinner
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        selectCategory.setAdapter(adapter);

    }

    /**Used to fill in the difficulty spinner and assign their values to them */
    private void setDifficultyData(){
        ArrayList<Difficulty> difficulties = new ArrayList<>();
        //add difficulties
        difficulties.add(new Difficulty("Easy", "easy"));
        difficulties.add(new Difficulty("Medium", "medium"));
        difficulties.add(new Difficulty("Hard", "hard"));

        //fill in data
        ArrayAdapter<Difficulty> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, difficulties);
        selectDifficulty.setAdapter(adapter);
    }

    /**Used to fill in the select type spinner and assign their values to them */
    private void setSelectTypeData(){
        ArrayList<SelectType> selectTypes = new ArrayList<>();
        //add select types
        selectTypes.add(new SelectType("Multiple Choice", "multiple"));
        selectTypes.add(new SelectType("True / False","boolean"));

        //fill in data
        ArrayAdapter<SelectType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,selectTypes);
        selectType.setAdapter(adapter);

    }

    private void findViewByIdes(){
        doneButton = findViewById(R.id.select_button);
        edit_numOfQuestion = findViewById(R.id.select_question_amount_edit_box);
        selectCategory = findViewById(R.id.select_category_spinner);
        selectDifficulty = findViewById(R.id.select_difficulty_spinner);
        selectType = findViewById(R.id.select_type_spinner);
    }

    /**Check if device is connected to the internet or a Wi-Fi*/
    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_question_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ic_select_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
