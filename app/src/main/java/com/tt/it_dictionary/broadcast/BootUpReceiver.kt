package com.tt.it_dictionary.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class BootUpReceiver : BroadcastReceiver() {
    private var alarmManager: AlarmManager? = null
    private val alarmRequestCode = 77
    private lateinit var sharePref: SharedPreferences
    private lateinit var sdf: SimpleDateFormat
    private var startCalendar: Calendar = Calendar.getInstance()
    private var endCalendar: Calendar = Calendar.getInstance()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            sharePref = PreferenceManager.getDefaultSharedPreferences(context)
            val isRemind =
                sharePref.getBoolean("isRemind", false)
            if (isRemind) {
                val myFormat = "HH:mm" //In which you need put here
                sdf = SimpleDateFormat(myFormat, Locale.getDefault())

                alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

                val startTime = sdf.parse(sharePref.getString("startTime", "07:00")!!)!!
                val endTime = sdf.parse(sharePref.getString("endTime", "17:00")!!)!!
                startCalendar.time = startTime
                endCalendar.time = endTime

                val interval =
                    (endCalendar.timeInMillis - startCalendar.timeInMillis) / sharePref.getInt(
                        "frequency",
                        1
                    )

                val myIntent = Intent(context, RemindWordReceiver::class.java)
                myIntent.putExtra("endCalendar", endCalendar)

                val pendingIntent = PendingIntent.getBroadcast(
                    context, alarmRequestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager?.setExactAndAllowWhileIdle(
                        AlarmManager.RTC,
                        interval,
                        pendingIntent
                    )
                }
                alarmManager?.setRepeating(
                    AlarmManager.RTC,
                    startCalendar.timeInMillis,
                    interval,
                    pendingIntent
                )
            }
        }
    }
}
