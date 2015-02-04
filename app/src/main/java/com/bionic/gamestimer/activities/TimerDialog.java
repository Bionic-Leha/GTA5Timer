package com.bionic.gamestimer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.bionic.gamestimer.R;

/**
 * Created by Leha on 04.02.2015.
 */
public class TimerDialog extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(TimerDialog.this);
        builder.setTitle("Важное сообщение!")
                .setMessage("Покормите кота!")
                .setIcon(R.drawable.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("ОК, иду на кухню",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
