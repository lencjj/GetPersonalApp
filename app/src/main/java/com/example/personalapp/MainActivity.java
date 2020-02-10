package com.example.personalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalapp.ArchitectureComponents.FinanceDao;
import com.example.personalapp.ArchitectureComponents.FinanceViewModel;
import com.example.personalapp.Entity.Finance;
import com.example.personalapp.JournalEntry.MainActivity_JournalEntry;
import com.example.personalapp.MoneyManagement.FinanceReminderBroadcast;
import com.example.personalapp.MoneyManagement.MainActivity_Finance;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

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
    private TextView scheduleNotificaion, financeNotification;
    private TextView displayUName;
    private MenuItem aboutItem, settingsItem, logoutItem;
    private NavigationView navigationView;
    private String username = "";
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    DisplayMetrics metrics = new DisplayMetrics();
    public static int dpi;

    // Finance checking
    private FinanceViewModel financeViewModel;
    private Finance finance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finance checking
        financeViewModel = ViewModelProviders
                .of(this)
                .get(FinanceViewModel.class);
        financeReminderCheck();
        financeNotification = findViewById(R.id.financeNotification);
        finance = new Finance();
        financeViewModel.getLastRecord().observe(this, new Observer<Finance>() {
            @Override
            public void onChanged(Finance lastFinance) {
                finance.setDate(lastFinance.getDate());
                finance.setTime(lastFinance.getTime());
                financeNotification.setText("Your last record was on " + finance.getDate() + " " + finance.getTime());
            }
        });



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

        //get height of the non grid view items

        dpi = metrics.densityDpi;




        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu menu = (Menu) navigationView.getMenu();

        aboutItem = (MenuItem) menu.getItem(0);
        settingsItem = (MenuItem) menu.getItem(1);
        logoutItem = (MenuItem) menu.getItem(2);
        View view = (View) navigationView.getHeaderView(0);
        displayUName = (TextView) view.findViewById(R.id.username);
        getUsername();


        aboutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent1);
                return true;
            }
        });
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });
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
            case R.id.financeBtn:
                Intent i2 = new Intent(this, MainActivity_Finance.class);
                startActivity(i2);
                break;
            case R.id.noteBtn:
                Intent i3 = new Intent(this, MainActivity_JournalEntry.class);
                startActivity(i3);
                break;
        }
    }

    public void getUsername(){
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

    public int getNotification(String date){
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


    // Finance checking
    public void financeReminderCheck() {
        financeViewModel.getTotalExpensesByToday().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble == null) {
                    // Notification ------------------------------------------------------------------------------------------------------------------------------------

                    Intent intent = new Intent(MainActivity.this, FinanceReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    long timeNow = System.currentTimeMillis();
                    long tenSecondsInMills = 1000 * 7;

                    alarmManager.set(AlarmManager.RTC_WAKEUP, (timeNow + tenSecondsInMills), pendingIntent);

//                    financeNotification.setText("Your last record was on " + finance.getDate());
                } else {
                    financeNotification.setText("You have recorded your expense today! :D");
                }
            }
        });
    }


}
