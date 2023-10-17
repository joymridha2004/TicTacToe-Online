package com.example.tictactoe.Game_Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private CardView ProfileDetailsCV;
    private FirebaseAuth mAuth;
    private TextView UserNameTV, UserEmailTV;
    private FirebaseFirestore fStore;
    private Button PlayWithFriendButton, PlayWithAIButton;
    private String userUid;
    private FirebaseFirestore db;
    private TextView DashBoardActivityPlayerName1st;
    private TextView DashBoardActivityPlayerName2nd;
    private TextView DashBoardActivityPlayer1stScore;
    private TextView DashBoardActivityPlayer2ndScore;
    private TextView DashBoardActivityMatchDate;
    private TextView DashBoardActivityMatchTime;
    private TextView DashBoardActivityMatchCount;
    private TextView TextView1, TextView, Project_Link;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        /*<------------Night mode disable--------->*/

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /*<------------Hooks--------->*/

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
        TextView1 = findViewById(R.id.TextView1);
        TextView = findViewById(R.id.TextView);
        userUid = mAuth.getCurrentUser().getUid();
        Project_Link = findViewById(R.id.Project_Link);
        PlayWithAIButton = findViewById(R.id.Play_With_AI_Button);
        ProfileDetailsCV = findViewById(R.id.Profile_Details_CV);
        DashBoardActivityMatchCount = findViewById(R.id.DashBoard_Activity_Match_Count);

        /*<------------Handle_Personal_Details_TextView--------->*/

        DocumentReference documentReference = fStore.collection("users").document(userUid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    UserNameTV.setText(value.getString("userName"));
                    UserEmailTV.setText(value.getString("emailId"));

                    fetchRecentMatchData();
                }
            }
        });

        /*<------------Handle_Github_link_On_click_Listener--------->*/

        Project_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joymridha2004/TicTacToe-Online"));
                startActivity(intent);
                overridePendingTransition(R.anim.from_right, R.anim.out_from_left);
            }
        });

        /*<------------Handle_LogOut_On_click_Listener--------->*/

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
                overridePendingTransition(R.anim.from_left, R.anim.out_from_right);
                finish();
            }
        });

        /*<------------Handle_Play_With_Friend_On_click_Listener--------->*/

        PlayWithFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard_Activity.this, Players_Details_Activity.class);
                overridePendingTransition(R.anim.from_right, R.anim.out_from_left);
                startActivity(intent);
                finish();
            }
        });

        /*<------------Handle_Play_With_Ai_On_click_Listener--------->*/

        PlayWithAIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashBoard_Activity.this, "This Feature is available for Task 2", Toast.LENGTH_SHORT).show();
            }
        });

        /*<------------Handle_Personal_Details_On_click_Listener--------->*/

        ProfileDetailsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashBoard_Activity.this, "This Feature is available for Task 2", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchRecentMatchData() {

        /*<------------Handle_Fetch_CurrentUser_MatchDetails_Data_From_FireBase--------->*/

        db.collection("matchDetails")
                .document(mAuth.getCurrentUser().getUid())
                .collection("matchId")
                .orderBy("matchIndex", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            DashBoardActivityPlayerName1st.setText(documentSnapshot.getString("1stPlayerName"));
                            DashBoardActivityPlayerName2nd.setText(documentSnapshot.getString("2ndPlayerName"));
                            DashBoardActivityPlayer1stScore.setText(String.valueOf(documentSnapshot.getLong("1stPlayerScore")));
                            DashBoardActivityPlayer2ndScore.setText(String.valueOf(documentSnapshot.getLong("2ndPlayerScore")));
                            DashBoardActivityMatchDate.setText(documentSnapshot.getString("matchDate"));
                            DashBoardActivityMatchTime.setText(documentSnapshot.getString("matchTime"));
                            DashBoardActivityMatchCount.setText("MP: " + String.valueOf(documentSnapshot.getLong("matchIndex") + 1));
                            TextView1.setText(getString(R.string.vs));
                            TextView.setText("/");

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