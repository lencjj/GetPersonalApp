package com.example.personalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DBHelper sqDbHelper;
    private DocumentReference docRef;
    private DrawerLayout drawerLayout;
    private TextView scheduleNotificaion;
    private TextView displayUName;
    private MenuItem logoutItem;
    private NavigationView navigationView;
    private String username = "";
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    DisplayMetrics metrics = new DisplayMetrics();
    public static int dpi;
    private ImageView profPicView;
    private int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            Intent j = new Intent(this, LoginActivity.class);
            startActivity(j);

        }

        //firebase authentication and retrieve user information
        FirebaseUser user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("User information").document(user.getEmail());

        //Set up schedule notification
        scheduleNotificaion = (TextView) findViewById(R.id.scheduleNotification);
        Calendar calendar = Calendar.getInstance();
        String strDate = eventDateFormat.format(calendar.getTime());
        int numOfEventsTdy = getNotification(strDate);
        if(numOfEventsTdy == 0){
            scheduleNotificaion.setText("No event or activities planned for today");
        }
        else if (numOfEventsTdy == 1){
            scheduleNotificaion.setText("1 event planned for today");
        } else{
            scheduleNotificaion.setText(numOfEventsTdy + " events planned for today");
        }

        //get dpi of the screen
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dpi = metrics.densityDpi;

        //Handle logout button
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu menu = (Menu) navigationView.getMenu();
        logoutItem = (MenuItem) menu.getItem(1);
        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                firebaseAuth.signOut();
                finish();
                Intent j = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(j);
                return true;
            }
        });

        //Retrieve and display username
        View view = (View) navigationView.getHeaderView(0);
        displayUName = (TextView) view.findViewById(R.id.username);
        getUsername();


        //Handle profile picture activities
        profPicView = (ImageView) view.findViewById(R.id.displayPicture);
        if(user.getPhotoUrl() != null){
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(profPicView);
        }
        ImageButton pictureBtn = (ImageButton) view.findViewById(R.id.pictureBtn);
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent();
            }
        });

    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scheduleBtn:
                Intent i = new Intent(this, CalendarWithEvent.class);
                startActivity(i);
                break;
            case R.id.menuBtn:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void getUsername(){
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            username = documentSnapshot.getString("username");
                            displayUName.setText(username);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Information does not exist", Toast.LENGTH_SHORT);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT);
                    }
                });
    }

    private int getNotification(String date){
        int i = 0;
        sqDbHelper = new DBHelper(MainActivity.this);
        SQLiteDatabase database = sqDbHelper.getReadableDatabase();
        Cursor cursor = sqDbHelper.ReadEvents(date, database);
        while (cursor.moveToNext()){
           i++;
        }
        cursor.close();
        sqDbHelper.close();

        return i;
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
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profPicView.setImageBitmap(bitmap);
                    uploadImage(bitmap);

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
                Log.d("ProfilePictureActivity", "Sucess: " + uri);
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
                        Toast.makeText(MainActivity.this, "Profile image update success..", Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Profile image update failed...", Toast.LENGTH_SHORT);
            }
        });
    }
}
