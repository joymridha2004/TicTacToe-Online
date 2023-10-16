package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

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


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(Sign_In_Activity.PREFS_NAME, 0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
                Intent intent = null;
                if (hasLoggedIn) {
                    intent = new Intent(Splash_Activity.this, DashBoard_Activity.class);
                    finish();
                } else {
                    intent = new Intent(Splash_Activity.this, Sign_In_Activity.class);
                    finish();
                }
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}