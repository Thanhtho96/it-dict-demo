package com.tt.it_dictionary.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.tt.it_dictionary.broadcast.RemindWordReceiver
import com.tt.it_dictionary.databinding.ActivitySettingBinding
import org.koin.android.ext.android.inject
import java.util.*

class SettingActivity : AppCompatActivity() {
    private val sharePref: SharedPreferences by inject()
    private lateinit var btnBack: ImageButton
    private lateinit var checkBox: CheckBox
    private var alarmManager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySettingBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            btnBack = it.backButton
            checkBox = it.checkboxRemind
        }
        btnBack.setOnClickListener { onBackPressed() }

        alarmManager =
            getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        val isRemind = sharePref.getBoolean("isRemind", false)
        checkBox.isChecked = isRemind

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) return@setOnCheckedChangeListener
            sharePref.edit().putBoolean("isRemind", isChecked).apply()
            if (isChecked) {
                setRemind(this, alarmManager)
            } else {
                val myIntent = Intent(this, RemindWordReceiver::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(
                        this, alarmRequestCode, myIntent,
                        PendingIntent.FLAG_NO_CREATE
                    )
                if (pendingIntent != null && alarmManager != null) {
                    alarmManager?.cancel(pendingIntent)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        private val alarmRequestCode = 77

        fun setRemind(context: Context, alarmManager: AlarmManager?) {
            // Todo: let user set time for this thing
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 12)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val cur: Calendar = Calendar.getInstance()
            if (cur.after(calendar)) {
                calendar.add(Calendar.DATE, 1)
            }
            val myIntent = Intent(context, RemindWordReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, alarmRequestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
}
