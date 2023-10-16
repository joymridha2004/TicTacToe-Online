package com.example.tictactoe.Game_Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Game_Activity extends AppCompatActivity {

    /* --------------Game_Activity--------------- */

    private static int flag = 0;
    private Button GameActivityQuitButton;
    private Button GameActivity1noButton;
    private Button GameActivity2noButton;
    private Button GameActivity3noButton;
    private Button GameActivity4noButton;
    private Button GameActivity5noButton;
    private Button GameActivity6noButton;
    private Button GameActivity7noButton;
    private Button GameActivity8noButton;
    private Button GameActivity9noButton;
    private TextView Player1stGotTV;
    private TextView Project_Link;
    private TextView Player2ndGotTV;
    private TextView GameActivity1stPlayerMoveStatusTextView;
    private TextView GameActivity2ndPlayerMoveStatusTextView;
    private TextView GameActivity1stPlayerScoreTextView;
    private TextView GameActivity2ndPlayerScoreTextView;
    private String Button1, Button2, Button3, Button4, Button5, Button6, Button7, Button8, Button9, X = "X", O = "O", Player1stGot, Player2ndGot;
    private String Player1stName;
    private String Player2ndName;
    private int Move = 0, Player1stPoint = 0, Player2ndPoint = 0, ShapeMove = 0;

    /* --------------Quit_DialogBox--------------- */

    private Button QuitButton;
    private ImageButton QuitCloseIV;
    private TextView QuitNameDialogBoxTV;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /*<------------Night mode disable--------->*/

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /* --------------Previous Activity Pass the Name Through the intent--------------- */

        Player1stName = getIntent().getStringExtra("PlayerName1st");
        Player2ndName = getIntent().getStringExtra("PlayerName2nd");

        /* --------------DialogBox Creation--------------- */

        Dialog QuitDialog = new Dialog(this);
        QuitDialog.setContentView(R.layout.quit_dialog_box);

        /*---------------Hooks Game Activity--------------->*/

        GameActivityQuitButton = findViewById(R.id.Game_Activity_Quit_Button);
        GameActivity1noButton = findViewById(R.id.Game_Activity_1no_Button);
        GameActivity2noButton = findViewById(R.id.Game_Activity_2no_Button);
        GameActivity3noButton = findViewById(R.id.Game_Activity_3no_Button);
        GameActivity4noButton = findViewById(R.id.Game_Activity_4no_Button);
        GameActivity5noButton = findViewById(R.id.Game_Activity_5no_Button);
        GameActivity6noButton = findViewById(R.id.Game_Activity_6no_Button);
        GameActivity7noButton = findViewById(R.id.Game_Activity_7no_Button);
        GameActivity8noButton = findViewById(R.id.Game_Activity_8no_Button);
        GameActivity9noButton = findViewById(R.id.Game_Activity_9no_Button);
        Player1stGotTV = findViewById(R.id.Player1st_Got_TV);
        Player2ndGotTV = findViewById(R.id.Player2nd_Got_TV);
        GameActivity1stPlayerMoveStatusTextView = findViewById(R.id.Game_Activity_1st_Player_Move_Status_Text_View);
        GameActivity2ndPlayerMoveStatusTextView = findViewById(R.id.Game_Activity_2nd_Player_Move_Status_Text_View);
        GameActivity1stPlayerScoreTextView = findViewById(R.id.Game_Activity_1st_Player_Score_Text_View);
        GameActivity2ndPlayerScoreTextView = findViewById(R.id.Game_Activity_2nd_Player_Score_Text_View);
        Project_Link = findViewById(R.id.Project_Link);

        /*---------------Hooks Quit DialogBox--------------->*/

        QuitButton = QuitDialog.findViewById(R.id.Quit_Button);
        QuitCloseIV = QuitDialog.findViewById(R.id.Quit_Close_IV);
        QuitNameDialogBoxTV = QuitDialog.findViewById(R.id.Quit_Name_DialogBox_TV);

        /*---------------First Look Of Game / Restart Game--------------->*/

        Player1stGotTV.setText(Player1stName + " You " + " Got  'X'");
        Player2ndGotTV.setText(Player2ndName + " You " + " Got  'O'");
        GameActivity1stPlayerScoreTextView.setText("00");
        GameActivity2ndPlayerScoreTextView.setText("00");
        GameActivity1stPlayerMoveStatusTextView.setText("Now  " + Player1stName + " Your Game Move");
        GameActivity1stPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF000000"));
        GameActivity2ndPlayerMoveStatusTextView.setText("Waiting for Opponent Move ");
        GameActivity2ndPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF0000"));

        /*<------------Handle_Github_link_On_click_Listener--------->*/

        Project_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joymridha2004/TicTacToe-Online"));
                startActivity(intent);
            }
        });

        /*---------------On Click Listener On ReStart Game Button--------------->*/

        GameActivityQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*---------------Quit DialogBox Name Init --------------->*/

                QuitNameDialogBoxTV.setText(Player1stName + " And " + Player2ndName);

                QuitDialog.show();
            }
        });

        /* --------------Handle onClicks on  dialogBox Quit Button------------------- */

        QuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScoreUpdate();
                startActivity(new Intent(Game_Activity.this, DashBoard_Activity.class));
                finish();
            }
        });

        /* --------------Handle onClicks on  dialogBox Close Button------------------- */

        QuitCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuitDialog.dismiss();
            }
        });

    }

    private void ScoreUpdate() {

        /* --------------Handle_Upload_Score_to_Firebase_Cloud_Storage------------------- */

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("matchDetails")
                .document(mAuth.getCurrentUser().getUid())
                .collection("matchId")
                .orderBy("matchIndex", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int matchIndex = 0;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            QueryDocumentSnapshot lastMatch = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                            if (lastMatch.contains("matchIndex")) {
                                matchIndex = lastMatch.getLong("matchIndex").intValue() + 1;
                            }
                        }

                        Map<String, Object> matchDetails = new HashMap<>();
                        matchDetails.put("matchTime", getCurrentTimeWithAmAndPm());
                        matchDetails.put("matchDate", getCurrentDate());
                        matchDetails.put("1stPlayerName", Player1stName);
                        matchDetails.put("2ndPlayerName", Player2ndName);
                        matchDetails.put("1stPlayerScore", Player1stPoint);
                        matchDetails.put("2ndPlayerScore", Player2ndPoint);
                        matchDetails.put("matchIndex", matchIndex);

                        // Generate a unique match ID
                        String matchId = generateMatchId();

                        // Implement your own match ID generation logic
                        db.collection("matchDetails")
                                .document(mAuth.getCurrentUser().getUid())
                                .collection("matchId")
                                .document(matchId)
                                .set(matchDetails)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Handle successful write
                                        Log.d(TAG, "Match details added successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle any errors
                                        Log.e(TAG, "Error adding match details: " + e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e(TAG, "Error fetching match details: " + e.getMessage());
                    }
                });

    }

    /* --------------Handle_Get_CurrentTime------------------- */

    private String getCurrentTimeWithAmAndPm() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    /* --------------Handle_Get_CurrentDate------------------- */

    private String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(new Date());
    }

    /* --------------Handle_Get_Unique_MatchId------------------- */

    private String generateMatchId() {
        return UUID.randomUUID().toString();
    }

    /* --------------Handle_Game_Logic------------------- */

    @SuppressLint("ResourceAsColor")
    public void check(View view) {
        if (flag == 0) {
            Button ButtonCurrent = (Button) view;
            if (ButtonCurrent.getText().toString().equals("")) {


                Move++;
                if (ShapeMove % 2 == 0) {
                    Player1stGot = "X";
                    Player2ndGot = "O";
                    Player1stGotTV.setText(Player1stName + " You " + " Got  '" + Player1stGot + "'");
                    Player2ndGotTV.setText(Player2ndName + " You " + " Got  '" + Player2ndGot + "'");
                } else {
                    Player1stGot = "O";
                    Player2ndGot = "X";
                    Player1stGotTV.setText(Player1stName + " You " + " Got  '" + Player1stGot + "'");
                    Player2ndGotTV.setText(Player2ndName + " You " + " Got  '" + Player2ndGot + "'");
                }

                if (Move % 2 != 0) {
                    ButtonCurrent.setText("X");
                    if (Player1stGot.equals("X")) {
                        GameActivity2ndPlayerMoveStatusTextView.setText("Now  " + Player2ndName + " Your Game Move");
                        GameActivity1stPlayerMoveStatusTextView.setText("Waiting for Opponent Move ");
                        GameActivity1stPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF0000"));
                        GameActivity2ndPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF000000"));
                    } else {
                        GameActivity1stPlayerMoveStatusTextView.setText("Now  " + Player1stName + " Your Game Move");
                        GameActivity2ndPlayerMoveStatusTextView.setText("Waiting for Opponent Move ");
                        GameActivity1stPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF000000"));
                        GameActivity2ndPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF0000"));
                    }

                } else {
                    ButtonCurrent.setText("O");
                    ButtonCurrent.setTextColor(Color.parseColor("#FF0000"));
                    if (Player1stGot.equals("O")) {
                        GameActivity2ndPlayerMoveStatusTextView.setText("Now  " + Player2ndName + " Your Game Move");
                        GameActivity1stPlayerMoveStatusTextView.setText("Waiting for Opponent Move ");
                        GameActivity1stPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF0000"));
                        GameActivity2ndPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF000000"));
                    } else {
                        GameActivity1stPlayerMoveStatusTextView.setText("Now  " + Player1stName + " Your Game Move");
                        GameActivity2ndPlayerMoveStatusTextView.setText("Waiting for Opponent Move ");
                        GameActivity1stPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF000000"));
                        GameActivity2ndPlayerMoveStatusTextView.setTextColor(Color.parseColor("#FF0000"));
                    }
                }

                if (Move > 4) {
                    Button1 = GameActivity1noButton.getText().toString();
                    Button2 = GameActivity2noButton.getText().toString();
                    Button3 = GameActivity3noButton.getText().toString();
                    Button4 = GameActivity4noButton.getText().toString();
                    Button5 = GameActivity5noButton.getText().toString();
                    Button6 = GameActivity6noButton.getText().toString();
                    Button7 = GameActivity7noButton.getText().toString();
                    Button8 = GameActivity8noButton.getText().toString();
                    Button9 = GameActivity9noButton.getText().toString();

                    /*----------------------Conditions---------------------------*/


                    if (Button1.equals(Button2) && Button2.equals(Button3) && !Button1.equals("")) {
                        FindWinner(Button1, 1);
                        flag = 1;
                    } else if (Button4.equals(Button5) && Button5.equals(Button6) && !Button4.equals("")) {
                        FindWinner(Button4, 2);
                        flag = 1;
                    } else if (Button7.equals(Button8) && Button8.equals(Button9) && !Button7.equals("")) {
                        FindWinner(Button7, 3);
                        flag = 1;
                    } else if (Button1.equals(Button4) && Button4.equals(Button7) && !Button1.equals("")) {
                        FindWinner(Button1, 4);
                        flag = 1;
                    } else if (Button2.equals(Button5) && Button5.equals(Button8) && !Button2.equals("")) {
                        FindWinner(Button2, 5);
                        flag = 1;
                    } else if (Button3.equals(Button6) && Button6.equals(Button8) && !Button3.equals("")) {
                        FindWinner(Button3, 6);
                        flag = 1;
                    } else if (Button1.equals(Button5) && Button5.equals(Button9) && !Button1.equals("")) {
                        FindWinner(Button1, 7);
                        flag = 1;
                    } else if (Button3.equals(Button5) && Button5.equals(Button7) && !Button3.equals("")) {
                        FindWinner(Button3, 8);
                        flag = 1;
                    } else if (Move == 9) {
                        NewGame();
                        flag = 1;
                    }

                }
            }
        }
    }

    public void NewGame() {

        /*----------------------Time Delay---------------------------*/

        Runnable runnable = new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {

                GameActivity1noButton.setText("");
                GameActivity2noButton.setText("");
                GameActivity3noButton.setText("");
                GameActivity4noButton.setText("");
                GameActivity5noButton.setText("");
                GameActivity6noButton.setText("");
                GameActivity7noButton.setText("");
                GameActivity8noButton.setText("");
                GameActivity9noButton.setText("");

                GameActivity1noButton.setVisibility(View.VISIBLE);
                GameActivity2noButton.setVisibility(View.VISIBLE);
                GameActivity3noButton.setVisibility(View.VISIBLE);
                GameActivity4noButton.setVisibility(View.VISIBLE);
                GameActivity5noButton.setVisibility(View.VISIBLE);
                GameActivity6noButton.setVisibility(View.VISIBLE);
                GameActivity7noButton.setVisibility(View.VISIBLE);
                GameActivity8noButton.setVisibility(View.VISIBLE);
                GameActivity9noButton.setVisibility(View.VISIBLE);

                GameActivity1noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity2noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity3noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity4noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity5noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity6noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity7noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity8noButton.setTextColor(Color.parseColor("#102E44"));
                GameActivity9noButton.setTextColor(Color.parseColor("#102E44"));

                Move = 0;
                ShapeMove++;


                flag = 0;

            }
        };
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 2000);

    }

    /* --------------Handle_Finding_Who_is_Winner------------------- */

    public void FindWinner(String WinSymbol, int Format) {

        if (WinSymbol.equals(Player1stGot)) {
            Player1stPoint++;
            if (Player1stGot.equals("X")) {
                Player1stGotTV.setText(Player1stName + " You " + " Got  'O'");
                Player2ndGotTV.setText(Player2ndName + " You " + " Got  'X'");
            } else {
                Player1stGotTV.setText(Player1stName + " You " + " Got  'X'");
                Player2ndGotTV.setText(Player2ndName + " You " + " Got  'O'");
            }
            Toast.makeText(this, "Winner is : " + Player1stName, Toast.LENGTH_SHORT).show();
            if (Player1stPoint <= 9) {
                GameActivity1stPlayerScoreTextView.setText("0" + Player1stPoint);
            } else {
                GameActivity1stPlayerScoreTextView.setText("" + Player1stPoint);
            }
        } else if (WinSymbol.equals(Player2ndGot)) {
            Toast.makeText(this, "Winner is : " + Player2ndName, Toast.LENGTH_SHORT).show();
            Player2ndPoint++;
            if (Player2ndGot.equals("X")) {
                Player1stGotTV.setText(Player1stName + " You " + " Got  'X'");
                Player2ndGotTV.setText(Player2ndName + " You " + " Got  'O'");
            } else {
                Player1stGotTV.setText(Player1stName + " You " + " Got  'X'");
                Player2ndGotTV.setText(Player2ndName + " You " + " Got  'O'");
            }
            if (Player2ndPoint <= 9) {
                GameActivity2ndPlayerScoreTextView.setText("0" + Player2ndPoint);
            } else {
                GameActivity2ndPlayerScoreTextView.setText("" + Player2ndPoint);
            }
        } else if (WinSymbol.equals("")) {
            Toast.makeText(this, "Game is Draw", Toast.LENGTH_SHORT).show();
        }

        winningAnimation(Format);
        NewGame();

    }

    /* --------------Handle_Winning_Animation------------------- */

    private void winningAnimation(int format) {
        if (format == 1) {
            GameActivity4noButton.setVisibility(View.INVISIBLE);
            GameActivity5noButton.setVisibility(View.INVISIBLE);
            GameActivity6noButton.setVisibility(View.INVISIBLE);
            GameActivity7noButton.setVisibility(View.INVISIBLE);
            GameActivity8noButton.setVisibility(View.INVISIBLE);
            GameActivity9noButton.setVisibility(View.INVISIBLE);
        } else if (format == 2) {
            GameActivity1noButton.setVisibility(View.INVISIBLE);
            GameActivity2noButton.setVisibility(View.INVISIBLE);
            GameActivity3noButton.setVisibility(View.INVISIBLE);
            GameActivity7noButton.setVisibility(View.INVISIBLE);
            GameActivity8noButton.setVisibility(View.INVISIBLE);
            GameActivity9noButton.setVisibility(View.INVISIBLE);
        } else if (format == 3) {
            GameActivity1noButton.setVisibility(View.INVISIBLE);
            GameActivity2noButton.setVisibility(View.INVISIBLE);
            GameActivity3noButton.setVisibility(View.INVISIBLE);
            GameActivity4noButton.setVisibility(View.INVISIBLE);
            GameActivity5noButton.setVisibility(View.INVISIBLE);
            GameActivity6noButton.setVisibility(View.INVISIBLE);
        } else if (format == 4) {
            GameActivity2noButton.setVisibility(View.INVISIBLE);
            GameActivity3noButton.setVisibility(View.INVISIBLE);
            GameActivity5noButton.setVisibility(View.INVISIBLE);
            GameActivity6noButton.setVisibility(View.INVISIBLE);
            GameActivity8noButton.setVisibility(View.INVISIBLE);
            GameActivity9noButton.setVisibility(View.INVISIBLE);
        } else if (format == 5) {
            GameActivity1noButton.setVisibility(View.INVISIBLE);
            GameActivity3noButton.setVisibility(View.INVISIBLE);
            GameActivity4noButton.setVisibility(View.INVISIBLE);
            GameActivity6noButton.setVisibility(View.INVISIBLE);
            GameActivity7noButton.setVisibility(View.INVISIBLE);
            GameActivity9noButton.setVisibility(View.INVISIBLE);
        } else if (format == 6) {
            GameActivity1noButton.setVisibility(View.INVISIBLE);
            GameActivity2noButton.setVisibility(View.INVISIBLE);
            GameActivity4noButton.setVisibility(View.INVISIBLE);
            GameActivity5noButton.setVisibility(View.INVISIBLE);
            GameActivity7noButton.setVisibility(View.INVISIBLE);
            GameActivity9noButton.setVisibility(View.INVISIBLE);
        } else if (format == 7) {
            GameActivity2noButton.setVisibility(View.INVISIBLE);
            GameActivity3noButton.setVisibility(View.INVISIBLE);
            GameActivity4noButton.setVisibility(View.INVISIBLE);
            GameActivity6noButton.setVisibility(View.INVISIBLE);
            GameActivity7noButton.setVisibility(View.INVISIBLE);
            GameActivity8noButton.setVisibility(View.INVISIBLE);
        } else if (format == 8) {
            GameActivity1noButton.setVisibility(View.INVISIBLE);
            GameActivity2noButton.setVisibility(View.INVISIBLE);
            GameActivity4noButton.setVisibility(View.INVISIBLE);
            GameActivity6noButton.setVisibility(View.INVISIBLE);
            GameActivity8noButton.setVisibility(View.INVISIBLE);
            GameActivity9noButton.setVisibility(View.INVISIBLE);
        }
    }

}