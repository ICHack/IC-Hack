package com.example.clara.sync.Person;

import android.content.Context;
import android.view.View;

import com.example.clara.sync.MainActivity;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;

public class Profile extends AbstractPerson {

    public Profile(String name, String facebook, String instagram, String twitter) {
        super(name, facebook, instagram, twitter);
    }

    public static void savePerson(View view) throws IOException {
        String editName = MainActivity.name.getText().toString();
        String editFacebook = MainActivity.facebook.getText().toString();
        String editInstagram = MainActivity.instagram.getText().toString();
        String editTwitter =  MainActivity.instagram.getText().toString();


        // Convert to JSON:
        Profile obj = new Profile(editName, editFacebook, editInstagram, editTwitter);
        Context context = view.getContext();
        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(obj);
        preferenceEditor.putString("profile", json);
        preferenceEditor.apply();

        String sFileName = editName.replaceAll("\\s+", " ") + ".txt";

        FileOutputStream outputStream;
        try {
            outputStream = view.getContext().openFileOutput(sFileName, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Profile loadPerson(View view) throws IOException {
        Context context = view.getContext();
        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);

        Gson ngson = new Gson();
        String lJson = preferenceSettings.getString("profile", null);
        Profile loadProfile = ngson.fromJson(lJson, Profile.class);


        // Finds file name
        String sFileName = loadProfile.name.replaceAll("\\s+"," ") + ".txt";
        String yourFilePath = context.getFilesDir() + "/" + sFileName;

        Gson gson = new Gson();

        // reads file into a string called json
        String json = read_file(context, sFileName);
        // creates an object from the json string
        Profile myObj = gson.fromJson(json, Profile.class);

        // puts the json into a SharedPreference
        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.putString("profile", json);
        preferenceEditor.apply();

        // Loads SharedPreference
        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        // takes Json string from SharedPreference
        String loadedJson = preferenceSettings.getString("profile", null);
        // converts Json string to object
        System.out.println(loadedJson);
        Profile profile = gson.fromJson(loadedJson, Profile.class);
        // retrieves name from object

        return profile;
    }
}
