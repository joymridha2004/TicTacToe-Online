package com.example.tictactoe.Game_Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tictactoe.Login_System_Activity.Sign_In_Activity;
import com.example.tictactoe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DashBoard_Activity extends AppCompatActivity {
    private Button LogoutButton;
    private FirebaseAuth mAuth;
    private TextView UserNameTV, UserEmailTV;
    private FirebaseFirestore fStore;
    private Button PlayWithFriendButton;

    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        LogoutButton = findViewById(R.id.Logout_Button);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserNameTV = findViewById(R.id.User_Name_TV);
        UserEmailTV = findViewById(R.id.User_Email_TV);
        PlayWithFriendButton = findViewById(R.id.Play_With_Friend_Button);

        userUid = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userUid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                UserNameTV.setText(value.getString("userName"));
                UserEmailTV.setText(value.getString("emailId"));
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
                finish();
            }
        });

    }
}