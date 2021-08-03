package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SentinelsGameActivity : GameGameActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState>() {
    private val document: SentinelsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SentinelsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SentinelsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SentinelsGame(level.layout, this, doc)
}