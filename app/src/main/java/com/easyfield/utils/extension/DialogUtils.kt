package com.easyfield.utils.extension


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.easyfield.R
import com.easyfield.utils.ItemClickAdapter

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal


fun Context.alert(
    @StyleRes style: Int = 0,
    dialogBuilder: MaterialAlertDialogBuilder.() -> Unit
) {
    MaterialAlertDialogBuilder(this, style)
        .apply {
            setCancelable(true)
            dialogBuilder()
            create()
            show()
        }
}
fun Fragment.setProgressDialog(context:Context): Dialog {
    val dialog = Dialog(context)
    val inflate = LayoutInflater.from(context).inflate(R.layout.row_progress, null)
    dialog.setContentView(inflate)
    dialog.setCancelable(false)
    dialog.window!!.setBackgroundDrawable(
        ColorDrawable(Color.WHITE)
    )
    return dialog
}

fun Activity.setProgressDialog(context:Context): Dialog {
    val dialog = Dialog(context)
    val inflate = LayoutInflater.from(context).inflate(R.layout.row_progress, null)
    dialog.setContentView(inflate)
    dialog.window!!.setBackgroundDrawable(
        ColorDrawable(Color.WHITE)
    )
    return dialog
}
fun Fragment.noInternet(listener: ItemClickAdapter){

    NoInternetDialogSignal.Builder(
        requireActivity(),
        lifecycle
    ).apply {
        dialogProperties.apply {
            connectionCallback = object : ConnectionCallback { // Optional
                override fun hasActiveConnection(hasActiveConnection: Boolean) {

                   if(hasActiveConnection){
                       listener.onItemClick(0,"")
                   }


                }
            }


            cancelable = false // Optional
            noInternetConnectionTitle = "No Internet" // Optional
            noInternetConnectionMessage =
                "Check your Internet connection and try again." // Optional
            showInternetOnButtons = true // Optional
            pleaseTurnOnText = "Please turn on" // Optional
            wifiOnButtonText = "Wifi" // Optional
            mobileDataOnButtonText = "Mobile data" // Optional

            onAirplaneModeTitle = "No Internet" // Optional
            onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
            pleaseTurnOffText = "Please turn off" // Optional
            airplaneModeOffButtonText = "Airplane mode" // Optional
            showAirplaneModeOffButtons = true // Optional
        }
    }.build()
}
fun Fragment.showAlertDialog(message:String, listener: ItemClickAdapter){
    val mview = LayoutInflater.from(requireActivity())
        .inflate(R.layout.row_confirmation, null, false)
    val dialog= MaterialAlertDialogBuilder(requireContext())
        .setView(mview).create()

    val txtNameHint = mview.findViewById<TextView>(R.id.txtNameHint)

    val txtOK = mview.findViewById<TextView>(R.id.txtOK)
    val txtCancel = mview.findViewById<TextView>(R.id.txtCancel)


    txtNameHint.setText(message)

    txtOK!!.setOnClickListener {

        listener.onItemClick(0,"OK")
        dialog.dismiss()
    }
    txtCancel!!.setOnClickListener {

        dialog.dismiss()
    }
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    dialog.show()
}

fun MaterialAlertDialogBuilder.negativeButton(
    text: String = "No",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setNegativeButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.positiveButton(
    text: String = "Yes",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setPositiveButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.neutralButton(
    text: String = "OK",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setNeutralButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.items(
    items: Array<String> = arrayOf(),
    handleClick: (dialogInterface: DialogInterface, which: Int) -> Unit = { dialog: DialogInterface, _: Int -> dialog.dismiss() }
) {
    this.setItems(items) { dialogInterface, which -> handleClick(dialogInterface, which) }
}
