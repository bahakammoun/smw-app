package com.example.pavelnefedov.smwquiz;

/**
 * Created by Pavel Nefedov on 23.11.2016.
 * Main Class that runs the basic activity of the class. Also includes an Options menu to  ge into the Seti´ting and the FAQ.
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    int rigthAnswer;
    //button to get to the starting page of the quiz
    Button quizStart;
    //button to get into the statistics menu
    Button showStatistics;

    /*
    basic on create method of the Main Class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButtonListener to start a quiz
        quizStart = (Button) findViewById(R.id.quizButton);
        quizStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(MainActivity.this, ChooseYourWikiActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });

        showStatistics = (Button) findViewById(R.id.statisticButton);
        showStatistics.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intentLoadNewActivity);
            }
        });

    }
    //init Options Menu in this View
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // functionality of the Options Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.questionSettings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        } else if (id == R.id.FAQ) {

            startActivity(new Intent(this, FAQActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

