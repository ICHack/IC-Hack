package com.benjamindimant.savingkeyvaluesets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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

    public void saveMessage(View view) throws IOException {
        EditText editText = (EditText) findViewById(R.id.edit_message);


        // Convert to JSON:
        TransferObj obj = new TransferObj();
        obj.name = editText.getText().toString();
        preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(obj);
        preferenceEditor.putString("TransferObject", json);
        preferenceEditor.apply();

        String sFileName = "self.txt";

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(sFileName, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void loadMessage(View view) throws IOException {

        // Finds file name
        String sFileName = "self.txt";
        Context context = getApplicationContext();
        String yourFilePath = context.getFilesDir() + "/" + sFileName;

        Gson gson = new Gson();

        // reads file into a string called json
        String json = read_file(context, sFileName);
        // creates an object from the json string
        TransferObj myObj = gson.fromJson(json, TransferObj.class);

        // puts the json into a SharedPreference
        preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.putString("TransferObject", json);
        preferenceEditor.apply();

        // Loads SharedPreference
        preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        // takes Json string from SharedPreference
        String loadedJson = preferenceSettings.getString("TransferObject", null);
        // converts Json string to object
        System.out.println(loadedJson);
        TransferObj loadedMyObj = gson.fromJson(loadedJson, TransferObj.class);
        // retrieves name from object
        String name = loadedMyObj.name;

        if (name != null){
            EditText editText = (EditText) findViewById(R.id.edit_message);
            editText.setText(name);
        }

    }

    public String read_file(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }
}