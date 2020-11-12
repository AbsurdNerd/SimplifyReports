package project.absurdnerds.simplify

import android.util.Log
import android.util.Log.e
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import timber.log.Timber.e

class FirebaseNotificationService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.notification != null) {
            val title = message.notification!!.title.toString()
            val body = message.notification!!.body.toString()

            e(title)
            e(body)


            val CHANNEL_ID = "101"
            var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

//            Toast.makeText(applicationContext,title, Toast.LENGTH_SHORT).show()

            var notificationManager = NotificationManagerCompat.from(applicationContext)
            notificationManager.notify(1, builder.build())
        }
    }

}