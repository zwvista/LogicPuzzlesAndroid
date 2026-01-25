package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class FingerPointingGameActivity : GameGameActivity<FingerPointingGame, FingerPointingDocument, FingerPointingGameMove, FingerPointingGameState>() {
    private val document: FingerPointingDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = FingerPointingGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, FingerPointingHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        FingerPointingGame(level.layout, this, doc)
}