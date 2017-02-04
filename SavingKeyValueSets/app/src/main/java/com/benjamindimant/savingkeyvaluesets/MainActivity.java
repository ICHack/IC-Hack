package com.benjamindimant.savingkeyvaluesets;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String UNIQUE_PREF_FILE = "self";
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void saveMessage(View view){
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String name = editText.getText().toString();
        name.replaceAll("\\s+","");

        // Opens preference file and saves instagram and facebook links
        preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        preferenceEditor.putString("name", name);

        preferenceEditor.apply();
    }

    public void loadMessage(View view){

        preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        String name = preferenceSettings.getString("name", null);

        if (name != null){
            EditText editText = (EditText) findViewById(R.id.edit_message);
            editText.setText(name);
        }

    }
}