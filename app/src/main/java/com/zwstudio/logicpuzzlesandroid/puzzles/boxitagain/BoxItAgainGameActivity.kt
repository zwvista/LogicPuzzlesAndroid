package com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class BoxItAgainGameActivity : GameGameActivity<BoxItAgainGame, BoxItAgainDocument, BoxItAgainGameMove, BoxItAgainGameState>() {
    private val document: BoxItAgainDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = BoxItAgainGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, BoxItAgainHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        BoxItAgainGame(level.layout, this, doc)
}