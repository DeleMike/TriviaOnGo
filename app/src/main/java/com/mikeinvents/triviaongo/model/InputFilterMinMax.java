package com.mikeinvents.triviaongo.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.Spanned;

import com.mikeinvents.triviaongo.ui.QuizDialog;

public class InputFilterMinMax implements InputFilter {
    private int min, max;
    private Context context;

    public InputFilterMinMax(Context context, int min, int max){
        this.context = context;
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(Context context, String min, String max){
        this.context = context;
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if(isInRange(min,max,input)){
                return null;
            }else{
                showDialog();
            }
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c){
        return b > a ? c >= a && c <= b : c >= b && c<=a;
    }

    private void showDialog(){
        QuizDialog dialog = new QuizDialog();
        dialog.showDialog((Activity) context,"Note",
                "You are permitted to only enter a value between 1 and 50",true);

    }
}
