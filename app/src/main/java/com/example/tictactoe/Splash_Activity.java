package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.tictactoe.Game_Activity.DashBoard_Activity;
import com.example.tictactoe.Login_System_Activity.Sign_In_Activity;

public class Splash_Activity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*<------------Night mode disable--------->*/

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /*<------------Handle_Timing_To_Launch_Next_Activity--------->*/
        handler = new Handler(); // initialize the handler
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Sign_In_Activity.PREFS_NAME, 0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
                Intent intent;
                if (hasLoggedIn) {
                    intent = new Intent(Splash_Activity.this, DashBoard_Activity.class);
                } else {
                    intent = new Intent(Splash_Activity.this, Sign_In_Activity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Add these flags
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}