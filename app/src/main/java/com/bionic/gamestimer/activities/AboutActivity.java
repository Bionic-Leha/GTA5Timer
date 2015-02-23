package com.bionic.gamestimer.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.bionic.gamestimer.BuildConfig;
import com.bionic.gamestimer.R;

/**
 * Created by Leha on 04.02.2015.
 */
public class AboutActivity extends ActionBarActivity {
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText(getString(R.string.version) + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");
    }
}
