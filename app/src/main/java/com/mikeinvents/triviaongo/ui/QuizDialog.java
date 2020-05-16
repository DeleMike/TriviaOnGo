package com.mikeinvents.triviaongo.ui;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mikeinvents.triviaongo.R;

public class QuizDialog {
    private Dialog mDialog;

    public void showDialog(final Activity activity, String title, String message, boolean cancelable){
        mDialog = new Dialog(activity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(cancelable);
        mDialog.setContentView(R.layout.quiz_dialog);

        //set the layout
        TextView header = mDialog.findViewById(R.id.quiz_dialog_title);
        TextView theMessage = mDialog.findViewById(R.id.quiz_dialog_main_word);

        String str = "<b>"+title+"</b>";
        CharSequence formattedStr = Html.fromHtml(str);
        header.setText(formattedStr.toString());

        theMessage.setText(message);

        TextView button = mDialog.findViewById(R.id.quiz_dialog_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
}
