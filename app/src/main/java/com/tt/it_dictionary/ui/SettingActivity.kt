package com.tt.it_dictionary.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tt.it_dictionary.R
import com.tt.it_dictionary.broadcast.RemindWordReceiver
import com.tt.it_dictionary.databinding.ActivitySettingBinding
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class SettingActivity : AppCompatActivity() {
    private val sharePref: SharedPreferences by inject()
    private var alarmManager: AlarmManager? = null
    private lateinit var btnBack: ImageButton
    private lateinit var checkBox: CheckBox
    private lateinit var tvOften: TextView
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var linearLayout: LinearLayout
    private lateinit var startTimeLinearLayout: LinearLayout
    private lateinit var endTimeLinearLayout: LinearLayout
    private lateinit var oftenLinearLayout: LinearLayout
    private var startCalendar: Calendar = Calendar.getInstance()
    private var endCalendar: Calendar = Calendar.getInstance()
    private lateinit var sdf: SimpleDateFormat
    private lateinit var startTime: Date
    private lateinit var endTime: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySettingBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            btnBack = it.backButton
            checkBox = it.checkboxRemind
            linearLayout = it.remindWordChildLayout
            startTimeLinearLayout = it.layoutStartTime
            endTimeLinearLayout = it.layoutEndTime
            oftenLinearLayout = it.layoutOften
            tvOften = it.textViewOftenRemind
            tvStartTime = it.textViewStartTime
            tvEndTime = it.textViewEndTime
        }
        btnBack.setOnClickListener { onBackPressed() }

        alarmManager =
            getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        initTextValue()
        listenToRemindCheckBox()
        listenToFrequencyReminder()

        val startDateListener =
            listenToStartDate()

        val endDateListener =
            listenToEndDate()

        showTimePicker(startDateListener, endDateListener)
    }

    private fun listenToEndDate(): TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endCalendar.set(Calendar.MINUTE, minute)
            if (endCalendar > startCalendar) {
                updateLabel(tvEndTime, endCalendar).apply {
                    sharePref.edit().putString("endTime", this).apply()
                    endTime = sdf.parse(this)!!
                }
                cancelRemind(this, alarmManager)
                setRemind(this, alarmManager, startCalendar, endCalendar, sharePref)
            } else {
                Toast.makeText(this, "End date must after start date", Toast.LENGTH_SHORT)
                    .show()
            }
            endCalendar.time = endTime
        }
    }

    private fun listenToStartDate(): TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startCalendar.set(Calendar.MINUTE, minute)
            if (startCalendar < endCalendar) {
                updateLabel(tvStartTime, startCalendar).apply {
                    sharePref.edit().putString("startTime", this).apply()
                    startTime = sdf.parse(this)!!
                }
                cancelRemind(this, alarmManager)
                setRemind(this, alarmManager, startCalendar, endCalendar, sharePref)
            } else {
                Toast.makeText(this, "Start date must before end date", Toast.LENGTH_SHORT)
                    .show()
            }
            startCalendar.time = startTime
        }
    }

    private fun listenToFrequencyReminder() {
        oftenLinearLayout.setOnClickListener {
            val dialogFragment = RemindDialogFragment.newInstance(sharePref.getInt("frequency", 1))
            dialogFragment.show(supportFragmentManager, "dialogFragment")
            dialogFragment.setOnItemClickListener(object : RemindDialogFragment.OnClickListener {
                override fun onItemClick(position: Int) {
                    tvOften.text = resources.getQuantityString(
                        R.plurals.plural_time,
                        position, position
                    )
                    sharePref.edit().putInt("frequency", position).apply()
                    cancelRemind(this@SettingActivity, alarmManager)
                    setRemind(
                        this@SettingActivity,
                        alarmManager,
                        startCalendar,
                        endCalendar,
                        sharePref
                    )
                }
            })
        }
    }

    private fun listenToRemindCheckBox() {
        val isRemind = sharePref.getBoolean("isRemind", false)
        checkBox.isChecked = isRemind
        if (checkBox.isChecked) {
            linearLayout.visibility = View.VISIBLE
        } else {
            linearLayout.visibility = View.GONE
        }

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) return@setOnCheckedChangeListener
            sharePref.edit().putBoolean("isRemind", isChecked).apply()
            if (isChecked) {
                linearLayout.visibility = View.VISIBLE
                setRemind(this, alarmManager, startCalendar, endCalendar, sharePref)
            } else {
                linearLayout.visibility = View.GONE
                cancelRemind(this, alarmManager)
            }
        }
    }

    private fun initTextValue() {
        val myFormat = "HH:mm" //In which you need put here
        sdf = SimpleDateFormat(myFormat, Locale.getDefault())

        startTime = sdf.parse(sharePref.getString("startTime", "07:00")!!)!!
        endTime = sdf.parse(sharePref.getString("endTime", "17:00")!!)!!
        tvOften.text = resources.getQuantityString(
            R.plurals.plural_time,
            sharePref.getInt("frequency", 1),
            sharePref.getInt("frequency", 1)
        )

        startCalendar.time = startTime
        endCalendar.time = endTime
        updateLabel(tvStartTime, startCalendar)
        updateLabel(tvEndTime, endCalendar)
    }

    private fun updateLabel(textView: TextView, calendar: Calendar): String {
        return sdf.format(calendar.time).also {
            textView.text = it
        }
    }

    private fun showTimePicker(
        startDateListener: TimePickerDialog.OnTimeSetListener,
        endDateListener: TimePickerDialog.OnTimeSetListener
    ) {
        startTimeLinearLayout.setOnClickListener {
            showTimePickerDialog(startDateListener, startCalendar)
        }

        endTimeLinearLayout.setOnClickListener {
            showTimePickerDialog(endDateListener, endCalendar)
        }
    }

    private fun showTimePickerDialog(date: TimePickerDialog.OnTimeSetListener, calendar: Calendar) {
        TimePickerDialog(
            this,
            date,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    companion object {
        @JvmStatic
        private val alarmRequestCode = 77

        fun setRemind(
            context: Context,
            alarmManager: AlarmManager?,
            startCalendar: Calendar,
            endCalendar: Calendar,
            sharePref: SharedPreferences
        ) {
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

        fun cancelRemind(context: Context, alarmManager: AlarmManager?) {
            val myIntent = Intent(context, RemindWordReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context, alarmRequestCode, myIntent,
                    PendingIntent.FLAG_NO_CREATE
                )
            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }
}
