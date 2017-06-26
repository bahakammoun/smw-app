package com.example.pavelnefedov.smwquiz;

/**
 * Created by Pavel Nefedov on 07.12.2016.
 */

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.widget.Toast;


public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection deprecation
        addPreferencesFromResource(R.xml.preferences);

        //noinspection deprecation
        Preference agePref = findPreference(getString(R.string.preference_age_key));
        agePref.setOnPreferenceChangeListener(this);

        //noinspection deprecation
        Preference genderPref = findPreference(getString(R.string.preference_gender_key));
        genderPref.setOnPreferenceChangeListener(this);

        //noinspection deprecation
        Preference numberOfQuestionsPref = findPreference(getString(R.string.preference_number_of_questions_key));
        numberOfQuestionsPref.setOnPreferenceChangeListener(this);

        //noinspection deprecation
        Preference answerPossibilitiesPref = findPreference(getString(R.string.preference_number_of_response_possibilities_key));
        answerPossibilitiesPref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        return true;
    }
}
