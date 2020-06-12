package com.zwstudio.logicpuzzlesandroid.common.android

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EActivity

// http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
@EActivity
abstract class BaseActivity : AppCompatActivity() {
    @App
    lateinit var app: LogicPuzzlesApplication
    override fun onStart() {
        super.onStart()
        app.soundManager.activityStarted()
    }

    override fun onStop() {
        app.soundManager.activityStopped()
        super.onStop()
    }

    protected fun yesNoDialog(message: CharSequence, yesAction: () -> Unit) {
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
}