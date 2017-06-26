package com.example.pavelnefedov.smwquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pavel Nefedov on 11.12.2016.
 * Activity after the quiz is finished. Show results of the current quiz. User can also report the quiz and give feedback.
 */

public class QuizResultsActivity extends AppCompatActivity {

    String resultAnswers;// = String.valueOf(QuizActivity.finalRightAnswers);
    Button readyButton;
    String resultInput;
    TextView result;
    String resultUrl;
    EditText feedback;
    Button done;
    String feedbackTextBack;
    String ageCode;
    String genderCode;

    private ProgressDialog pDialog;


    /*
    basic on Create method, that creates the overlay
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_results_layout);

        Bundle korb = getIntent().getExtras();
        //resultAnswers = String.valueOf(korb.getInt("finalRightAnswers"));
        resultAnswers = String.valueOf(QuizActivityNew.finalRightAnswers);


        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefNumberQuestionsKey = getString(R.string.preference_number_of_questions_key);
        String prefNumberQuestionsDefault = getString(R.string.preference_number_of_questions_default);
        String numberOfQuestionsChoosen = sPrefs.getString(prefNumberQuestionsKey, prefNumberQuestionsDefault);

        String prefNumberResponsesKey = getString(R.string.preference_number_of_response_possibilities_key);
        String prefNumberResponseDefault = getString(R.string.preference_number_of_response_possibilities_default);
        String numberOfResponsesChoosen = sPrefs.getString(prefNumberResponsesKey,prefNumberResponseDefault);

        String prefGenderKey = getString(R.string.preference_gender_key);
        String prefGenderDefault = "keine Angabe";
        String genderChoosen = sPrefs.getString(prefGenderKey, prefGenderDefault);

        String prefAgeKey = getString(R.string.preference_age_key);
        String prefAgeDefault = "20-30";
        String ageChoosen = sPrefs.getString(prefAgeKey, prefAgeDefault);

        String wikiChoose = "currentWiki";
        String prefWikiDefault = "error1";
        String wikiUrl = sPrefs.getString(wikiChoose, prefWikiDefault);

        if (ageChoosen.equals("0-20")) {
            ageCode = "1";
        } else if (ageChoosen.equals("21-30")) {
            ageCode = "2";
        } else if (ageChoosen.equals("31-40")) {
            ageCode = "3";
        } else if(ageChoosen.equals("41+")) {
            ageCode = "4";
        }

        if (genderChoosen.equals("männlich")) {
            genderCode = "1";
        } else if (genderChoosen.equals("weiblich")) {
            genderCode = "2";
        } else if (genderChoosen.equals("sonstiges")){
            genderCode = "3";
        } else {
            genderCode = "4";
        }
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putInt("rightAnswers", sPrefs.getInt("rightAnswers", 0) + Integer.valueOf(resultAnswers)); //anzahl aller richtigen Antworten lifetime
        editor.putInt("noQuestions", sPrefs.getInt("noQuestions", 0) + Integer.valueOf(numberOfQuestionsChoosen)); // Anzahl aller Fragen lifetime
        editor.apply();


        resultUrl = "http://46.101.100.92:8080/?do=statSet," + numberOfQuestionsChoosen + "," + resultAnswers + "," + numberOfResponsesChoosen + "," + ageCode + "," + genderCode + ","  + wikiUrl + ",";
        resultInput = "Du hast " + resultAnswers +" von " + numberOfQuestionsChoosen + " Fragen richitg beantwortet!";

        result = ((TextView)findViewById(R.id.results));
        result.setText(resultInput);
        feedback = (EditText) findViewById(R.id.feedbackText);

        readyButton = (Button) findViewById(R.id.confirm);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackTextBack = feedback.getText().toString();
                resultUrl = resultUrl + encodeString(feedbackTextBack);

                new SetStats().execute();

            }
        });
}

    private class SetStats extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog-
            pDialog = new ProgressDialog(QuizResultsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /*
        method make a service call in backgorund
         */
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            System.out.println(resultUrl);
            String jsonStr = sh.makeServiceCall(resultUrl);

            return null;
        }
        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Intent intentLoadNewActivity = new Intent(QuizResultsActivity.this, MainActivity.class);
            startActivity(intentLoadNewActivity);

        }
    }

    public String encodeString(String s) {
        String stringWithoutComma ="";
        if (s.contains(",")) {
            String arrayWithoutComma[] = s.split(",");
            for (int i =0; i<arrayWithoutComma.length;i++) {
                stringWithoutComma = stringWithoutComma + arrayWithoutComma[i];
            }
        } else {
            stringWithoutComma = s;
        }
        System.out.println(stringWithoutComma);//TEST
        String parsed[] = stringWithoutComma.split(" ");
        String output = "";

        for (int i = 0; i< parsed.length; i++) {
            if (i< parsed.length -1) {
              output = output + parsed[i] + "+";
            } else {
                output = output + parsed[i];
            }
        }
        System.out.println(output);//Test
        return output;
    }
}