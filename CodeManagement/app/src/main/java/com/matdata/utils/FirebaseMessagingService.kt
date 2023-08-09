
package com.matdata.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.matdata.BuildConfig
import com.matdata.R
import com.matdata.restfulClient.api.extension.log
import com.matdata.ui.activity.MainActivity

class FirebaseMessagingService : FirebaseMessagingService() {
    private val prefStore = PrefStore(this)

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        prefStore.saveString(Const.DEVICE_TOKEN, refreshedToken)
    }




    private fun displayMessage(context: Context, extras: Bundle?) {
        val intent = Intent(Const.DISPLAY_MESSAGE_ACTION)
        intent.putExtra("detail", extras)
        context.sendBroadcast(intent)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        log("test", data["userName"]!!)
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "MyFcmListenerService :$data")
        }


        val bundle = Bundle()
        for ((key, value) in remoteMessage.data) {
            bundle.putString(key, value)
        }

        if (remoteMessage.data.isNotEmpty()) {
        }
        displayMessage(this, bundle)
        generateNotification(this, bundle)
    }

    private fun inForeground(): Boolean {
        return prefStore.getBoolean(Const.FOREGROUND)
    }


    @SuppressLint("InvalidWakeLockTag")
    private fun generateNotification(context: Context, extra: Bundle) {
        val action = extra["action"] as String?


        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_notifications)
        val mBuilder = NotificationCompat.Builder(context, PRIMARY_CHANNEL)
                .setSmallIcon(R.drawable.ic_notifications)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(largeIcon)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentTitle(extra.get("userName") as String)
                .setContentText(extra.get("message") as String)


        val NotiSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mBuilder.setSound(NotiSound)
        val vibrate = longArrayOf(0, 100, 200, 300)
        mBuilder.setVibrate(vibrate)
        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.action = Intent.ACTION_MAIN
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        resultIntent.putExtra("detail", extra)
        resultIntent.putExtra("isNotFromOpenTok", true)
        val resultPendingIntent = PendingIntent.getActivity(applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setWhen(System.currentTimeMillis())
        mBuilder.setContentIntent(resultPendingIntent)
        val mPowerManager = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        var wl: PowerManager.WakeLock? = null
        wl = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag")
        wl?.acquire(1000)
        val mNotificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val chan1 = NotificationChannel(PRIMARY_CHANNEL,
                    "NeverMynd", NotificationManager.IMPORTANCE_DEFAULT)
            chan1.lightColor = Color.GREEN
            chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            chan1.setShowBadge(true)
            mNotificationManager.createNotificationChannel(chan1)
        }
        //Important. Do not skip this. This notifies the old notification and updates it with the new one with new message list else creates a new one if it doesn't exist
        val notification = mBuilder.build()
        mNotificationManager.notify(0, notification)

    }


    companion object {
        const val PRIMARY_CHANNEL = "default"
        private const val TAG = "NotificationService"
    }
}