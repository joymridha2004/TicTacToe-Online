package com.example.tictactoe.Game_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;

public class Game_Activity extends AppCompatActivity {
    /* --------------Game_Activity--------------- */
    static int flag = 0;

    Button GameActivityQuitButton;

    Button GameActivity1noButton;
    Button GameActivity2noButton;
    Button GameActivity3noButton;
    Button GameActivity4noButton;
    Button GameActivity5noButton;
    Button GameActivity6noButton;
    Button GameActivity7noButton;
    Button GameActivity8noButton;
    Button GameActivity9noButton;

    TextView Player1stGotTV;
    TextView Player2ndGotTV;
    TextView GameActivity1stPlayerMoveStatusTextView;
    TextView GameActivity2ndPlayerMoveStatusTextView;
    TextView GameActivity1stPlayerScoreTextView;
    TextView GameActivity2ndPlayerScoreTextView;

    String Button1, Button2, Button3, Button4, Button5, Button6, Button7, Button8, Button9, X = "X", O = "O", Player1stGot, Player2ndGot;
    String Player1stName;
    String Player2ndName;

    TextView Game_Activity_Version_TV;

    int Move = 0, Player1stPoint = 0, Player2ndPoint = 0, ShapeMove = 0;


    /* --------------Quit_DialogBox--------------- */
    Button QuitButton;
    ImageButton QuitCloseIV;
    TextView QuitNameDialogBoxTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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