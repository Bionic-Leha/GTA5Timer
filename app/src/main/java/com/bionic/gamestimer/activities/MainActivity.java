package com.bionic.gamestimer.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.bionic.gamestimer.Global;
import com.bionic.gamestimer.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    TextView tvMain;
    Button btnGame;
    TextView tvTest;
    private Handler mHandler = new Handler();

    int itemnum = 0;

    AlertDialog.Builder ad;
    Context context;

    // идентификатор диалогового окна AlertDialog с кнопками
    private final int IDD_THREE_BUTTONS = 0;
    private final int FIRST_RUN = 1;

    private ArrayList<HashMap<String, Object>> gamesList;
    private static final String TITLE = "gamename"; // Название игры
    private static final String DESCRIPTION = "gamedesc"; // Описание игры
    private static final String PLATFORM = "gameplatf"; // Платформа
    private static final String DATE = "gamegate"; // Дата выхода
    private static final String TIMER = "game_timer"; // Таймер выхода
    private static final String ICON = "icon";  // Скриншот игры

    public static String info;

    String link_2_wiki = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.games_list));

        Global.gta5date = 83;

        SharedPreferences saves = getSharedPreferences(Global.PREFS_SAVES, MODE_PRIVATE);

        // Первый запуск!?
        if (saves.getBoolean("first_launch_1", true)) {
            showDialog(FIRST_RUN);
            saves.edit().putBoolean("first_launch_1", false).apply();
        }

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

        String timer_gta = null;
        String timer_witcher = null;


        ListView lvGames = (ListView) findViewById(R.id.lvGames);

        // создаем массив списков
        gamesList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> games;

        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.gta5_name_1)); // Название
        games.put(DESCRIPTION, getString(R.string.gta5_desc_1)); // Описание
        games.put(PLATFORM, PC); // Платформа
        games.put(DATE, OUTDAY + ": " + getString(R.string.gta5_date_1)); // Дата выхода
        //games.put(DATE, OUTDAY + ": " + timer_gta); // Дата выхода
        games.put(ICON, R.drawable.ic_gta); // Картинка
        gamesList.add(games);

        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.witcher_name2));
        games.put(DESCRIPTION, getString(R.string.witcher_desc2));
        games.put(PLATFORM, PC + comma + PS4 + comma + XONE);
        games.put(DATE, OUTDAY + ": " + getString(R.string.witcher_date2));
        //games.put(DATE, OUTDAY + ": " + timer_witcher); // Дата выхода
        games.put(ICON, R.drawable.ic_witcher);
        gamesList.add(games);

        SimpleAdapter adapter = new SimpleAdapter(this, gamesList,
                R.layout.list_item, new String[]{TITLE, DESCRIPTION, PLATFORM, DATE, ICON},
                new int[]{R.id.tvTitle, R.id.tvDesc, R.id.tvPlatforms, R.id.tvDate, R.id.img});

        lvGames.setAdapter(adapter);

        lvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                switch (position) {
                    case 0:
                        link_2_wiki = "https://ru.wikipedia.org/wiki/Grand_Theft_Auto_V";
                        Global.day_game = 24;
                        Global.month_game = 5;
                        mHandler.removeCallbacks(TimeUpdater);
                        mHandler.postDelayed(TimeUpdater, 1000);
                        showDialog(IDD_THREE_BUTTONS);
                        break;
                    case 1:
                        /*
                        Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.wikipedia.org/wiki/The_Witcher_3:_Wild_Hunt"));
                        startActivity(browserIntent1);
                        */
                        Global.current_screenshot = R.drawable.gtavscr;
                        startActivity(new Intent(MainActivity.this, GameTimerActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case FIRST_RUN:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.imp_info));
                builder.setMessage(getString(R.string.first_run))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                return builder.create();

            case IDD_THREE_BUTTONS:
                AlertDialog.Builder builder_2 = new AlertDialog.Builder(this);
                builder_2.setTitle(getString(R.string.detail_info));
                builder_2.setMessage(MainActivity.info)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        mHandler.removeCallbacks(TimeUpdater);
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("Wiki",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_2_wiki));
                                        startActivity(browserIntent);
                                        dialog.cancel();
                                    }
                                });

                return builder_2.create();
            default:
                return null;
        }
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

            MainActivity.info = getString(R.string.game_left) + ": « "
                    + Global.month_left + " " + getString(R.string.month) + ", "
                    + Global.day_left + " " + getString(R.string.days) + ", "
                    + Global.hour_left + " " + getString(R.string.hours) + ", "
                    + Global.minute_left + " " + getString(R.string.minutes) + ", "
                    + Global.second_left + " " + getString(R.string.seconds) + "»";

            //tvMain = (TextView) findViewById(R.id.tvMain);
            //tvMain.setText(info);

            mHandler.postDelayed(this, 1000);
        }
    };

    public void updGtaTimer(){

    }

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
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }

        if (id == R.id.action_info) {
            showDialog(FIRST_RUN);
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
