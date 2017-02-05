package com.example.clara.sync;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    TextView namePerson;
    TextView fbPerson;
    TextView instaPerson;
    TextView twitPerson;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SYNC");

        namePerson = (TextView) findViewById(R.id.name);
        fbPerson = (TextView) findViewById(R.id.fb);
        instaPerson = (TextView) findViewById(R.id.insta);
        twitPerson = (TextView) findViewById(R.id.twit);

        Button updateButton = (Button) findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    saveMessage();
                    System.out.println("DASDASKDKASFCKADSKFKA");
                } catch (IOException e) {
                    e.printStackTrace();
                    namePerson.setText("It works");
                }

            }
        });


        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout =
                (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    //draweeer

    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                osArray);
        mDrawerList.setAdapter(mAdapter);

      // Button b = (Button) mDrawerList.getItemAtPosition(0);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();

                try {
                    loadMessage();
                    System.out.println("DADADADADADADADA");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // namePerson.setText("It works");
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.bringToFront();
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
              invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Amar's part

        private static final int PREFERENCE_MODE_PRIVATE = 0;
        private static final String UNIQUE_PREF_FILE = "sharedPrefs";
        private SharedPreferences preferenceSettings;
        private SharedPreferences.Editor preferenceEditor;


        public void saveMessage() throws IOException {
            EditText editTextName = (EditText) findViewById(R.id.name);
            EditText editTextFb = (EditText) findViewById(R.id.fb);
            EditText editTextTwit = (EditText) findViewById(R.id.twit);
            EditText editTextInsta = (EditText) findViewById(R.id.insta);


            // Convert to JSON:
            Profile obj = new Profile(editTextName.getText().toString(), editTextFb.getText().toString(),
                    editTextInsta.getText().toString(), editTextTwit.getText().toString());

            preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
            preferenceEditor = preferenceSettings.edit();

            Gson gson = new Gson();
            String json = gson.toJson(obj);
            preferenceEditor.putString("profile", json);
            preferenceEditor.apply();

            String sFileName = obj.getName().replaceAll("\\s+"," ") + ".txt";

            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(sFileName, Context.MODE_PRIVATE);
                outputStream.write(json.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }




        public void loadMessage() throws IOException {
            preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);

            Gson ngson = new Gson();
            String lJson = preferenceSettings.getString("profile", null);
            Profile loadProfile = ngson.fromJson(lJson, Profile.class);


            // Finds file name
            String sFileName = loadProfile.getName().replaceAll("\\s+"," ") + ".txt";
            Context context = getApplicationContext();
            String yourFilePath = context.getFilesDir() + "/" + sFileName;

            Gson gson = new Gson();

            // reads file into a string called json
            String json = read_file(context, sFileName);
            // creates an object from the json string
            Profile myObj = gson.fromJson(json, Profile.class);

            // puts the json into a SharedPreference
            preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
            preferenceEditor = preferenceSettings.edit();
            preferenceEditor.putString("profile", json);
            preferenceEditor.apply();

            // Loads SharedPreference
            preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
            // takes Json string from SharedPreference
            String loadedJson = preferenceSettings.getString("profile", null);
            // converts Json string to object
            System.out.println(loadedJson);
            Profile loadedMyObj = gson.fromJson(loadedJson, Profile.class);
            // retrieves name from object
            //String name = loadedMyObj.getName();

            if (loadedMyObj.getName() != null){
                EditText editText = (EditText) findViewById(R.id.name);
                editText.setText(loadedMyObj.getName());

            }

            if (loadedMyObj.getFacebook() != null){
                EditText editText = (EditText) findViewById(R.id.fb);
                editText.setText(loadedMyObj.getFacebook());
            }
            if (loadedMyObj.getInstagram() != null){
                EditText editText = (EditText) findViewById(R.id.insta);
                editText.setText(loadedMyObj.getInstagram());
            }
            if (loadedMyObj.getTwitter() != null){
                EditText editText = (EditText) findViewById(R.id.twit);
                editText.setText(loadedMyObj.getTwitter());
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

        public void saveFriend(){
            EditText editTextName = (EditText) findViewById(R.id.name);
            EditText editTextFb = (EditText) findViewById(R.id.fb);
            EditText editTextTwit = (EditText) findViewById(R.id.twit);
            EditText editTextInsta = (EditText) findViewById(R.id.insta);

            Friend obj = new Friend(editTextName.getText().toString(), editTextFb.getText().toString(),
                    editTextInsta.getText().toString(), editTextTwit.getText().toString());

            obj.setName(editTextName.getText().toString());

            preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
            preferenceEditor = preferenceSettings.edit();

            Gson gson = new Gson();
            String json = gson.toJson(obj);
            preferenceEditor.putString(obj.getName().replaceAll("\\s+"," "), json);
            preferenceEditor.apply();
        }

        public void loadFriend(){
            preferenceSettings = getSharedPreferences(UNIQUE_PREF_FILE, PREFERENCE_MODE_PRIVATE);
            Map<String, ?> friendsMap = preferenceSettings.getAll();
            List<String> namesList = new ArrayList<>();

            for(Map.Entry<String,?> entry : friendsMap.entrySet()){
                namesList.add(entry.getKey().toString());
            }

            String nameToLoad = namesList.get(2);
            String loadedJson = preferenceSettings.getString(nameToLoad, null);

            Gson gson = new Gson();
            Friend friendToLoad = gson.fromJson(loadedJson, Friend.class);

            EditText editText = (EditText) findViewById(R.id.name);
            editText.setText(friendToLoad.getName());

            editText = (EditText) findViewById(R.id.fb);
            editText.setText(friendToLoad.getFacebook());

            editText = (EditText) findViewById(R.id.insta);
            editText.setText(friendToLoad.getInstagram());

            editText = (EditText) findViewById(R.id.twit);
            editText.setText(friendToLoad.getTwitter());
        }






}
