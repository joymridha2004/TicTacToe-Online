package com.example.tictactoe.Login_System_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Sign_Up_Activity extends AppCompatActivity {

    private TextView alreadyHaveAnAccount, Project_Link;
    private Drawable errorIcon;
    private TextInputEditText userName;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar signUpProgress;
    private FirebaseFirestore db;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*<------------Night mode disable--------->*/

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /*<------------Hooks--------->*/

        alreadyHaveAnAccount = findViewById(R.id.Sign_Up_Activity_Sign_In_TV);
        errorIcon = getResources().getDrawable(R.drawable.ic_error);
        userName = findViewById(R.id.Sign_Up_Activity_User_Name_ET);
        email = findViewById(R.id.Sign_Up_Activity_Email_ET);
        password = findViewById(R.id.Sign_Up_Activity_Password_ET);
        confirmPassword = findViewById(R.id.Sign_Up_Activity_Confirm_Password_ET);
        signUpButton = findViewById(R.id.Sign_Up_Activity_Sign_Up_Button);
        mAuth = FirebaseAuth.getInstance();
        signUpProgress = findViewById(R.id.Sign_Up_Progress);
        db = FirebaseFirestore.getInstance();
        Project_Link = findViewById(R.id.Project_Link);

        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        /*<------------Handle_Github_link_On_click_Listener--------->*/

        Project_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joymridha2004/TicTacToe-Online"));
                startActivity(intent);
                overridePendingTransition(R.anim.from_right, R.anim.out_from_left);
            }
        });

        /*<------------Handle_Go_To_SignIn_On_click_Listener--------->*/

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up_Activity.this, Sign_In_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_left, R.anim.out_from_right);

                finish();
            }
        });

        /*<------------Handle_Go_To_SignIn_After_SignUP_On_click_Listener--------->*/

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpWithFirebase();
            }
        });

    }

    private void signUpWithFirebase() {

        /*<------------Handle_EditText_nullPoint_and_other_validations--------->*/

        if (email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                signUpProgress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                signUpProgress.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Sign_Up_Activity.this, "User registered successfully. Please verify your Email.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Sign_Up_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("userName", userName.getText().toString());
                                    user.put("emailId", email.getText().toString());
                                    String avatarBase64 = saveImageToInternalStorage();
                                    if (avatarBase64 != null) {
                                        user.put("avatar", avatarBase64);
                                    }
                                    db.collection("users")
                                            .document(task.getResult().getUser().getUid())
                                            .set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(Sign_Up_Activity.this, Sign_In_Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    overridePendingTransition(R.anim.from_left, R.anim.out_from_right);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Sign_Up_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(Sign_Up_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                confirmPassword.setError("Password Mismatch!", errorIcon);
            }
        } else {
            email.setError("Invalid Email Pattern!", errorIcon);
        }

    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String saveImageToInternalStorage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);

        if (bitmap != null) {
            return convertBitmapToBase64(bitmap);
        }
        return null;
    }

}