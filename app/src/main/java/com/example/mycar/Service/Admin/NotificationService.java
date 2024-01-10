package com.example.mycar.Service.Admin;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.mycar.Prevalent.Orders;
import com.example.mycar.Prevalent.Prevalent;
import com.example.mycar.R;
import com.example.mycar.ui.Users.OrdersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.RemoteMessage;
//import com.google.firebase.messaging.RemoteMessageCreator;

import java.util.UUID;

public class NotificationService extends Service {

    private Notification notification;

    @Override
    public void onCreate() {
        lisnenerCountorder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected final DatabaseReference incrementinfo = FirebaseDatabase.getInstance().getReference("num_newOrders");

    private ValueEventListener listenerNumberic;

    public void lisnenerCountorder(){
        listenerNumberic = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {/*
                if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {*/
                int curNumber = snapshot.getValue(Integer.class);
                if (curNumber > 0)
                    createNotification(curNumber);
                // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        incrementinfo.addValueEventListener(listenerNumberic);
    }

    @Override
    public void onDestroy() {
        incrementinfo.removeEventListener(listenerNumberic);
        super.onDestroy();
    }

    public void createNotification(int number) {

        if(Prevalent.currentOnLineUser.getActivity() != null) {

            createNotificationChannel();

            Intent intent
                    = new Intent(Prevalent.currentOnLineUser.getActivity(), OrdersActivity.class);
            // Assign channel ID
            String channel_id = "channelId";
            PendingIntent pendingIntent
                    = PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Что-то новое")
                    .setContentText(generateMessage(number))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            startForeground(1, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //O = Oreo, not zero
            String channelName = "notification_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            //Create the NotificationChannel object
            NotificationChannel channel = new NotificationChannel(
                    "channelId",
                    channelName,
                    importance);

            //Retrieve the NotificationManager from the system.
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //Registers the channel with NotificationManager
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private boolean isNotificationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PERMISSION_GRANTED;
    }

    public String generateMessage(int number) {
        String message;

        if (number == 1) {
            message = "У вас 1 новый заказ";
        } else if (number % 10 == 1 && number != 11) {
            message = String.format("У вас %d новый заказ", number);
        } else if (2 <= number % 10 && number % 10 <= 4 && (number < 10 || number >= 20)) {
            message = String.format("У вас %d новых заказа", number);
        } else {
            message = String.format("У вас %d новых заказов", number);
        }

        return message;
    }
}
