package com.aperez.apps.androidfunwithflags;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aperez.apps.data.DatabaseHelper;
import com.aperez.apps.eventhandlers.PreferenceChangeListener;
import com.aperez.apps.lifecyclehelpers.QuizViewModel;

public class MainActivity extends AppCompatActivity {
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    private boolean deviceIsPhone = true;
    private boolean preferencesChanged = true;
    private MainActivityFragment quizFragment;
    private QuizViewModel quizViewModel;
    private OnSharedPreferenceChangeListener preferencesChangeListener;

    private String player = "0";
    private String actualLevel = "0";
    private TextView playerName;

    private void setSharedPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(preferencesChangeListener);
    }

    private void screenSetUp() {
        if (getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            deviceIsPhone = false;
        }
        if (deviceIsPhone) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.quizViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);
        this.preferencesChangeListener = new PreferenceChangeListener(this);
        //Edit the preferences file
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        DatabaseHelper SIMPdbHelper = new DatabaseHelper(this, "AddressBook.db", null, 1);
        SQLiteDatabase SIMPsql = SIMPdbHelper.getReadableDatabase();

        playerName = findViewById(R.id.textViewJugador);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String player = extras.getString("player");
            //The key argument here must match that used in the other activity
            if(player.equals("1")){
                playerName.setText("Danny");
            } else if(player.equals("2")){
                playerName.setText("Álex");
            }
        }

        String SIMPconsulta = "SELECT ActualLevel " +
                "FROM POINTS " +
                "WHERE Player = '" + player + "'";

        Cursor cursor = SIMPsql.rawQuery(SIMPconsulta, null);


        editor.putString("pref_numberOfChoices", "2");

        editor.commit();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setSharedPreferences();
        this.screenSetUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferencesChanged) {
            this.quizFragment = (MainActivityFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.quizFragment);
            this.quizViewModel.setGuessRows(PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(CHOICES, null));
            this.quizViewModel.setRegionsSet(PreferenceManager.getDefaultSharedPreferences(this)
                    .getStringSet(REGIONS, null));

            this.quizFragment.resetQuiz();

            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    public int getScreenSize() {
        return getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    public MainActivityFragment getQuizFragment() {
        return this.quizFragment;
    }

    public QuizViewModel getQuizViewModel() {
        return quizViewModel;
    }

    public static String getCHOICES() {
        return CHOICES;
    }

    public static String getREGIONS() {
        return REGIONS;
    }

    public void setPreferencesChanged(boolean preferencesChanged) {
        this.preferencesChanged = preferencesChanged;
    }


}
