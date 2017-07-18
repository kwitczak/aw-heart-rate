package com.krzysztofwitczak.androwearapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.krzysztofwitczak.androwearapp.game_server_connection.GameConnectionHelper;
import com.krzysztofwitczak.androwearapp.R;

public class SettingsActivity extends AppCompatActivity {
    static final String LOG_KEY = "M_SETTINGS_ACTIVITY";
    public static final String APP_KEY = "EmotionsGame";
    public static final String IP_KEY = "GameServerIP";
    public static final String DEFAULT_IP = "192.168.1.102";
    TextView gameServerIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        gameServerIP = (TextView) findViewById(R.id.server_ip);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(APP_KEY, 0);
        gameServerIP.setText(settings.getString(IP_KEY, DEFAULT_IP));
    }

    public void saveGameSetting(View view) {
        SharedPreferences sharedPref = getSharedPreferences(APP_KEY, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        String newIP = gameServerIP.getText().toString();
        editor.putString(IP_KEY, newIP);
        editor.commit();

        GameConnectionHelper.setupConnection(newIP);
        if (GameConnectionHelper.connectionSuccessful) {
            Log.i(LOG_KEY, "Connection with server successful!");
            // TODO: Show some success message?
        }

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
