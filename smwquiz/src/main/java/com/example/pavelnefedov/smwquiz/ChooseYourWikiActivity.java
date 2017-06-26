package com.example.pavelnefedov.smwquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pavel Nefedov on 07.12.2016.
 * Activity where the user can choose which wiki he wants to use for the quiz
 */

public class ChooseYourWikiActivity extends AppCompatActivity {

    static ArrayList<HashMap<String,String>> allQuestions;
    static ArrayList<HashMap<String,String>> allAnswers;

    String numberOfQuestionsChoosen;
    String numberOfResponsesChoosen;

    private String url;
    String wikiUrl;

    Button firstWiki;
    Button secondWiki;
    Button thirdWiki;
    Button userWiki;
    EditText wikiEntry;

    private ProgressDialog pDialog;

    /*
    basic on create method of the chooseYourWiki class
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_your_wiki_activity);

        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefNumberResponsesKey = getString(R.string.preference_number_of_response_possibilities_key);
        String prefNumberResponseDefault = getString(R.string.preference_number_of_response_possibilities_default);
        numberOfResponsesChoosen = sPrefs.getString(prefNumberResponsesKey,prefNumberResponseDefault);

        String prefNumberQuestionsKey = getString(R.string.preference_number_of_questions_key);
        String prefNumberQuestionsDefault = getString(R.string.preference_number_of_questions_default);
        numberOfQuestionsChoosen = sPrefs.getString(prefNumberQuestionsKey, prefNumberQuestionsDefault);

        allQuestions = new ArrayList<HashMap<String, String>>();
        allAnswers = new ArrayList<HashMap<String, String>>();

        //first wiki we got to get the data from
        firstWiki = (Button) findViewById(R.id.wiki1);
        firstWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wikiUrl = "https://amazonas.fzi.de/kel-1/";
                saveWiki(wikiUrl);
                url =  "http://46.101.100.92:8080/?do=quest," + wikiUrl + "," + numberOfQuestionsChoosen + "," + numberOfResponsesChoosen;
                new GetJSON().execute();
            }
        });

        //second wiki we got the data from
        secondWiki = (Button) findViewById(R.id.wiki2);
        secondWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wikiUrl = "https://amazonas.fzi.de/kel-2/";
                saveWiki(wikiUrl);
                System.out.println();
                url =  "http://46.101.100.92:8080/?do=quest," + wikiUrl + "," + numberOfQuestionsChoosen + "," + numberOfResponsesChoosen;
                new GetJSON().execute();
            }
        });

        thirdWiki = (Button) findViewById(R.id.Wiki3);
        thirdWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wikiUrl = "http://aifb-ls3-vm2.aifb.kit.edu/smw-seminar2/";
                saveWiki(wikiUrl);
                url =  "http://46.101.100.92:8080/?do=quest," + wikiUrl + "," + numberOfQuestionsChoosen + "," + numberOfResponsesChoosen;
                new GetJSON().execute();
            }
        });

        wikiEntry = (EditText) findViewById(R.id.Wikieingabe);
        userWiki = (Button) findViewById(R.id.ownWiki);
        userWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wikiUrl = wikiEntry.getText().toString();
                saveWiki(wikiUrl);
                url =  "http://46.101.100.92:8080/?do=quest," + wikiUrl + "," + numberOfQuestionsChoosen + "," + numberOfResponsesChoosen;
                new GetJSON().execute();
            }
        });
    }

    public void saveWiki (String url) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sPrefs.edit();
        editor.putString("currentWiki", url); //save currentWikilink

        editor.apply();
    }

    /*
    inner class to split data from a String in JSON format
     */
     private class GetJSON extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog-
            pDialog = new ProgressDialog(ChooseYourWikiActivity.this);
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

            String jsonStr = sh.makeServiceCall(url);
        Log.e(MainActivity.class.getSimpleName(),"Response from url:" + jsonStr);
        if(jsonStr != null) {
            try {

                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray fullQuiz = jsonObj.getJSONArray("fullQuiz");

                //get the questions of the quiz as JSON Objects and sort them into a JSONArray with
                // the information about the qID, question text and also the right answer of the question
                for (int i = 0; i < fullQuiz.length(); i++) {
                    JSONObject questions = fullQuiz.getJSONObject(i);

                    String questionId = questions.getString("qId");
                    String questionText = questions.getString("Question");
                    String rightAnswer = questions.getString("rightAnswer");

                    JSONArray possibleAnswers = questions.getJSONArray("PossibleAnswers");

                    // save all answers in one array saving the aId to match the answers to a question later
                    //aID = qID, and also saving the answer text
                    for (int j = 0; j < possibleAnswers.length(); j++) {
                        JSONObject answers = possibleAnswers.getJSONObject(j);
                        String answerId = answers.getString("aId");
                        String answerText = answers.getString("Answer");

                        HashMap<String, String> possibleAnswerTMP = new HashMap<>();

                        possibleAnswerTMP.put("aId", answerId);
                        possibleAnswerTMP.put("answerText", answerText);

                        allAnswers.add(possibleAnswerTMP);
                    }

                    HashMap<String, String> questionsTMP = new HashMap<>();

                    questionsTMP.put("qId", questionId);
                    questionsTMP.put("questionText", questionText);
                    questionsTMP.put("rightAnswer", rightAnswer);

                    allQuestions.add(questionsTMP);

                }
            } catch (final JSONException e) {
               // Log.e(MainActivity.class.getSimpleName(), "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            } else {
                //Log.e(MainActivity.class.getSimpleName(), "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
            return null;
         }
        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

                    Intent intentLoadNewActivity = new Intent(ChooseYourWikiActivity.this, QuizActivityNew.class);
                    startActivity(intentLoadNewActivity);
              }
    }

}
