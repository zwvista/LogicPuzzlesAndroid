package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PouringWaterGameActivity : GameGameActivity<PouringWaterGame, PouringWaterDocument, PouringWaterGameMove, PouringWaterGameState>() {
    private val document: PouringWaterDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PouringWaterGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PouringWaterHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PouringWaterGame(level.layout, this, doc)
}