package com.bionic.gamestimer.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bionic.gamestimer.Global;
import com.bionic.gamestimer.R;
import com.bionic.gamestimer.fragments.RecyclerViewFragment;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    TextView tvMain;
    Button btnGame;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGame = (Button) findViewById(R.id.btnGame);
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        Global.gta5date = 83;

        mHandler.removeCallbacks(TimeUpdater);

        // Дальше пойдет RecycleView...ну или пиздец
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

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

            if (Global.minute_left <= 0) {
                Global.hour_left = Global.hour_left - 1;
                Global.minute_left = Global.minute_left + 60;
            }

            if (Global.second_left < 0) {
                Global.minute_left = Global.minute_left - 1;
                Global.second_left = Global.second_left + 60;
            }

            String info = getString(R.string.gtaleft_text) + ": « "
                    + Global.month_left + " " + getString(R.string.month) + ", "
                    + Global.day_left + " " + getString(R.string.days) + ", "
                    + Global.hour_left + " " + getString(R.string.hours) + ", "
                    + Global.minute_left + " " + getString(R.string.minutes) + ", "
                    + Global.second_left + " " + getString(R.string.seconds) + "»" ;

            tvMain = (TextView) findViewById(R.id.tvMain);
            tvMain.setText(info);
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
