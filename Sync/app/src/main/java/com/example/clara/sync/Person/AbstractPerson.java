package com.example.clara.sync.Person;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractPerson {

    protected static final int PREFERENCE_MODE_PRIVATE = 0;
    protected static final String UNIQUE_PREF_FILE = "sharedPrefs";
    protected static SharedPreferences preferenceSettings;
    protected static SharedPreferences.Editor preferenceEditor;

    public String name;
    public String facebook;
    public String instagram;
    public String twitter;

    public AbstractPerson(String name, String facebook, String instagram, String twitter) {
        this.name = name;
        this.facebook = facebook;
        this.instagram = instagram;
        this.twitter = twitter;
    }

    public List<String> personList(View view) {
        Context context = view.getContext();
        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        Map<String, ?> friendsMap = preferenceSettings.getAll();
        List<String> namesList = new ArrayList<>();

        for(Map.Entry<String,?> entry : friendsMap.entrySet()){
            namesList.add(entry.getValue().toString());
        }

        return namesList;
    }

    protected String read_file(Context context, String filename) {
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
