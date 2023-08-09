
package com.matdata.utils.extensions

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.matdata.R
import java.util.*


//common method to show alert dialog
fun Context.showAlertDialog(message: String = "", style: Int = R.style.AlertDialogTheme, title: String = this.getString(R.string.alert), positiveBtnText: String = this.getString(R.string.ok), negativeBtnText: String = this.getString(R.string.cancel), handleClick: (positiveClick: Boolean) -> Unit = {}, isCancelable: Boolean = true) {

    val builder = MaterialAlertDialogBuilder(this, style)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(isCancelable)
    builder.setPositiveButton(positiveBtnText) { dialog, which ->
        dialog.dismiss()
        handleClick(true)
    }
    if (negativeBtnText.isNotEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()
            handleClick(false)

        }
    }
    val dialog = builder.create()
    dialog.show()

}


@SuppressLint("RestrictedApi")
fun View.openDatePickerDialog(setMinDate: Boolean = false, selectedDate: Long = 0, setMaxDate: Boolean = false, setMaxYear: Int = 0, onDateSelect: (calendar: Calendar) -> Unit) {
//    val calendar = Calendar.getInstance()
//
//    val datePickerDialog = MaterialStyledDatePickerDialog(this.context, { view, year, monthOfYear, dayOfMonth ->
//        val selectedCal = Calendar.getInstance()
//        selectedCal.set(Calendar.YEAR, year)
//        selectedCal.set(Calendar.MONTH, monthOfYear)
//        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//        onDateSelect(selectedCal)
//    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//
////    datePickerDialog.datePicker.minDate = selectedtDate - 1000
//    if (setMinDate) {
//        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
//    }
//
//    if (setMaxDate) {
//        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
//        if (setMaxYear != 0) {
//            val cal = Calendar.getInstance()
//            cal.add(Calendar.YEAR, - setMaxYear)
//            datePickerDialog.datePicker.maxDate = cal.timeInMillis - 1000
//
//        }
//    }
//
//
//    datePickerDialog.show()

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(this.context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val selectedCal = Calendar.getInstance()
                selectedCal.set(Calendar.YEAR, year)
                selectedCal.set(Calendar.MONTH, monthOfYear)
                selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                onDateSelect(selectedCal)


            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    if (setMinDate) {
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    if (setMaxDate) {
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        if (setMaxYear != 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -setMaxYear)
            datePickerDialog.datePicker.maxDate = cal.timeInMillis - 1000

        }
    }


    datePickerDialog.show()
    datePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundResource(0)
    datePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
    datePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundResource(0)
    datePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))

}


fun View.openTimePickerDialog(calendar: Calendar = Calendar.getInstance(), is24Hour: Boolean = false, onTimeSelect: (calendar: Calendar) -> Unit) {
    val timePickerDialog = TimePickerDialog(this.context,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                onTimeSelect(calendar)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24Hour)

    timePickerDialog.show()
    timePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundResource(0)
    timePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
    timePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundResource(0)
    timePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))

}


fun Context.showSingleChoiceAlertDialog(dataArray: Array<String>, style: Int = R.style.AlertDialogTheme, selectedPosition: Int = - 1, title: String = this.getString(R.string.alert), negativeBtnText: String = this.getString(R.string.cancel), handleClick: (selectedPosition: Int) -> Unit = {}, isCancelable: Boolean = true) {
    val builder = MaterialAlertDialogBuilder(this, style)
    builder.setTitle(title)
    builder.setSingleChoiceItems(dataArray, selectedPosition) { dialog, which ->
        dialog?.dismiss()
        handleClick(which)
    }

    builder.setCancelable(isCancelable)
    if (! negativeBtnText.isEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()

        }
    }
    val dialog = builder.create()
    dialog.show()

}

fun Context.showMultiChoiceAlertDialog(dataArray: Array<String>, style: Int = R.style.AlertDialogTheme, selectedArray: BooleanArray, title: String = this.getString(R.string.alert), negativeBtnText: String = this.getString(R.string.cancel), positiveBtnText: String = this.getString(R.string.ok), handleClick: (selectedArray: BooleanArray) -> Unit = {}, isCancelable: Boolean = true) {
    val builder = MaterialAlertDialogBuilder(this, style)
    builder.setTitle(title)

    builder.setMultiChoiceItems(dataArray, selectedArray) { dialog, which, isChecked ->
        selectedArray[which] = isChecked
    }

    builder.setPositiveButton(positiveBtnText) { dialog, which ->
        handleClick(selectedArray)
        dialog.dismiss()
    }

    builder.setCancelable(isCancelable)
    if (negativeBtnText.isNotEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()

        }
    }
    val dialog = builder.create()
    dialog.show()
}
