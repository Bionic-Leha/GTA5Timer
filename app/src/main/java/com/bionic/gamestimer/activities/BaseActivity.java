/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bionic.gamestimer.activities;

import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bionic.gamestimer.R;
import com.bionic.gamestimer.adapters.SimpleHeaderRecyclerAdapter;
import com.bionic.gamestimer.adapters.SimpleRecyclerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseActivity extends ActionBarActivity {
    private static final int NUM_OF_ITEMS = 100;
    private static final int NUM_IDS = 10;
    private static final int NUM_OF_ITEMS_FEW = 3;
    private static final int NUM_OF_ITEMS_FEW_2 = 3;

    public static String creator;
    public static String publisher;
    public static String platform;
    public static String release_date;
    public static String announce_date;
    public static String genre;
    public static String game_mode;
    public static String site;
    public static String icon;

    private ArrayList<HashMap<String, Object>> infoList;
    private static final String TITLE = "name game";
    private static final String DESCRIPTION = "description of game";
    private static final String CREATOR = "creator name";
    private static final String PUBLISHER = "publisher studio";
    private static final String PLATFORM = "game platform";
    private static final String RELEASE_DATE = "date of release";
    private static final String ANNOUNCE_DATE = "date of announce";
    private static final String GENRE = "game genre";
    private static final String GAME_MODE = "game mode";
    private static final String SITE = "official site";
    private static final String ICON = "screenshot of game";

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }


    public static ArrayList<String> getDummyData() {
        return getDummyData(NUM_OF_ITEMS);
    }

    public void setListView(ListView listView) {
        // создаем массив списков
        infoList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> games;

        games = new HashMap<String, Object>();
        games.put(TITLE, getString(R.string.gta5_name_1)); // Название
        games.put(DESCRIPTION, getString(R.string.gta5_desc_1)); // Описание
        games.put(PLATFORM, "TEEEST"); // Платформа
        games.put(ICON, R.drawable.ic_gta); // Картинка
        infoList.add(games);

        SimpleAdapter adapter = new SimpleAdapter(this, infoList,
                R.layout.list_item, new String[]{TITLE, DESCRIPTION, PLATFORM, ICON},
                new int[]{R.id.tvTitle, R.id.tvDesc, R.id.tvPlatforms, R.id.tvDate, R.id.img});

        listView.setAdapter(adapter);
    }

    public static ArrayList<HashMap<String, Object>> getListData(){
        return getListData(NUM_IDS);
    }

    // NEW

    public static ArrayList<String> getDummyData(int num) {
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 1; i <= num; i++) {
            items.add("Item " + i);
        }
        return items;
    }

    public static ArrayList<HashMap<String, Object>> getListData(int num){
        ArrayList<HashMap<String, Object>> infoList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> games;

        games = new HashMap<String, Object>();
        games.put(TITLE, "TEST"); // Название
        games.put(DESCRIPTION, "TEST 2"); // Описание
        games.put(PLATFORM, "TEST 3"); // Платформа
        games.put(ICON, R.drawable.ic_gta); // Картинка
        infoList.add(games);

        return infoList;
    }

    // separator

    protected void setDummyData(ListView listView) {
        setDummyData(listView, NUM_OF_ITEMS);
    }

    protected void setListData(ListView listView){
        setListData(listView, NUM_IDS);
    }

    // separator

    protected void setDummyDataFew(ListView listView) {
        setDummyData(listView, NUM_OF_ITEMS_FEW);
    }

    protected void setListDataFew(ListView listView) {
        setDummyData(listView, NUM_OF_ITEMS_FEW);
    }


    // separator

    protected void setDummyData(ListView listView, int num) {
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getDummyData(num)));
    }

    public void setListData(ListView listView, int num) {
        SimpleAdapter adapter = new SimpleAdapter(this, infoList,
                R.layout.list_item, new String[]{TITLE, DESCRIPTION, PLATFORM, ICON},
                new int[]{R.id.tvTitle, R.id.tvDesc, R.id.tvPlatforms, R.id.tvDate, R.id.img});

        listView.setAdapter(adapter);
    }

    protected void setDummyDataWithHeader(ListView listView, int headerHeight) {
        setDummyDataWithHeader(listView, headerHeight, NUM_OF_ITEMS);
    }

    protected void setDummyDataWithHeader(ListView listView, int headerHeight, int num) {
        View headerView = new View(this);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
        headerView.setMinimumHeight(headerHeight);
        // This is required to disable header's list selector effect
        headerView.setClickable(true);
        setDummyDataWithHeader(listView, headerView, num);
    }

    protected void setDummyDataWithHeader(ListView listView, View headerView, int num) {
        listView.addHeaderView(headerView);
        setDummyData(listView, num);
    }

    protected void setDummyData(GridView gridView) {
        gridView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getDummyData()));
    }

    protected void setDummyData(RecyclerView recyclerView) {
        setDummyData(recyclerView, NUM_OF_ITEMS);
    }

    protected void setDummyDataFew(RecyclerView recyclerView) {
        setDummyData(recyclerView, NUM_OF_ITEMS_FEW);
    }

    protected void setDummyData(RecyclerView recyclerView, int num) {
        recyclerView.setAdapter(new SimpleRecyclerAdapter(this, getDummyData(num)));
    }

    protected void setDummyDataWithHeader(RecyclerView recyclerView, int headerHeight) {
        View headerView = new View(this);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, headerHeight));
        headerView.setMinimumHeight(headerHeight);
        // This is required to disable header's list selector effect
        headerView.setClickable(true);
        setDummyDataWithHeader(recyclerView, headerView);
    }

    protected void setDummyDataWithHeader(RecyclerView recyclerView, View headerView) {
        recyclerView.setAdapter(new SimpleHeaderRecyclerAdapter(this, getDummyData(), headerView));
    }
}
