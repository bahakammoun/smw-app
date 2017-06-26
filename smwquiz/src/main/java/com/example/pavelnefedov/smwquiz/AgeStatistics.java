package com.example.pavelnefedov.smwquiz;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by Pavel Nefedov on 04.01.2017.
 * Activity to show the statistic of the users score compared to people who are the same age
 */

public class AgeStatistics extends AppCompatActivity{

    double percentage;
    String [] splitted = StatisticsActivity.responseAge.split(",");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.age_statistics_layout);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((this));
        double answerResults = preferences.getInt("rightAnswers", 0);
        double noQuestions = preferences.getInt("noQuestions", 0);

        if (preferences.getInt("noQuestions", 0) == 0) {
            percentage = 0;
        } else {
            percentage = (answerResults / noQuestions) * 100;
        }


        GraphView graph = (GraphView) findViewById(R.id.graphAge);
        graph.setTitle("Vergleich der Altersgruppen");
        graph.setTitleTextSize(50);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Altergruppen");
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, percentage),
                new DataPoint(1, Double.valueOf(splitted[0])*100),        //add data from int[] data
                new DataPoint(2, Double.valueOf(splitted[1])*100),
                new DataPoint(3, Double.valueOf(splitted[2])*100),
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
