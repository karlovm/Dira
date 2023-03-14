package ru.dira.notifications;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ru.dira.R;
import ru.dira.activities.RoomActivity;
import ru.dira.activities.RoomSelectorActivity;
import ru.dira.attachments.ImageStorage;
import ru.dira.attachments.ImagesWorker;
import ru.dira.db.DiraRoomDatabase;
import ru.dira.db.entities.Message;
import ru.dira.db.entities.Room;

public class Notifier {

    public static String DIRA_ID = "Dira";

    private static int notificationId = 1;

    public static void notifyMessage(Message message, Context context)
    {
        createNotificationChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, DIRA_ID)
                        .setSmallIcon(R.drawable.logo_solid)
                        .setContentTitle(message.getAuthorNickname())
                        .setContentText(message.getText())
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        Room room = DiraRoomDatabase.getDatabase(context).getRoomDao().getRoomBySecretName(message.getRoomSecret());
        if(room != null)
        {
            builder.setContentTitle(room.getName());
            Bitmap bitmap = ImagesWorker.getCircleCroppedBitmap(ImageStorage.getImage(room.getImagePath()), 256, 256);
            if(bitmap != null)
            {
                builder.setLargeIcon(bitmap);
            }
            builder.setContentText(message.getAuthorNickname() + ": " + message.getText());
            Intent notificationIntent = new Intent(context, RoomActivity.class);

            RoomActivity.pendingRoomSecret = room.getSecretName();
            RoomActivity.pendingRoomName = room.getName();

            PendingIntent intent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            }
            else
            {
                intent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);
            }


            builder.setContentIntent(intent);
        }

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }


    public static void cancelAllNotifications(Context context)
    {
        NotificationManagerCompat.from(context).cancelAll();
    }

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Dira";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(DIRA_ID, name, importance);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
