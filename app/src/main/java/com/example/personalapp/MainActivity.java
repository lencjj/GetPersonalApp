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
import com.example.personalapp.MoneyManagement.MainActivity_MoneyManagement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
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
    private MenuItem settingsItem;
    private NavigationView navigationView;
    private String username = "";
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    DisplayMetrics metrics = new DisplayMetrics();
    public static int dpi;
    private ImageView profPicView;

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

        settingsItem = (MenuItem) menu.getItem(0);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent j = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(j);
                return true;
            }
        });

        //Retrieve and display username
        View view = (View) navigationView.getHeaderView(0);
        displayUName = (TextView) view.findViewById(R.id.username);
        getUsername();


        //Load profile picture
        profPicView = (ImageView) view.findViewById(R.id.displayPicture);
        if(user.getPhotoUrl() != null){
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(profPicView);
        }

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
                // JingHui
            case R.id.financeBtn:
                Intent i1 = new Intent(this, MainActivity_MoneyManagement.class);
                startActivity(i1);
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

}
