package com.bionic.gamestimer.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.bionic.gamestimer.Global;
import com.bionic.gamestimer.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.ListView;
import android.widget.SimpleAdapter;


public class MainActivity extends ActionBarActivity {

    TextView tvMain;
    Button btnGame;
    TextView tvTest;
    private Handler mHandler = new Handler();

    int itemnum = 0;

    private ArrayList<HashMap<String, Object>> gamesList;
    private static final String TITLE = "gamename"; // Название игры
    private static final String DESCRIPTION = "gamedesc"; // Описание игры
    private static final String PLATFORM = "gameplatf"; // Платформа
    private static final String DATE = "gamegate"; // Дата выхода
    private static final String ICON = "icon";  // Скриншот игры

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.games_list));

        Global.gta5date = 83;

        //mHandler.removeCallbacks(TimeUpdater);

        createList();

        /*
        // Дальше пойдет RecycleView...ну или пиздец
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewFragment fragment = new RecyclerViewFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        */

    }

    public void createList() {

        String PC = getString(R.string.pc);
        String PS3 = getString(R.string.ps3);
        String PS4 = getString(R.string.ps4);
        String X360 = getString(R.string.xbox_360);
        String XONE = getString(R.string.xbox_one);
        String ANDROID = getString(R.string.android);
        String IOS = getString(R.string.ios);
        String OUTDAY = getString(R.string.out_date);
        String comma = ", ";


        ListView lvGames = (ListView) findViewById(R.id.lvGames);

        // создаем массив списков
        gamesList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> games;

        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.gta5_name_1)); // Название
        games.put(DESCRIPTION, getString(R.string.gta5_desc_1)); // Описание
        games.put(PLATFORM, PC); // Платформа
        games.put(DATE, OUTDAY + ": " + getString(R.string.gta5_date_1)); // Дата выхода
        games.put(ICON, R.drawable.ic_gta); // Картинка
        gamesList.add(games);

        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.witcher_name2));
        games.put(DESCRIPTION, getString(R.string.witcher_desc2));
        games.put(PLATFORM, PC + comma + PS4 + comma + XONE);
        games.put(DATE, OUTDAY + ": " + getString(R.string.witcher_date2));
        games.put(ICON, R.drawable.ic_witcher);
        itemnum++;
        gamesList.add(games);

        SimpleAdapter adapter = new SimpleAdapter(this, gamesList,
                R.layout.list_item, new String[]{TITLE, DESCRIPTION, PLATFORM, DATE, ICON},
                new int[]{R.id.tvTitle, R.id.tvDesc, R.id.tvPlatforms, R.id.tvDate, R.id.img});

        lvGames.setAdapter(adapter);
    }

    /*
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
            Global.days_month = now.getActualMaximum(Calendar.DAY_OF_MONTH);
            Global.month_now = Global.month_now + 1;

            Global.day_left = Global.day_gta - Global.day_now;
            Global.month_left = Global.month_gta - Global.month_now;
            Global.hour_left = Global.hour_gta - Global.hour_now;
            Global.minute_left = Global.minute_gta - Global.minute_now;
            Global.second_left = Global.second_gta - Global.second_now;

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

            String info = getString(R.string.gtaleft_text) + ": « "
                    + Global.month_left + " " + getString(R.string.month) + ", "
                    + Global.day_left + " " + getString(R.string.days) + ", "
                    + Global.hour_left + " " + getString(R.string.hours) + ", "
                    + Global.minute_left + " " + getString(R.string.minutes) + ", "
                    + Global.second_left + " " + getString(R.string.seconds) + "»";

            tvMain = (TextView) findViewById(R.id.tvMain);
            tvMain.setText(info);

            mHandler.postDelayed(this, 1000);
        }
    };
    */

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
        if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, GameInfoActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        // Удаляем Runnable-объект
        //mHandler.removeCallbacks(TimeUpdater);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Добавляем Runnable-объект
        //mHandler.postDelayed(TimeUpdater, 1000);
    }

}
