package com.example.tictactoe.Game_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;

public class Players_Details_Activity extends AppCompatActivity {

    private EditText PlayersDetailsActivity1stNameET, PlayersDetailsActivity2ndNameET;
    private Button PlayersDetailsActivityStartGameButton;
    private TextView Project_Link;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_details);

        /*<------------Night mode disable--------->*/

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /*<------------Hooks--------->*/

        PlayersDetailsActivity1stNameET = findViewById(R.id.Players_Details_Activity_1st_Name_ET);
        PlayersDetailsActivity2ndNameET = findViewById(R.id.Players_Details_Activity_2nd_Name_ET);
        PlayersDetailsActivityStartGameButton = findViewById(R.id.Players_Details_Activity_Start_Game_Button);
        Project_Link = findViewById(R.id.Project_Link);

        /*<------------Handle_Github_link_On_click_Listener--------->*/

        Project_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joymridha2004/TicTacToe-Online"));
                overridePendingTransition(R.anim.from_right, R.anim.out_from_left);
                startActivity(intent);
            }
        });

        /*---------------On Click Listener On Start Game Button--------------->*/

        PlayersDetailsActivityStartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*---------------Conditions--------------->*/

                if (!PlayersDetailsActivity1stNameET.getText().toString().isEmpty() && !PlayersDetailsActivity2ndNameET.getText().toString().isEmpty() && !PlayersDetailsActivity1stNameET.getText().toString().equals(PlayersDetailsActivity2ndNameET.getText().toString())) {
                    Toast.makeText(getApplicationContext(), " Hi   " + PlayersDetailsActivity1stNameET.getText().toString() + "  and   " + PlayersDetailsActivity2ndNameET.getText().toString(), Toast.LENGTH_SHORT).show();

                    /*---------------Save the name --------------->*/

                    String Player1stName = PlayersDetailsActivity1stNameET.getText().toString();
                    String Player2ndName = PlayersDetailsActivity2ndNameET.getText().toString();

                    /*---------------Pass the Name through The Intent --------------->*/

                    Intent intent = new Intent(Players_Details_Activity.this, Game_Activity.class);

                    intent.putExtra("PlayerName1st", Player1stName);
                    intent.putExtra("PlayerName2nd", Player2ndName);

                    startActivity(intent);

                    /*---------------After start Activity Status --------------->*/

                    finish();

                } else if (PlayersDetailsActivity1stNameET.getText().toString().equals(PlayersDetailsActivity2ndNameET.getText().toString()) && !PlayersDetailsActivity2ndNameET.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Two Different Name", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}