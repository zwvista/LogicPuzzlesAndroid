package com.zwstudio.logicpuzzlesandroid.home.android

import android.widget.CheckedTextView
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_home_options)
open class HomeOptionsActivity : BaseActivity() {
    fun doc() = app.homeDocument

    @ViewById
    lateinit var ctvPlayMusic: CheckedTextView

    @ViewById
    lateinit var ctvPlaySound: CheckedTextView
    
    lateinit var rec: HomeGameProgress

    @AfterViews
    protected fun init() {
        rec = doc().gameProgress()
        ctvPlayMusic.isChecked = rec.playMusic
        ctvPlaySound.isChecked = rec.playSound
    }

    @Click
    protected fun ctvPlayMusic() {
        ctvPlayMusic.isChecked = !rec.playMusic
        rec.playMusic = !rec.playMusic
        app.daoHomeGameProgress.update(rec)
        app.soundManager.playOrPauseMusic()
    }

    @Click
    protected fun ctvPlaySound() {
        ctvPlaySound.isChecked = !rec.playSound
        rec.playSound = !rec.playSound
        app.daoHomeGameProgress.update(rec)
    }

    @Click
    protected fun btnDone() {
        finish()
    }

    @Click
    protected fun btnDefault() {
        yesNoDialog("Do you really want to reset the options?") {
            rec.playMusic = true
            rec.playSound = true
            app.daoHomeGameProgress.update(rec)
            ctvPlayMusic.isChecked = rec.playMusic
            app.soundManager.playOrPauseMusic()
            ctvPlaySound.isChecked = rec.playSound
        }
    }
}