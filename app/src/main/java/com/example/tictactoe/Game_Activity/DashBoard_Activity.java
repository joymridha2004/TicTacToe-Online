package com.example.tictactoe.Game_Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tictactoe.Login_System_Activity.Sign_In_Activity;
import com.example.tictactoe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class DashBoard_Activity extends AppCompatActivity {
    private Button LogoutButton;
    private FirebaseAuth mAuth;
    private TextView UserNameTV, UserEmailTV;
    private FirebaseFirestore fStore;
    private Button PlayWithFriendButton;

    private String userUid;
    private FirebaseFirestore db;

    private TextView DashBoardActivityPlayerName1st;
    private TextView DashBoardActivityPlayerName2nd;
    private TextView DashBoardActivityPlayer1stScore;
    private TextView DashBoardActivityPlayer2ndScore;
    private TextView DashBoardActivityMatchDate;
    private TextView DashBoardActivityMatchTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        LogoutButton = findViewById(R.id.Logout_Button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserNameTV = findViewById(R.id.User_Name_TV);
        UserEmailTV = findViewById(R.id.User_Email_TV);
        PlayWithFriendButton = findViewById(R.id.Play_With_Friend_Button);
        DashBoardActivityPlayerName1st = findViewById(R.id.DashBoard_Activity_Player_Name_1st);
        DashBoardActivityPlayerName2nd = findViewById(R.id.DashBoard_Activity_Player_Name_2nd);
        DashBoardActivityPlayer1stScore = findViewById(R.id.DashBoard_Activity_Player_1st_Score);
        DashBoardActivityPlayer2ndScore = findViewById(R.id.DashBoard_Activity_Player_2nd_Score);
        DashBoardActivityMatchDate = findViewById(R.id.DashBoard_Activity_Match_Date);
        DashBoardActivityMatchTime = findViewById(R.id.DashBoard_Activity_Match_Time);

        userUid = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userUid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    UserNameTV.setText(value.getString("userName"));
                    UserEmailTV.setText(value.getString("emailId"));
                }
            }
        });

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SharedPreferences sharedPreferences = getSharedPreferences(Sign_In_Activity.PREFS_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
                Intent intent = new Intent(DashBoard_Activity.this, Sign_In_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        PlayWithFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard_Activity.this, Players_Details_Activity.class);
                startActivity(intent);
            }
        });

        fetchRecentMatchData();

    }

    private void fetchRecentMatchData() {
        db.collection("matchDetails")
                .document(mAuth.getCurrentUser().getUid())
                .collection("matchId")
                .orderBy("matchTime", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            // Handle the data

                            String matchTime = documentSnapshot.getString("matchTime");
                            String matchDate = documentSnapshot.getString("matchDate");
                            String firstPlayerName = documentSnapshot.getString("1stPlayerName");
                            String secondPlayerName = documentSnapshot.getString("2ndPlayerName");
                            long firstPlayerScore = documentSnapshot.getLong("1stPlayerScore");
                            long secondPlayerScore = documentSnapshot.getLong("2ndPlayerScore");

                            DashBoardActivityPlayerName1st.setText(firstPlayerName);
                            DashBoardActivityPlayerName2nd.setText(secondPlayerName);
                            DashBoardActivityPlayer1stScore.setText(String.valueOf(firstPlayerScore));
                            DashBoardActivityPlayer2ndScore.setText(String.valueOf(secondPlayerScore));
                            DashBoardActivityMatchDate.setText(matchDate);
                            DashBoardActivityMatchTime.setText(matchTime);

                            // Handle other data as needed

                            Log.d(TAG, "Match Time: " + matchTime);
                            Log.d(TAG, "Match Date: " + matchDate);
                            Log.d(TAG, "1st Player Name: " + firstPlayerName);
                            Log.d(TAG, "2nd Player Name: " + secondPlayerName);
                            Log.d(TAG, "1st Player Score: " + firstPlayerScore);
                            Log.d(TAG, "2nd Player Score: " + secondPlayerScore);

                        }
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
}