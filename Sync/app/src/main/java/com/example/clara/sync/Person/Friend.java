package com.example.clara.sync.Person;

import android.content.Context;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Friend extends AbstractPerson {

    public Friend(String name, String facebook, String instagram, String twitter) {
        super(name, facebook, instagram, twitter);
    }

    public void saveFriend(View view, String filename){
        Context context = view.getContext();

        Gson gson = new Gson();
        // reads file into a string called json
        String json = read_file(context, filename);
        // creates an object from the json string
        Friend friend = gson.fromJson(json, Friend.class);

        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        json = gson.toJson(friend);

        preferenceEditor.putString(friend.name.replaceAll("\\s+"," "), json);
        preferenceEditor.apply();
    }


    public Friend loadFriend(View view, String friendName){
        Context context = view.getContext();
        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);

        Gson ngson = new Gson();
        String lJson = preferenceSettings.getString(friendName.replaceAll("\\s+"," "), null);
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
        preferenceEditor.putString(friendName.replaceAll("\\s+"," "), json);
        preferenceEditor.apply();

        // Loads SharedPreference
        preferenceSettings = context.getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
        // takes Json string from SharedPreference
        String loadedJson = preferenceSettings.getString(friendName.replaceAll("\\s+"," "), null);
        // converts Json string to object
        System.out.println(loadedJson);
        Friend friend = gson.fromJson(loadedJson, Friend.class);
        // retrieves name from object

        return friend;
    }
}
