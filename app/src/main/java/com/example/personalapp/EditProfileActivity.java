package com.example.personalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DBHelper sqDbHelper;
    private DocumentReference docRef;
    private int REQUEST_IMAGE_CAPTURE = 100;
    ImageView profPicView;
    ImageButton pictureBtn, backBtn;
    Button saveBtn;
    EditText usernameTxt;
    private String username = "";
    private String email = "";
    private ProgressDialog progressDialog;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        //firebase authentication and retrieve user information
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        email = user.getEmail();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("User information").document(email);

        //Retrieve and display profile picture
        profPicView = (ImageView) findViewById(R.id.profilePicture);
        if(user.getPhotoUrl() != null){
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(profPicView);
        }

        //Retrieve and display username
        usernameTxt = (EditText) findViewById(R.id.editUsername);
        getUsername();

        progressDialog = new ProgressDialog(this);
        backBtn = (ImageButton) findViewById(R.id.backToSettingsBtn2);
        pictureBtn = (ImageButton) findViewById(R.id.pictureBtn);
        saveBtn = (Button) findViewById(R.id.saveUsernameBtn);

        usernameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String usernameInput = usernameTxt.getText().toString().trim();
                saveBtn.setEnabled(!usernameInput.isEmpty());
                if (saveBtn.isEnabled()){
                    saveBtn.setBackgroundColor(Color.WHITE);
                    saveBtn.setTextColor(Color.BLACK);
                }
                else if (!saveBtn.isEnabled()){
                    saveBtn.setBackgroundColor(EditProfileActivity.this.getResources().getColor(R.color.opacitywhite));
                    saveBtn.setTextColor(Color.rgb(170, 170, 170));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getUsername(){
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            username = documentSnapshot.getString("username");
                            usernameTxt.setText(username);
                            usernameTxt.setSelection(usernameTxt.getText().length());
                        }
                        else{
                            Toast.makeText(EditProfileActivity.this, "Information does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void takePictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            switch(resultCode){
                case RESULT_OK:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    profPicView.setImageBitmap(bitmap);
            }
        }
    }

    private void uploadImage(Bitmap bitmap){
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        String id = firebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImgs")
                .child(id + ".jpeg");

        reference.putBytes(outStream.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ProfilePictureActivity", "Failed: ", e.getCause());
            }
        });
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("ProfilePictureActivity", "Success: " + uri);
                setUserProfileUrl(uri);
            }
        });
    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ProfilePictureActivity", "Profile image update successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Profile image update failed...", Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
        Toast.makeText(EditProfileActivity.this, "Update successful.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v == pictureBtn){
            takePictureIntent();
        }
        if(v == saveBtn){
            try{
                saveUsername();
                if(bitmap != null){
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();
                    uploadImage(bitmap);
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Update successful.", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                Toast.makeText(EditProfileActivity.this, "Update unsuccessful.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == backBtn){
            Intent j = new Intent(EditProfileActivity.this, SettingsActivity.class);
            startActivity(j);
        }
    }

    public void saveUsername(){
        usernameTxt.onEditorAction(EditorInfo.IME_ACTION_DONE);
        String newUsername = usernameTxt.getText().toString();
        db.collection("User information").document(email).update("username", newUsername);
    }
}
