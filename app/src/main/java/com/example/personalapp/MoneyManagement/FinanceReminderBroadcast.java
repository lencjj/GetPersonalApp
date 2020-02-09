package com.example.personalapp.MoneyManagement;
// JingHui's



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.personalapp.R;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;



public class FinanceReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "financeReminder")
                .setSmallIcon(R.drawable.ic_add_alert)
                .setContentTitle("Finance Reminder")
                .setContentText("You haven't recorded your expenses today!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200, builder.build());

    }
}
