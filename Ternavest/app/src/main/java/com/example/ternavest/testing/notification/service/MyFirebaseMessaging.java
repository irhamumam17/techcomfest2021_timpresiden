package com.example.ternavest.testing.notification.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.ternavest.R;
import com.example.ternavest.testing.notification.model.Token;
import com.example.ternavest.ui.both.welcome.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

// Kelas ini menangani penerimaan notifikasi
public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessaging.class.getSimpleName();
    public static final int REQUEST_PEMINAT_BARU_NOTIFICATION = 100;

    /* Kelas ini merupakan kelas service yang berjalan di latar belakang
    * mendeteksi setiap ada notifikasi baru yang diterima*/
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage remoteMessage) {
        String CHANNEL_ID = "channel_01";
        String CHANNEL_NAME = "Peminat baru";

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_PEMINAT_BARU_NOTIFICATION, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_logo_notif)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(this, android.R.color.transparent))
                .setSound(defaultSoundUri)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = builder.build();
        if (notificationManager != null) notificationManager.notify(REQUEST_PEMINAT_BARU_NOTIFICATION, notification);
    }

    @Override
    // Menangani setiap ada perubahan token (identifier dari perangkat tertentu)
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        Log.d(TAG, "New token: " + newToken);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            /* Kondisi yang tidak akan tercapai karena fungsi ini dipanggil satu kali
            saat pertama kali buka aplikasi dan belum login akun*/
            updateToken(newToken);
        }
    }

    // Simpan dan perbarui token baru ke database
    public static void updateToken(String newToken) {
        Log.d(TAG, "updateToken() called: " + newToken);
        /* Token ini dikueri saat akan mengirimkan notifikasi
        sebagai penerima notifikasi*/
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("token");
        Token token = new Token(newToken);
        databaseReference.child(firebaseUser.getUid()).setValue(token);
    }

    /*
        // Dapatkan token untuk notifikasi
        FirebaseInstallations.getInstance().getToken(true).addOnSuccessListener(new OnSuccessListener<InstallationTokenResult>() {
            @Override
            public void onSuccess(InstallationTokenResult installationTokenResult) {
                String newToken = installationTokenResult.getToken();
                updateToken(newToken);
            }
        });
    */

    /*
    sendNotification(project.getUuid(), "Peminat baru", project.getNamaProyek() + " mendapat peminat baru");
    private void sendNotification(String receiverId, final String title, final String message) {
        Log.d(TAG, "sendNotification() called");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("token");
        // Muat token si penerima berdasarkan id pengguna
        Query query = reference.orderByKey().equalTo(receiverId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "sendNotification() called: success get receiver token");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Notification notification = new Notification(title, message);

                    Sender sender = new Sender(notification, token.getToken());

                    ApiService apiService = ApiClient.getClient("https://fcm.googleapis.com/").create(ApiService.class);
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Log.d(TAG, "sendNotification() called: failed send notification");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {}
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    */
}
