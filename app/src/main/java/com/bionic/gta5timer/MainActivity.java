package com.bionic.gta5timer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    TextView tvMain;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Global.gta5date = 83;

        Calendar now = Calendar.getInstance();
        Global.currentday = now.get(Calendar.DAY_OF_YEAR);

        Global.second_now = now.get(Calendar.SECOND);
        Global.minute_now = now.get(Calendar.MINUTE);
        Global.hour_now = now.get(Calendar.HOUR);
        Global.day_now = now.get(Calendar.DAY_OF_MONTH);
        Global.month_now = now.get(Calendar.MONTH);
        Global.month_now = Global.month_now + 1;

        Global.day_left = Global.day_gta - Global.day_now;
        Global.month_left = Global.month_gta - Global.month_now;
        Global.hour_left = Global.hour_gta - Global.hour_now;
        Global.minute_left = Global.minute_gta - Global.minute_now;
        Global.second_left = Global.second_gta - Global.second_now;

        if (Global.minute_left < 0){
            Global.hour_left = Global.hour_left - 1;
            Global.minute_left = Global.minute_left + 60;
        }

        if (Global.second_left < 0){
            Global.minute_left = Global.minute_left - 1;
            Global.second_left = Global.second_left + 60;
        }

        /*
        if (sTime == 0L) {
            sTime = SystemClock.uptimeMillis();
            mHandler.removeCallbacks(TimeUpdater);
            // Добавляем Runnable-объект TimeUpdater в очередь
            // сообщений, объект должен быть запущен после задержки в 100 мс
            mHandler.postDelayed(TimeUpdater, 100);
        }
        */

        tvMain = (TextView) findViewById(R.id.tvMain);
        tvMain.setText("GTA 5 to left: " + "« " + Global.month_left + " month, " + Global.day_left
                + " days, " + Global.hour_left + " hours, " + Global.minute_left + " minutes, " + Global.second_left + " seconds " + "»");

        mHandler.removeCallbacks(TimeUpdater);


    }





    // Описание Runnable-объекта
    private Runnable TimeUpdater = new Runnable() {
        public void run() {
            Calendar now = Calendar.getInstance();
            Global.currentday = now.get(Calendar.DAY_OF_YEAR);

            Global.second_now = now.get(Calendar.SECOND);
            Global.minute_now = now.get(Calendar.MINUTE);
            Global.hour_now = now.get(Calendar.HOUR);
            Global.day_now = now.get(Calendar.DAY_OF_MONTH);
            Global.month_now = now.get(Calendar.MONTH);
            Global.month_now = Global.month_now + 1;

            Global.day_left = Global.day_gta - Global.day_now;
            Global.month_left = Global.month_gta - Global.month_now;
            Global.hour_left = Global.hour_gta - Global.hour_now;
            Global.minute_left = Global.minute_gta - Global.minute_now;
            Global.second_left = Global.second_gta - Global.second_now;

            if (Global.minute_left < 0){
                Global.hour_left = Global.hour_left - 1;
                Global.minute_left = Global.minute_left + 60;
            }

            if (Global.second_left < 0){
                Global.minute_left = Global.minute_left - 1;
                if (Global.minute_left == -1){
                    Global.minute_left = Global.minute_left + 1;
                }
                Global.second_left = Global.second_left + 60;
            }

            tvMain = (TextView) findViewById(R.id.tvMain);
            tvMain.setText("GTA 5 to left: " + "« " + Global.month_left + " month, " + Global.day_left
                    + " days, " + Global.hour_left + " hours, " + Global.minute_left + " minutes, " + Global.second_left + " seconds " + "»");
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
