package com.example.smb2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationService : Service() {


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        notify(this, intent)
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    private fun notify(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(
            context,
            "Dodano ${intent.getStringExtra("name")}",
            Toast.LENGTH_SHORT
        ).show()
        //Toast.makeText(context, intent.getStringExtra("name"),Toast.LENGTH_SHORT).show()
        //Tworzymy kanał notyfikacji
        val channelid = createChannel(context)

        //Tworzymy przepustkę
        val addItemIntent = Intent().also {
            it.component = ComponentName("com.example.SMB", "com.example.SMB.MainActivity")
            it.putExtra("name", intent.getStringExtra("name"))
        }
        //addItemIntent.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }

        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            addItemIntent,
            PendingIntent.FLAG_MUTABLE //or PendingIntent.FLAG_UPDATE_CURRENT

        )

        //Tworzymy notyfikację
        val notification = NotificationCompat.Builder(
            context,
            channelid
        ).setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Item added:")
            .setContentText(intent.getStringExtra("name"))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        //Pokazujemy notyfikację
        NotificationManagerCompat.from(context).notify(0, notification)
    }


    private fun createChannel(context: Context): String {
        val id = "ItemAddChannel"
        val channel = NotificationChannel(
            id,
            "Item Add Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
        return id
    }

}