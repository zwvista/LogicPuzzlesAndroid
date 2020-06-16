package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.widget.Button
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity
import org.androidannotations.annotations.*
import java.util.*

@EActivity(R.layout.activity_home_main)
class HomeMainActivity : BaseActivity() {
    fun doc() = app.homeDocument

    @ViewById
    lateinit var btnResumeGame: Button

    @AfterViews
    protected fun init() {
        // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
        // Set the hardware buttons to control the music
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onResume() {
        super.onResume()
        btnResumeGame.text = "Resume Game " + doc().gameProgress().gameTitle
    }

    @Click
    protected fun btnResumeGame() {
        resumeGame(doc().gameProgress().gameName, doc().gameProgress().gameTitle, true)
    }

    @Click
    protected fun btnChooseGame() {
        HomeChooseGameActivity_.intent(this).startForResult(CHOOSE_GAME_REQUEST)
    }

    @Click
    protected fun btnOptions() {
        HomeOptionsActivity_.intent(this).start()
    }

    private fun resumeGame(gameName: String, gameTitle: String, toResume: Boolean) {
        doc().resumeGame(gameName, gameTitle)
        val cls = Class.forName("com.zwstudio.logicpuzzlesandroid.puzzles.${gameName.toLowerCase(Locale.ROOT)}.${gameName}MainActivity_")
        val intent = Intent(this, cls)
        intent.putExtra("toResume", toResume)
        startActivity(intent)
    }

    @OnActivityResult(CHOOSE_GAME_REQUEST)
    fun onResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) btnResumeGame()
    }

    companion object {
        const val CHOOSE_GAME_REQUEST = 123
    }
}