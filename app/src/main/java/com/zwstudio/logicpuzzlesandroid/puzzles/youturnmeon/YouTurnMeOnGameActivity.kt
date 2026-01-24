package com.zwstudio.logicpuzzlesandroid.puzzles.youturnmeon

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class YouTurnMeOnGameActivity : GameGameActivity<YouTurnMeOnGame, YouTurnMeOnDocument, YouTurnMeOnGameMove, YouTurnMeOnGameState>() {
    private val document: YouTurnMeOnDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = YouTurnMeOnGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, YouTurnMeOnHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        YouTurnMeOnGame(level.layout, this, doc)
}