package com.easyfield.utils.extension

import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Date

fun FragmentActivity.showDatePicker(
    today: Date = Date(),
    allowFutureDate: Boolean = true,
    handleClick: (onDateSelected: Long) -> Unit
) {
    MaterialDatePicker.Builder.datePicker()
        .apply {
            setTitleText(today.formatToServerDateDefaults())
            if (!allowFutureDate) {
                setCalendarConstraints(
                    CalendarConstraints.Builder().apply {
                        setValidator(DateValidatorPointBackward.now())
                    }.build()
                )
            }
            build().apply {
                show(supportFragmentManager, "DATE_PICKER")
                addOnPositiveButtonClickListener {
                    handleClick(it)
                }
            }
        }
}

fun FragmentActivity.showTimePicker(
    handleClick: (viewFormat: String, aiFormat: String) -> Unit
) {

    MaterialTimePicker.Builder().apply {
        setTimeFormat(TimeFormat.CLOCK_12H)
        setTitleText("Select visit time")
        build().apply {
            show(supportFragmentManager, "time_picker")
            addOnPositiveButtonClickListener {

                var am_pm = ""
                var newHours = hour
                when {
                    hour == 0 -> {
                        newHours = hour + 12
                        am_pm = "AM"
                    }

                    hour == 12 -> am_pm = "PM"
                    hour > 12 -> {
                        newHours = hour - 12
                        am_pm = "PM"
                    }

                    else -> am_pm = "AM"
                }
                val hour = if (newHours < 10) "0$newHours" else newHours
                val min = if (minute < 10) "0$minute" else minute

                handleClick("$hour:$min $am_pm", "$hour:$min:00")
            }
        }
    }
}

