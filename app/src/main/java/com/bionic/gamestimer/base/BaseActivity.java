package com.bionic.gamestimer.base;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Leha on 30.01.2015.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        setSupportActionBar(this.toolbar);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

}
