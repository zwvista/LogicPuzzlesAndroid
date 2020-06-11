package com.zwstudio.logicpuzzlesandroid.home.android

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.widget.Button
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.BaseActivity
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_home_main)
open class HomeMainActivity : BaseActivity() {
    fun doc(): HomeDocument? {
        return app!!.homeDocument
    }

    @ViewById
    protected var btnResumeGame: Button? = null

    @AfterViews
    protected fun init() {
        // http://www.vogella.com/tutorials/AndroidMedia/article.html#tutorial_soundpool
        // Set the hardware buttons to control the music
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onResume() {
        super.onResume()
        btnResumeGame!!.text = "Resume Game " + doc()!!.gameProgress()!!.gameTitle
    }

    @Click
    protected fun btnResumeGame() {
        resumeGame(doc()!!.gameProgress()!!.gameName, doc()!!.gameProgress()!!.gameTitle, true)
    }

    @Click
    protected fun btnChooseGame() {
        HomeChooseGameActivity_.intent(this).startForResult(CHOOSE_GAME_REQUEST)
    }

    @Click
    protected fun btnOptions() {
        HomeOptionsActivity_.intent(this).start()
    }

    private fun resumeGame(gameName: String?, gameTitle: String?, toResume: Boolean) {
        doc()!!.resumeGame(gameName, gameTitle)
        var cls: Class<*>? = null
        try {
            cls = Class.forName(String.format("com.zwstudio.logicpuzzlesandroid.puzzles.%s.android.%sMainActivity_",
                gameName!!.toLowerCase(), gameName))
            val intent = Intent(this, cls)
            intent.putExtra("toResume", toResume)
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    @OnActivityResult(CHOOSE_GAME_REQUEST)
    fun onResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) btnResumeGame()
    }

    companion object {
        const val CHOOSE_GAME_REQUEST = 123
    }
}