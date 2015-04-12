package com.bionic.gamestimer.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bionic.gamestimer.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Leha on 25.02.2015.
 */
public class JsoupActivity extends ActionBarActivity {
    public TextView textView;
    public ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);
        setTitle("JSOUP - Test");
        textView = (TextView) findViewById (R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        MyTask mt = new MyTask();
        mt.execute();
    }

    public void jsoupStart(View v) {

    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        String title;//Тут храним значение заголовка сайта

        @Override
        protected Void doInBackground(Void... params) {
            progressBar.setVisibility(View.VISIBLE);
            Document doc = null;//Здесь хранится будет разобранный html документ
            try {
                doc = Jsoup.connect("http://bionic-leha.ru").get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа заголовок

            if (doc!=null)
                title = doc.title();
            else
                title = "Ошибка";

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            textView.setText(title);
        }
    }
}
