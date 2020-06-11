package com.zwstudio.logicpuzzlesandroid.home.android

import android.widget.CheckedTextView
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById
import java.sql.SQLException

@EActivity(R.layout.activity_home_options)
open class HomeOptionsActivity : BaseActivity() {
    fun doc(): HomeDocument? {
        return app!!.homeDocument
    }

    @ViewById
    var ctvPlayMusic: CheckedTextView? = null

    @ViewById
    var ctvPlaySound: CheckedTextView? = null
    var rec: HomeGameProgress? = null

    @AfterViews
    protected fun init() {
        rec = doc()!!.gameProgress()
        ctvPlayMusic!!.isChecked = rec!!.playMusic
        ctvPlaySound!!.isChecked = rec!!.playSound
    }

    @Click
    protected fun ctvPlayMusic() {
        ctvPlayMusic!!.isChecked = !rec!!.playMusic
        rec!!.playMusic = !rec!!.playMusic
        try {
            app!!.daoHomeGameProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        app!!.soundManager!!.playOrPauseMusic()
    }

    @Click
    protected fun ctvPlaySound() {
        ctvPlaySound!!.isChecked = !rec!!.playSound
        rec!!.playSound = !rec!!.playSound
        try {
            app!!.daoHomeGameProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    @Click
    protected fun btnDone() {
        finish()
    }

    @Click
    protected fun btnDefault() {
        yesNoDialog("Do you really want to reset the options?") {
            rec!!.playMusic = true
            rec!!.playSound = true
            try {
                app!!.daoHomeGameProgress!!.update(rec)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            ctvPlayMusic!!.isChecked = rec!!.playMusic
            app!!.soundManager!!.playOrPauseMusic()
            ctvPlaySound!!.isChecked = rec!!.playSound
        }
    }
}