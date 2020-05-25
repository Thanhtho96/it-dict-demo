package com.tt.it_dictionary.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tt.it_dictionary.R
import com.tt.it_dictionary.WordRoomDatabase
import com.tt.it_dictionary.model.Word
import com.tt.it_dictionary.repository.WordRepository
import com.tt.it_dictionary.ui.WordDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class RemindWordReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val endCalendar = intent.getSerializableExtra("endCalendar") as Calendar
        val now = Calendar.getInstance()
        endCalendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
        endCalendar.set(Calendar.MONTH, now.get(Calendar.MONTH))
        endCalendar.set(Calendar.YEAR, now.get(Calendar.YEAR))

        if (now < endCalendar) {
            val scope = GlobalScope
            val wordRoomDatabase =
                WordRoomDatabase.getDatabase(
                    context,
                    scope
                )
            val wordRepository = WordRepository(wordRoomDatabase.wordDao())

            scope.launch(Dispatchers.IO) {
                val favWord: Word? = wordRepository.getRandomFavoriteWord()

                if (favWord != null) {
                    val intent1 = Intent(context, WordDetail::class.java).apply {
                        this.putExtra("en", favWord.en)
                        this.putExtra("vn", favWord.vn)
                        this.putExtra("wordId", favWord.id)
                    }

                    val pendingIntent =
                        PendingIntent.getActivity(
                            context,
                            1,
                            intent1,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                    createNotificationChannel("REMIND_WORD", "Remind word", "Remind word", context)

                    val builder = NotificationCompat.Builder(context, "REMIND_WORD")
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(favWord.en)
                        .setContentText(favWord.vn)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(context)) {
                        // notificationId is a unique int for each notification that you must define
                        notify(System.currentTimeMillis().toInt(), builder.build())
                    }
                }
            }
        }
    }

    private fun createNotificationChannel(
        channelId: String,
        description: String,
        channelName: String,
        context: Context
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
