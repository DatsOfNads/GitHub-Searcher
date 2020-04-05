package com.company.tochka.view;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.company.tochka.R;

public class CustomAlertDialog extends AlertDialog {

    private Button button;

    private Context context;

    CustomAlertDialog(Context context, int buttonTitle) {
        super(context);

        this.context = context;

        @SuppressLint("InflateParams")
        View customAlertDialogView = getLayoutInflater().inflate(R.layout.fragment_custom_alert_dialog,null);

        button = customAlertDialogView.findViewById(R.id.button);
        button.setText(context.getResources().getString(buttonTitle));

        super.setCancelable(false);
        super.setView(customAlertDialogView);
    }

    public void setTitle(int id){
        super.setTitle(context.getResources().getString(id));
    }

    void setMessage(int id){
        super.setMessage(context.getResources().getString(id));
    }

    void setButtonClickListener(View.OnClickListener onClickListener){
        button.setOnClickListener(onClickListener);
    }
}