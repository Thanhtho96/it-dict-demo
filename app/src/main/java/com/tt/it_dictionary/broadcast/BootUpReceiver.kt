package com.tt.it_dictionary.broadcast

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.tt.it_dictionary.ui.SettingActivity

class BootUpReceiver : BroadcastReceiver() {
    private var alarmManager: AlarmManager? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val isRemind =
                PreferenceManager.getDefaultSharedPreferences(context).getBoolean("isRemind", false)
            if (isRemind) {
                alarmManager =
                    context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                SettingActivity.setRemind(
                    context,
                    alarmManager
                )
            }
        }
    }
}
