package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TapAlikeGameActivity : GameGameActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState>() {
    private val document: TapAlikeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TapAlikeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TapAlikeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TapAlikeGame(level.layout, this, doc)
}