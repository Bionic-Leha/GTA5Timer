package com.bionic.gamestimer.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bionic.gamestimer.Global;
import com.bionic.gamestimer.R;

import java.util.Calendar;

/**
 * Created by Leha on 17.02.2015.
 */
public class GameTimerActivity extends ActionBarActivity {

    ImageView ivScreen;
    TextView tvTimer;
    String info;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_timer);

        ivScreen = (ImageView) findViewById(R.id.ivScreen);
        Drawable screenshot= getResources().getDrawable(Global.current_screenshot);
        ivScreen.setBackgroundDrawable(screenshot);

        mHandler.removeCallbacks(TimeUpdater);

    }

    // Описание Runnable-объекта
    public Runnable TimeUpdater = new Runnable() {
        public void run() {
            Log.d("New", " ok");
            Calendar now = Calendar.getInstance();
            Global.currentday = now.get(Calendar.DAY_OF_YEAR);

            Global.second_now = now.get(Calendar.SECOND);
            Global.minute_now = now.get(Calendar.MINUTE);
            Global.hour_now = now.get(Calendar.HOUR);
            Global.day_now = now.get(Calendar.DAY_OF_MONTH);
            Global.month_now = now.get(Calendar.MONTH);
            Global.days_month = now.getActualMaximum(Calendar.DAY_OF_MONTH);
            Global.month_now = Global.month_now + 1;

            Global.hour_game = 12;
            Global.minute_game = 0;
            Global.second_game = 0;

            Global.day_left = Global.day_game - Global.day_now;
            Global.month_left = Global.month_game - Global.month_now;
            Global.hour_left = Global.hour_game - Global.hour_now;
            Global.minute_left = Global.minute_game - Global.minute_now;
            Global.second_left = Global.second_game - Global.second_now;

            if (Global.day_left <= 0) {
                Global.month_left -= 1;
                Global.day_left += Global.days_month;
            }

            if (Global.minute_left <= 0) {
                Global.hour_left -= 1;
                Global.minute_left += 60;
            }

            if (Global.second_left < 0) {
                Global.minute_left -= 1;
                Global.second_left += 60;
            }

            info = getString(R.string.game_left) + ": « "
                    + Global.month_left + " " + getString(R.string.month) + ", "
                    + Global.day_left + " " + getString(R.string.days) + ", "
                    + Global.hour_left + " " + getString(R.string.hours) + ", "
                    + Global.minute_left + " " + getString(R.string.minutes) + ", "
                    + Global.second_left + " " + getString(R.string.seconds) + "»";

            tvTimer = (TextView) findViewById(R.id.tvTimer);
            tvTimer.setText(info);

            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onPause() {
        // Удаляем Runnable-объект
        mHandler.removeCallbacks(TimeUpdater);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Добавляем Runnable-объект
        mHandler.postDelayed(TimeUpdater, 1000);
    }
}