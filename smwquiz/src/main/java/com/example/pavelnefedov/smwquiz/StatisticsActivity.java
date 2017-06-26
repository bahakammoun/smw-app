package com.example.pavelnefedov.smwquiz;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Pavel Nefedov on 09.12.2016.
 * Activity which stores the main screen for all statistics and showing the average score of a user.
 */

public class StatisticsActivity extends AppCompatActivity {


    ProgressDialog pDialog;
    static String responseGender;
    static String responseAge;
    Button openGenderStatistics;
    Button openAgeStatistics;
    TextView resultOutput;
    double percentage;
    int[] data;

    //LineGraphSeries<DataPoint> series;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.average_score_layout);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((this));
        double answerResults = preferences.getInt("rightAnswers", 0);
        double noQuestions = preferences.getInt("noQuestions", 0);
        if (preferences.getInt("noQuestions", 0) == 0) {
            percentage = 0;
        } else {
            percentage = (answerResults / noQuestions) * 100;
        }

        resultOutput = (TextView) findViewById(R.id.averageScoreResult);
        resultOutput.setText(String.valueOf(percentage) + "%");

        openGenderStatistics = (Button) findViewById(R.id.sortByGender);
        openAgeStatistics = (Button) findViewById(R.id.sortByAge);

        openGenderStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetGender().execute();

            }
        });
        openAgeStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetAge().execute();
            }
        });
    }

    /*
inner class to split data from a String in JSON format
*/
    private class GetGender extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog-
            pDialog = new ProgressDialog(StatisticsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /*
        method to execute the process of getting data
         */
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall("http://46.101.100.92:8080/?do=stat,gender");

            Log.e(MainActivity.class.getSimpleName(),"Response from url:" + jsonStr);
            if(jsonStr != null) {

                responseGender = jsonStr;
            }

            return null;
        }
        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Intent intentLoadNewActivity = new Intent(StatisticsActivity.this, GenderStatistics.class);
            startActivity(intentLoadNewActivity);

        }
    }

    /*
inner class to split data from a String in JSON format
*/
    private class GetAge extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog-
            pDialog = new ProgressDialog(StatisticsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /*
        method to execute the process of getting data
         */
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall("http://46.101.100.92:8080/?do=stat,age");

            Log.e(MainActivity.class.getSimpleName(),"Response from url:" + jsonStr);
            if(jsonStr != null) {


                responseAge = jsonStr;
            }

            return null;
        }
        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Intent intentLoadNewActivity = new Intent(StatisticsActivity.this, AgeStatistics.class);
            startActivity(intentLoadNewActivity);

        }
    }
}