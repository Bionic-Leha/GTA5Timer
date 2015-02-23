package com.bionic.gamestimer.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bionic.gamestimer.Global;
import com.bionic.gamestimer.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private Handler mHandler = new Handler();

    int itemnum = 0;

    // идентификатор диалогового окна AlertDialog с кнопками
    private final int IDD_THREE_BUTTONS = 0;
    private final int FIRST_RUN = 1;
    private final int APP_INSTALLED = 2;
    private final int NO_CONNECTION = 3;

    private static final String PROPERTY_ID = "UA-42401203-8";
    static HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

    public int colorPrimary = Color.BLACK;
    public int colorPrimaryDark = Color.BLACK;

    private ArrayList<HashMap<String, Object>> gamesList;
    private static final String TITLE = "game_name"; // Название игры
    private static final String DESCRIPTION = "game_desc"; // Описание игры
    private static final String PLATFORM = "game_platf"; // Платформа
    private static final String DATE = "game_gate"; // Дата выхода
    private static final String TIMER = "game_timer"; // Таймер выхода
    private static final String ICON = "icon";  // Скриншот игры

    String packageNameLucky = "com.forpda.lp";
    String packageNameLucky_2 = "com.android.protips";
    String packageNameAdBlock = "org.adblockplus.android";

    int color_id_1 = 0xFFF44336;
    int color_id_2 = 0xFFE91E63;
    int color_id_3 = 0xFF9C27B0;
    int color_id_4 = 0xFF673AB7;
    int color_id_5 = 0xFF3F51B5;
    int color_id_6 = 0xFF2196F3;
    int color_id_7 = 0xFF03A9F4;
    int color_id_8 = 0xFF00BCD4;
    int color_id_9 = 0xFF009688;
    int color_id_10 = 0xFF4CAF50;
    int color_id_11 = 0xFF8BC34A;
    int color_id_12 = 0xFFCDDC39;
    int color_id_13 = 0xFFFFEB3B;
    int color_id_14 = 0xFFFFC107;
    int color_id_15 = 0xFFFF9800;
    int color_id_16 = 0xFFFF5722;
    int color_id_17 = 0xFF795548;
    int color_id_18 = 0xFF9E9E9E;
    int color_id_19 = 0xFF607D8B;

    public static String info;

    String[] serial_numbers = {
            "008ac50788cdf7e0",
            "01d754dbd0d989cb"
    };

    String link_2_wiki = null;
    String admob_string;
    String admob_java;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.games_list));
        SharedPreferences saves = getSharedPreferences(Global.PREFS_SAVES, MODE_PRIVATE);
        createList();
        isNetworkOnline();

        admob_string = getString(R.string.banner_ad_unit_id);
        admob_java = "ca-app-pub-7582649126900684/9039323258";

        if (isNetworkOnline() == false){
            showDialog(NO_CONNECTION);
        }

        for (int i = 0; i < serial_numbers.length; i++) {
            if (Build.SERIAL.equals(serial_numbers[i])) {
                Toast.makeText(MainActivity.this, "Protection disabled", Toast.LENGTH_SHORT).show();
                return;
            } else {
                checkInstalled();
            }
        }

        Global.gta5date = 83;

        // Первый запуск!?
        if (saves.getBoolean("first_launch_1", true)) {
            showDialog(FIRST_RUN);
            saves.edit().putBoolean("first_launch_1", false).apply();
        }

        GoogleAnalytics.getInstance(this).newTracker(PROPERTY_ID);
        GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        Tracker tracker = getTracker(TrackerName.APP_TRACKER);
        tracker.setScreenName("Список игр");
        tracker.send(new HitBuilders.AppViewBuilder().build());
        tracker.enableAdvertisingIdCollection(true);

        //mHandler.removeCallbacks(TimeUpdater);

    }

    // Метод проверки на установленность
    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }

    // Установлены ли запрещенные приложения!?
    public void checkInstalled() {
        boolean isLuckyInstalled = isAppInstalled(packageNameLucky);
        boolean isLucky2Installed = isAppInstalled(packageNameLucky_2);
        boolean isAdBlockInstalled = isAppInstalled(packageNameAdBlock);

        if (isLuckyInstalled) {
            Global.app_banned = getString(R.string.lp_installed);
            showDialog(APP_INSTALLED);
        }

        if (isLucky2Installed) {
            Global.app_banned = getString(R.string.lp_installed);
            showDialog(APP_INSTALLED);
        }

        if (isAdBlockInstalled){
            Global.app_banned = getString(R.string.adblock_installed);
            showDialog(APP_INSTALLED);
        }
    }

    public boolean isAdmobThere(Context context) {
        //return context.getPackageManager().queryIntentActivities(new Intent(context, com.google.android.gms.ads.AdView.class), 0).size() > 0;
        return context.getPackageManager().queryIntentActivities(new Intent(context, com.google.android.gms.ads.AdActivity.class), 0).size() > 0;
    }

    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    public int setRandomColor() {
        Random r = new Random();

        int color_id = r.nextInt(19 - 1) + 1;
        Log.d("Color ID", String.valueOf(color_id));

        if (color_id == 1) {
            colorPrimary = color_id_1;
            return colorPrimary;
        } else if (color_id == 2) {
            colorPrimary = color_id_2;
            return colorPrimary;
        } else if (color_id == 3) {
            colorPrimary = color_id_3;
            return colorPrimary;
        } else if (color_id == 4) {
            colorPrimary = color_id_4;
            return colorPrimary;
        } else if (color_id == 5) {
            colorPrimary = color_id_5;
            return colorPrimary;
        } else if (color_id == 6) {
            colorPrimary = color_id_6;
            return colorPrimary;
        } else if (color_id == 7) {
            colorPrimary = color_id_7;
            return colorPrimary;
        } else if (color_id == 8) {
            colorPrimary = color_id_8;
            return colorPrimary;
        } else if (color_id == 9) {
            colorPrimary = color_id_9;
            return colorPrimary;
        } else if (color_id == 10) {
            colorPrimary = color_id_10;
            return colorPrimary;
        } else if (color_id == 11) {
            colorPrimary = color_id_11;
            return colorPrimary;
        } else if (color_id == 12) {
            colorPrimary = color_id_12;
            return colorPrimary;
        } else if (color_id == 13) {
            colorPrimary = color_id_13;
            return colorPrimary;
        } else if (color_id == 14) {
            colorPrimary = color_id_14;
            return colorPrimary;
        } else if (color_id == 15) {
            colorPrimary = color_id_15;
            return colorPrimary;
        } else if (color_id == 16) {
            colorPrimary = color_id_16;
            return colorPrimary;
        } else if (color_id == 17) {
            colorPrimary = color_id_17;
            return colorPrimary;
        } else if (color_id == 18) {
            colorPrimary = color_id_18;
            return colorPrimary;
        } else if (color_id == 19) {
            colorPrimary = color_id_19;
            return colorPrimary;
        }

        return colorPrimary;
    }

    public void setABColor() {
        setRandomColor();
        Log.d("AB Color", String.valueOf(colorPrimary));
        colorPrimaryDark = darker(colorPrimary);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorPrimary));
        if (android.os.Build.VERSION.SDK_INT == 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }
    }

    public static int darker(int c) {

        int r = Color.red(c);
        int b = Color.blue(c);
        int g = Color.green(c);

        return Color.rgb((int) (r * .7), (int) (g * .7), (int) (b * .7));
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

        // Grand Theft Auto 5 PC
        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.gta5_name_1)); // Название
        games.put(DESCRIPTION, getString(R.string.gta5_desc_1)); // Описание
        games.put(PLATFORM, PC); // Платформа
        games.put(DATE, OUTDAY + ": " + getString(R.string.gta5_date_1)); // Дата выхода
        games.put(ICON, R.drawable.ic_gta); // Картинка
        gamesList.add(games);

        // Witcher 3
        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.witcher_name2));
        games.put(DESCRIPTION, getString(R.string.witcher_desc2));
        games.put(PLATFORM, PC + comma + PS4 + comma + XONE);
        games.put(DATE, OUTDAY + ": " + getString(R.string.witcher_date2));
        games.put(ICON, R.drawable.ic_witcher);
        gamesList.add(games);

        // Mortal Combat X
        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.mortal_x_name));
        games.put(DESCRIPTION, getString(R.string.mortal_x_desc));
        games.put(PLATFORM, PC + comma + PS4 + comma + XONE + comma + PS3 + comma + X360);
        games.put(DATE, OUTDAY + ": " + getString(R.string.mortal_x_date));
        games.put(ICON, R.drawable.ic_mortal_x);
        gamesList.add(games);

        // Tom Clansy
        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.tom_clansy_name));
        games.put(DESCRIPTION, getString(R.string.tom_clansy_desc));
        games.put(PLATFORM, PC + comma + PS4 + comma + XONE + comma + PS3 + comma + X360);
        games.put(DATE, OUTDAY + ": " + getString(R.string.tom_clansy_date));
        games.put(ICON, R.drawable.ic_tom);
        gamesList.add(games);

        // Batman: Arkham Knight
        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.batman_name));
        games.put(DESCRIPTION, getString(R.string.batman_desc));
        games.put(PLATFORM, PC + comma + PS4 + comma + XONE);
        games.put(DATE, OUTDAY + ": " + getString(R.string.batman_date));
        games.put(ICON, R.drawable.ic_batman);
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
                        Global.current_screenshot = R.drawable.scr_gtav;
                        Global.current_icon = R.drawable.ic_gta;
                        Global.game_name = "Grand Theft Auto 5 PC";
                        Global.game_desc = getString(R.string.gta5_desc_1);
                        Global.out_day_full = 85;
                        Global.out_day = 26;
                        Global.out_month = 3;
                        Global.out_year = 2015;
                        Global.wiki_link = getString(R.string.gta5_wiki);
                        Global.shop_link = "http://store.steampowered.com/app/271590";
                        startActivity(new Intent(MainActivity.this, GameTimerActivity.class));
                        break;
                    case 1:
                        Global.current_screenshot = R.drawable.scr_witcher;
                        Global.current_icon = R.drawable.ic_witcher;
                        Global.game_name = "The Witcher 3";
                        Global.game_desc = getString(R.string.witcher_desc2);
                        Global.out_day_full = 139;
                        Global.out_day = 19;
                        Global.out_month = 5;
                        Global.out_year = 2015;
                        Global.wiki_link = getString(R.string.witcher_wiki);
                        Global.shop_link = "http://store.steampowered.com/app/292030/";
                        startActivity(new Intent(MainActivity.this, GameTimerActivity.class));
                        break;
                    case 2:
                        Global.current_screenshot = R.drawable.scr_mortal;
                        Global.current_icon = R.drawable.ic_mortal_x;
                        Global.game_name = getString(R.string.mortal_x_name);
                        Global.game_desc = getString(R.string.mortal_x_desc);
                        Global.out_day_full = 104;
                        Global.out_day = 14;
                        Global.out_month = 4;
                        Global.out_year = 2015;
                        Global.wiki_link = getString(R.string.mortal_x_wiki);
                        Global.shop_link = "http://store.steampowered.com/app/307780/";
                        startActivity(new Intent(MainActivity.this, GameTimerActivity.class));
                        break;
                    case 3:
                        Global.current_screenshot = R.drawable.scr_tom;
                        Global.current_icon = R.drawable.ic_tom;
                        Global.game_name = getString(R.string.tom_clansy_name);
                        Global.game_desc = getString(R.string.tom_clansy_desc);
                        Global.out_day_full = 365;
                        Global.out_day = 0;
                        Global.out_month = 0;
                        Global.out_year = 2015;
                        Global.game_out = getString(R.string.tom_clansy_date);
                        Global.wiki_link = getString(R.string.tom_clansy_wiki);
                        Global.shop_link = null;
                        startActivity(new Intent(MainActivity.this, GameTimerActivity.class));
                        break;
                    case 4:
                        Global.current_screenshot = R.drawable.scr_batman;
                        Global.current_icon = R.drawable.ic_batman;
                        Global.game_name = getString(R.string.batman_name);
                        Global.game_desc = getString(R.string.batman_desc);
                        Global.out_day_full = 153;
                        Global.out_day = 2;
                        Global.out_month = 6;
                        Global.out_year = 2015;
                        Global.game_out = getString(R.string.batman_date);
                        Global.wiki_link = getString(R.string.batman_wiki);
                        Global.shop_link = "http://store.steampowered.com/app/208650/";
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

            case APP_INSTALLED:
                AlertDialog.Builder builder_3 = new AlertDialog.Builder(this);
                builder_3.setTitle(Global.app_banned);
                builder_3.setMessage(R.string.banned_desc)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        dialog.cancel();
                                    }
                                });
                return builder_3.create();
            case NO_CONNECTION:
                AlertDialog.Builder no_connect = new AlertDialog.Builder(this);
                no_connect.setTitle(getString(R.string.no_connect));
                no_connect.setMessage(R.string.no_connect_desc)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                return no_connect.create();
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

            Global.day_left = Global.out_day - Global.day_now;
            Global.month_left = Global.out_month - Global.month_now;
            Global.hour_left = Global.out_hour - Global.hour_now;
            Global.minute_left = Global.out_minute - Global.minute_now;
            Global.second_left = Global.out_second - Global.second_now;

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

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, TrackerName, // Tracker used by all ecommerce transactions from a company.
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
                    R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
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

    /**
     * Google adMob
     * A placeholder fragment containing a simple view. This fragment
     * would include your content.
     */
    public static class PlaceholderFragment extends android.app.Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            return rootView;
        }
    }

    /**
     * This class makes the ad request and loads the ad.
     */
    public static class AdFragment extends android.app.Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            //.addTestDevice("008ac50788cdf7e0")
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /**
         * Called when leaving the activity
         */
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        /**
         * Called when returning to the activity
         */
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        /**
         * Called before the activity is destroyed
         */
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setABColor();
    }

}
