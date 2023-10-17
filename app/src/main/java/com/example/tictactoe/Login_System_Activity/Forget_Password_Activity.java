package com.example.tictactoe.Login_System_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tictactoe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_Password_Activity extends AppCompatActivity {

    private TextView back;
    private Drawable errorIcon;
    private EditText email;
    private ProgressBar resetPasswordProgress;
    private TextView responseMassage;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;
    private TextView Project_Link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        /*<------------Night mode disable--------->*/

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /*<------------Hooks--------->*/

        back = findViewById(R.id.Forget_Password_Activity_Go_Back_TV);
        errorIcon = getResources().getDrawable(R.drawable.ic_error);
        email = findViewById(R.id.Forget_Password_Activity_Email_ET);
        resetPasswordProgress = findViewById(R.id.Forget_Password_ProgressBar);
        responseMassage = findViewById(R.id.responseMessage);
        resetPasswordButton = findViewById(R.id.Forget_Password_Activity_Forget_Password_Button);
        mAuth = FirebaseAuth.getInstance();
        Project_Link = findViewById(R.id.Project_Link);

        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        /*<------------Handle_Github_link_On_click_Listener--------->*/

        Project_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/joymridha2004/TicTacToe-Online"));
                overridePendingTransition(R.anim.from_right, R.anim.out_from_left);
                startActivity(intent);
            }
        });

        /*<------------Handle_Go_To_SignIn_On_click_Listener--------->*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forget_Password_Activity.this, Sign_In_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_left, R.anim.out_from_right);
                finish();
            }
        });

        /*<------------Handle_Reset_Password_Function_On_click_Listener--------->*/

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        /*<------------Handle_Reset_Password_Response_Message_clear_after_any_changes_on_EditText_On_click_Listener--------->*/

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                responseMassage.setText(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void resetPassword() {

        /*<------------Handle_EditText_nullPoint_and_other_validations--------->*/

        if (email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            resetPasswordProgress.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        responseMassage.setTextColor(getResources().getColor(R.color.green));
                        responseMassage.setText("Check your Email.");
                    } else {
                        responseMassage.setTextColor(getResources().getColor(R.color.red));
                        responseMassage.setText("There is an issue sending Email.");
                    }
                    resetPasswordProgress.setVisibility(View.INVISIBLE);
                    responseMassage.setVisibility(View.VISIBLE);
                }
            });
        } else {
            email.setError("Invalid Email Pattern!", errorIcon);
        }

    }

}