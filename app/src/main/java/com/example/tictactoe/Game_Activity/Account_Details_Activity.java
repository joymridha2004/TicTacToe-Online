package com.example.tictactoe.Game_Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Account_Details_Activity extends AppCompatActivity {
    private Boolean editFlag = false;
    private Toolbar accountDetailsToolbar;
    private TextView viewUserNameTV, viewUserEmailTV;
    private EditText editUserNameTV, editUserEmailTV;
    private ImageView userPhotoIV, editIV;
    private Uri uri;
    private Button profileDetailsDataSaveButton;
    private Drawable close, edit;
    private FirebaseFirestore fStore;
    private String userUid;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        accountDetailsToolbar = findViewById(R.id.Account_Details_Toolbar);
        viewUserNameTV = findViewById(R.id.View_User_Name_TV);
        viewUserEmailTV = findViewById(R.id.View_User_Email_TV);
        userPhotoIV = findViewById(R.id.User_Photo_IV);
        editIV = findViewById(R.id.Edit_IV);
        editUserNameTV = findViewById(R.id.Edit_User_Name_TV);
        editUserEmailTV = findViewById(R.id.Edit_User_Email_TV);
        profileDetailsDataSaveButton = findViewById(R.id.Profile_Details_Data_Save_Button);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();

        close = getResources().getDrawable(R.drawable.close_svg);
        edit = getResources().getDrawable(R.drawable.edit_icon);

        accountDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editFlag) {
                    whenEditFlagFalse();
                } else {
                    whenEditFlagTrue();
                }
            }
        });

        profileDetailsDataSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whenEditFlagTrue();
                updateProfileDetails();
            }
        });

        userPhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editFlag) {
                    ImagePicker.with(Account_Details_Activity.this)
                            .cropSquare()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                }
            }
        });

        fetchPersonalDetails();

    }

    @Override
    protected void onResume() {
        fetchPersonalDetails();
        super.onResume();
    }

    private void updateProfileDetails() {
        String updateName = String.valueOf(editUserNameTV.getText());
        String updateEmail = String.valueOf(editUserEmailTV.getText());

        if (!updateName.isEmpty() && !updateEmail.isEmpty()) {
            DocumentReference documentReference = fStore.collection("users").document(userUid);

            Map<String, Object> updates = new HashMap<>();
            updates.put("userName", updateName);
            updates.put("emailId", updateEmail);

            if (uri != null) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    String avatarBase64 = convertBitmapToBase64ToUpdate(bitmap);
                    if (avatarBase64 != null) {
                        updates.put("avatar", avatarBase64);
                    }
                }
            }

            documentReference.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Account_Details_Activity.this, "Profile details updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Account_Details_Activity.this, "Failed to update profile details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertBitmapToBase64ToUpdate(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void whenEditFlagTrue() {
        editIV.setImageDrawable(edit);
        viewUserNameTV.setVisibility(View.VISIBLE);
        viewUserEmailTV.setVisibility(View.VISIBLE);
        editUserNameTV.setVisibility(View.GONE);
        editUserEmailTV.setVisibility(View.GONE);
        profileDetailsDataSaveButton.setVisibility(View.GONE);
        editFlag = false;
    }

    private void whenEditFlagFalse() {
        editIV.setImageDrawable(close);
        viewUserNameTV.setVisibility(View.GONE);
        viewUserEmailTV.setVisibility(View.GONE);
        editUserNameTV.setVisibility(View.VISIBLE);
        editUserEmailTV.setVisibility(View.VISIBLE);
        profileDetailsDataSaveButton.setVisibility(View.VISIBLE);
        editFlag = true;
    }

    private void fetchPersonalDetails() {

        DocumentReference documentReference = fStore.collection("users").document(userUid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                viewUserNameTV.setText(value.getString("userName"));
                viewUserEmailTV.setText(value.getString("emailId"));
                editUserNameTV.setText(value.getString("userName"));
                editUserEmailTV.setText(value.getString("emailId"));
                String avatarBase64 = value.getString("avatar");
                if (avatarBase64 != null) {
                    Bitmap avatarBitmap = convertBase64ToBitmap(avatarBase64);
                    if (avatarBitmap != null) {
                        userPhotoIV.setImageBitmap(avatarBitmap);
                    }
                }
            }
        });

    }

    private Bitmap convertBase64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            uri = data.getData();
            userPhotoIV.setImageURI(uri);
            whenEditFlagFalse();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            whenEditFlagFalse();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            whenEditFlagFalse();
        }
    }

}