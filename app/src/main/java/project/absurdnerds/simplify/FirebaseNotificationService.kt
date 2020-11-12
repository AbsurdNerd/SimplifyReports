package project.absurdnerds.simplify

import android.content.ContentResolver
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber.e


class FirebaseNotificationService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        e("Notification Received")
        if (message.notification != null) {
            val title = message.notification!!.title.toString()
            val body = message.notification!!.body.toString()
//            val id = message.notification!!.imageUrl

            e(title)
            e(body)


            val CHANNEL_ID = "101"
            val vibrate = longArrayOf(0, 400)
            var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSound(Uri.parse("android.resource://"
                            + applicationContext.packageName + "/" + R.raw.pikachu_scream))
                    .setVibrate(vibrate)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

//            Toast.makeText(applicationContext,title, Toast.LENGTH_SHORT).show()
//            val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                    + "://" + applicationContext.packageName + "/raw/pikachu_scream")
//            val r = RingtoneManager.getRingtone(applicationContext, alarmSound)
//            r.play()

            var notificationManager = NotificationManagerCompat.from(applicationContext)
            notificationManager.notify(1, builder.build())
        }
    }



}