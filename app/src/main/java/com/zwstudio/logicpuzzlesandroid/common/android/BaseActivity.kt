package com.zwstudio.logicpuzzlesandroid.common.android

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

fun Activity.yesNoDialog(message: CharSequence, yesAction: () -> Unit) {
    // http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> yesAction()  //Yes button clicked
            DialogInterface.BUTTON_NEGATIVE -> {}
        }
    }
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
        .setNegativeButton("No", dialogClickListener).show()
}
