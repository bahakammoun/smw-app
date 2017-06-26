package com.example.pavelnefedov.smwquiz;

/**
 * Created by Pavel Nefedov on 29.12.2016.
 * This is the Quiz Activity where the quiz is running, loading the questions and their answers until the quiz ends
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;



public class QuizActivityNew extends ActionBarActivity implements View.OnClickListener {

    int numberOfAnswers;
    int maxQuestions;
    String[] answers;
    Bundle korb = new Bundle();
    int numberOfQuestions = ChooseYourWikiActivity.allQuestions.size();
    int counter = 0;
    String[] choices = new String[numberOfQuestions];
    int idQuestion = 100;
    Button btn;
    String zwischenergebniss = "";
    static int numberRightAnswers;
    //String questionInput = "\n" + ChooseYourWikiActivity.allQuestions.get(counter).get("questionText");
    Button answer;
    EditText freiText;
    Button done;


    TextView question;
    String outputText;
    static int finalRightAnswers;

    /*
    basic onCreate method of the QuizActivity class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_new);

        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefNumberResponsesKey = getString(R.string.preference_number_of_response_possibilities_key);
        String prefNumberResponseDefault = getString(R.string.preference_number_of_response_possibilities_default);
        String numberOfResponsesChoosen = sPrefs.getString(prefNumberResponsesKey, prefNumberResponseDefault);
        answers = new String[Integer.valueOf(numberOfResponsesChoosen)];
        numberOfAnswers = Integer.valueOf(numberOfResponsesChoosen);
        String prefNumberQuestionsKey = getString(R.string.preference_number_of_questions_key);
        String prefNumberQuestionsDefault = getString(R.string.preference_number_of_questions_default);
        String numberOfQuestionsChoosen = sPrefs.getString(prefNumberQuestionsKey, prefNumberQuestionsDefault);
        maxQuestions = Integer.valueOf(numberOfQuestionsChoosen);
        init();
        createNextQuestion();
    }

    public void init() {
        //init Frage
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        question = new TextView(this);
        question.setId(100);

        question.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        question.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        mainLayout.addView(question);
        //init Antworten
        if (numberOfAnswers > 0) {
            for (int i = 0; i < numberOfAnswers; i++) {

                Button btn = new Button(this);
                btn.setId(i);
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Button btn = (Button) v;

                        if (ChooseYourWikiActivity.allQuestions.get(counter).get("rightAnswer").contains(btn.getText().toString())) {
                            numberRightAnswers++;
                            outputText = "Richtig!";
                        } else {
                            outputText = "Leider Falsch!" +
                                    " Richtig ist: " + ChooseYourWikiActivity.allQuestions.get(counter).get("rightAnswer");

                        }
                        Toast.makeText(QuizActivityNew.this, outputText, Toast.LENGTH_SHORT).show();
                        counter++;
                        if (counter < numberOfQuestions) {
                            createNextQuestion();
                        } else {
                            finalRightAnswers = numberRightAnswers;
                            numberRightAnswers = 0;
                            Intent intent = new Intent(QuizActivityNew.this, QuizResultsActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
                mainLayout.addView(btn);
            }
        } else //init freitextfeld
        {

            final EditText freitext = new EditText(this);
            Button btn = new Button(this);
            btn.setText("Bestätigen");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Button btn = (Button) v;
                    if (ChooseYourWikiActivity.allQuestions.get(counter).get("rightAnswer").contains(freitext.getText().toString())) {
                        numberRightAnswers++;
                        outputText = "Richtig!";
                    } else {
                        outputText = "Leider Falsch!" +
                                " Richtig ist: " + ChooseYourWikiActivity.allQuestions.get(counter).get("rightAnswer");
                    }
                    Toast.makeText(QuizActivityNew.this, outputText, Toast.LENGTH_SHORT).show();
                    counter++;
                    if (counter < numberOfQuestions) {
                        createNextQuestion();
                    } else {
                        finalRightAnswers = numberRightAnswers;
                        numberRightAnswers = 0;
                        Intent intent = new Intent(QuizActivityNew.this, QuizResultsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            mainLayout.addView(freitext);
            mainLayout.addView(btn);
        }
    }

    @Override
    public void onClick(View v) {

        Log.d("MainActivity", "onClick  " + v.getId());
    }

    public void createNextQuestion() {

        TextView question = (TextView) findViewById(100);
        question.setText(ChooseYourWikiActivity.allQuestions.get(counter).get("questionText"));

        if (numberOfAnswers > 0) {
            String [] antworten = getAnswers();
            for (int i = 1; i < numberOfAnswers + 1; i++) {
                Button btn = (Button) findViewById(i - 1);
                btn.setText(antworten[i-1]);

                if (btn.getText().equals("NotAvailable")) {
                    btn.setVisibility(View.GONE);
                    Toast.makeText(QuizActivityNew.this, "Das Wiki hat zu wenig Daten", Toast.LENGTH_SHORT).show();
                } else {
                    btn.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public String[] getAnswers() {

        String[] responses = new String[numberOfAnswers];
        int m = 0;
        int questionNumber = counter + 1;

        String questionId = "q" + String.valueOf(questionNumber);

        for (int k = 0; k < ChooseYourWikiActivity.allAnswers.size(); k++) {

            if (questionId.equals(ChooseYourWikiActivity.allAnswers.get(k).get("aId"))) {
                responses[m] = ChooseYourWikiActivity.allAnswers.get(k).get("answerText");
                m++;
            }

        }
        questionNumber++;
        return responses;
    }


}