package com.example.tictactoe.Login_System_Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.Game_Activity.DashBoard_Activity;
import com.example.tictactoe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Sign_In_Activity extends AppCompatActivity {

    public static String PREFS_NAME = "MyPrefsFile";
    private TextView dontHaveAnAccount, Project_Link;
    private TextView resetPassword;
    private Drawable errorIcon;
    private EditText email;
    private EditText password;
    private Button signInButton;
    private ProgressBar signInProgress;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userUid;
    private String nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /*<------------Hooks--------->*/

        dontHaveAnAccount = findViewById(R.id.Sign_In_Activity_Sign_Up_TV);
        resetPassword = findViewById(R.id.Sign_In_Forget_Password_TV);
        errorIcon = getResources().getDrawable(R.drawable.ic_error);
        email = findViewById(R.id.Sign_In_Activity_Email_ET);
        password = findViewById(R.id.Sign_In_Activity_Password_ET);
        signInButton = findViewById(R.id.Sign_In_Login_Button);
        signInProgress = findViewById(R.id.Sign_in_Progress);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Project_Link = findViewById(R.id.Project_Link);

        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        /*<------------Handle_Github_link_On_click_Listener--------->*/

        Project_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joymridha2004/TicTacToe-Online"));
                startActivity(intent);
            }
        });

        /*<------------Handle_Go_To_SignUp_On_click_Listener--------->*/

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_In_Activity.this, Sign_Up_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        /*<------------Handle_Go_To_ForgetPassword_On_click_Listener--------->*/

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_In_Activity.this, Forget_Password_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        /*<------------Handle_Go_To_DashBoard_On_click_Listener--------->*/

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithFirebase();
            }
        });

    }

    private void signInWithFirebase() {

        /*<------------Handle_EditText_nullPoint_and_other_validations--------->*/

        if (email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            signInProgress.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            signInProgress.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    userUid = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(userUid);
                                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (error != null) {
                                                Log.e(TAG, "Listen failed: " + error);
                                                return;
                                            }

                                            if (value != null && value.exists()) {
                                                // Check if the "userName" field exists
                                                if (value.contains("userName")) {
                                                    nameText = value.getString("userName");
                                                    Toast.makeText(Sign_In_Activity.this, "Welcome: " + nameText, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.d(TAG, "Document doesn't contain 'userName' field");
                                                }
                                            } else {
                                                Log.d(TAG, "Current data: null");
                                            }
                                        }
                                    });
                                    SharedPreferences sharedPreferences = getSharedPreferences(Sign_In_Activity.PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("hasLoggedIn", true);
                                    editor.commit();
                                    Intent intent = new Intent(Sign_In_Activity.this, DashBoard_Activity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Sign_In_Activity.this, "Please verify your Email.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Sign_In_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            email.setError("Invalid Email Pattern!", errorIcon);
        }

    }

}