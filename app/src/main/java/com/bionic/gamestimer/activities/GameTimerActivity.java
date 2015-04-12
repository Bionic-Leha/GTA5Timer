package com.bionic.gamestimer.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bionic.gamestimer.Global;
import com.bionic.gamestimer.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Leha on 17.02.2015.
 */
public class GameTimerActivity extends ActionBarActivity {

    ImageView ivScreen;
    ImageView ivIcon;
    TextView tvInfo;
    TextView tvTimer;
    TextView tvGameName;
    TextView tvGameDesc;
    Button btnWiki;
    Button btnShop;
    String info;
    private Handler mHandler = new Handler();

    public int colorPrimary = Color.BLACK;
    public int colorPrimaryDark = Color.BLACK;

    public static int counter = 1;

    private static final String PROPERTY_ID = "UA-42401203-8";
    static HashMap<MainActivity.TrackerName, Tracker> mTrackers = new HashMap<>();

    int x;
    int y;

    private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_timer);

        Log.d("Current API", String.valueOf(android.os.Build.VERSION.SDK_INT));

        ivScreen = (ImageView) findViewById(R.id.ivScreen);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvGameName = (TextView) findViewById(R.id.tvGameName);
        tvGameDesc = (TextView) findViewById(R.id.tvGameDesc);
        btnWiki = (Button) findViewById(R.id.btnWiki);
        btnShop = (Button) findViewById(R.id.btnShop);
        tvTimer.setText(getString(R.string.loading));
        tvGameName.setText(Global.game_name);
        tvGameDesc.setText(Global.game_desc);

        Calendar now = Calendar.getInstance();
        Global.currentday = now.get(Calendar.DAY_OF_YEAR);

        mHandler.removeCallbacks(TimeUpdater);

        Drawable screenshot = getResources().getDrawable(Global.current_screenshot);
        Drawable icon = getResources().getDrawable(Global.current_icon);
        ivScreen.setImageDrawable(screenshot);
        ivIcon.setImageDrawable(icon);

        Bitmap bitmap = ((BitmapDrawable) ivScreen.getDrawable()).getBitmap();
        int pixel = bitmap.getPixel(x, y);
        colorPrimary = pixel;
        colorPrimaryDark = darker(colorPrimary);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorPrimary));

        if (android.os.Build.VERSION.SDK_INT == 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        if (Global.current_screenshot != 0) {
            screenshot = getResources().getDrawable(Global.current_screenshot);
            ivScreen.setImageDrawable(screenshot);
        }

        GoogleAnalytics.getInstance(this).newTracker(PROPERTY_ID);
        GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        Tracker tracker = getTracker(MainActivity.TrackerName.APP_TRACKER);
        tracker.setScreenName(Global.game_name);
        tracker.send(new HitBuilders.AppViewBuilder().build());
        tracker.enableAdvertisingIdCollection(true);

        btnWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.wiki_link != null) {
                    Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(Global.wiki_link)));
                    startActivity(browserIntent1);
                } else {
                    ifNotAvailable();
                }
            }
        });

        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.shop_link != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(Global.shop_link)));
                    startActivity(browserIntent);
                } else {
                    ifNotAvailable();
                }
            }
        });

        /*
        Bitmap bitmap = ((BitmapDrawable) ivScreen.getDrawable()).getBitmap();
        Palette palette = Palette.generate(bitmap);
        CACHE.put(bitmap, palette);

        Palette p = PaletteTransformation.getPalette(bitmap);
        colorPrimary = p.getVibrantColor(Color.BLACK);
        colorPrimaryDark = darker(colorPrimary);
        if (colorPrimary == Color.BLACK || colorPrimaryDark == Color.BLACK) {
            colorPrimary = p.getMutedColor(Color.BLACK);
            colorPrimaryDark = darker(colorPrimary);
        }
        //toolbarBackground.setColor(colorPrimary);
        //statusBar.setBackgroundColor(colorPrimaryDark);
        */

        /*
        if (android.os.Build.VERSION.SDK_INT == 21){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        */

    }

    public void startTimer() {
        Calendar now = Calendar.getInstance();
        Global.currentday = now.get(Calendar.DAY_OF_YEAR);

        if (Global.out_day == 0) {
            tvInfo.setText(getString(R.string.no_date));
            tvTimer.setText(Global.game_out);
        } else if (Global.currentday > Global.out_day_full) {
            tvTimer.setText(getString(R.string.game_released));
        } else {
            mHandler.postDelayed(TimeUpdater, 1000);
        }
    }

    public void ifNotAvailable() {
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (GameTimerActivity.counter == 1) {
            Toast.makeText(GameTimerActivity.this, getString(R.string.not_available_1), Toast.LENGTH_SHORT).show();
            GameTimerActivity.counter++;
            vibrator.vibrate(90);
        } else if (GameTimerActivity.counter == 3) {
            Toast.makeText(GameTimerActivity.this, getString(R.string.not_available_3), Toast.LENGTH_SHORT).show();
            GameTimerActivity.counter++;
            vibrator.vibrate(90);
        } else if (GameTimerActivity.counter == 10) {
            Toast.makeText(GameTimerActivity.this, getString(R.string.not_available_10), Toast.LENGTH_SHORT).show();
            GameTimerActivity.counter++;
            vibrator.vibrate(90);
        } else if (GameTimerActivity.counter == 20) {
            Toast.makeText(GameTimerActivity.this, getString(R.string.not_available_20), Toast.LENGTH_SHORT).show();
            btnShop.setEnabled(false);
        } else {
            GameTimerActivity.counter++;
            vibrator.vibrate(90);
        }
    }

    public synchronized Tracker getTracker(MainActivity.TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            Tracker t = (trackerId == MainActivity.TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == MainActivity.TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
                    R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    public static int darker(int c) {

        int r = Color.red(c);
        int b = Color.blue(c);
        int g = Color.green(c);

        return Color.rgb((int) (r * .7), (int) (g * .7), (int) (b * .7));
    }

    // Описание Runnable-объекта
    public Runnable TimeUpdater = new Runnable() {
        public void run() {
            Calendar now = Calendar.getInstance();
            Global.currentday = now.get(Calendar.DAY_OF_YEAR);

            Global.second_now = now.get(Calendar.SECOND);
            Global.minute_now = now.get(Calendar.MINUTE);
            Global.hour_now = now.get(Calendar.HOUR);
            Global.day_now = now.get(Calendar.DAY_OF_MONTH);
            Global.month_now = now.get(Calendar.MONTH) + 1;
            Global.year_now = now.get(Calendar.YEAR);

            Global.days_month = now.getActualMaximum(Calendar.DAY_OF_MONTH);

            Global.second_left = Global.out_second - Global.second_now;
            Global.minute_left = Global.out_minute - Global.minute_now;
            Global.hour_left = Global.out_hour - Global.hour_now;
            Global.day_left = Global.out_day - Global.day_now;
            Global.month_left = Global.out_month - Global.month_now;

            if (Global.day_left <= 0) {
                Global.month_left -= 1;
                Global.day_left += Global.days_month;
            }

            if (Global.hour_left <= 0) {
                Global.day_left -= 1;
                Global.hour_left += 24;
            }

            if (Global.minute_left <= 0) {
                Global.hour_left -= 1;
                Global.minute_left += 60;
            }

            if (Global.second_left < 0) {
                Global.minute_left -= 1;
                Global.second_left += 60;
            }

            info = +Global.month_left + " " + getString(R.string.month) + ", "
                    + Global.day_left + " " + getString(R.string.days) + ", "
                    + Global.hour_left + " " + getString(R.string.hours) + ", "
                    + Global.minute_left + " " + getString(R.string.minutes) + ", "
                    + Global.second_left + " " + getString(R.string.seconds);

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
    protected void onDestroy() {
        // Удаляем Runnable-объект
        mHandler.removeCallbacks(TimeUpdater);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Добавляем Runnable-объект
        //mHandler.postDelayed(TimeUpdater, 1000);
        startTimer();
    }
}
