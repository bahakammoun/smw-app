package com.example.pavelnefedov.smwquiz;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.http.RequestQueue;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by Pavel Nefedov on 04.01.2017.
 * Activity to show the statistic of the users score compared to people who are from the same gender
 */

public class GenderStatistics extends AppCompatActivity {


    double percentage;
    String[] splitted = StatisticsActivity.responseGender.split(",");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gender_statistics_layout);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((this));
        double answerResults = preferences.getInt("rightAnswers", 0);
        double noQuestions = preferences.getInt("noQuestions", 0);
        if (preferences.getInt("noQuestions", 0) == 0) {
            percentage = 0;
        } else {
            percentage = (answerResults / noQuestions) * 100;
        }

        GraphView graph = (GraphView) findViewById(R.id.graphGender);
        graph.setTitle("Vergleich der Geschlechter");
        graph.setTitleTextSize(50);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Geschlecht");
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, percentage),
                new DataPoint(1, Double.valueOf(splitted[0]) * 100),        //add data from int[] data
                new DataPoint(2, Double.valueOf(splitted[1]) * 100),
                new DataPoint(3, Double.valueOf(splitted[2]) * 100),
                new DataPoint(4, Double.valueOf(splitted[3])*100)
        });
        graph.addSeries(series);
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        series.setSpacing(20);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        //series.setValuesOnTopSize(50);

    }

}
