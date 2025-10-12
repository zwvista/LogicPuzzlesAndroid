package com.zwstudio.logicpuzzlesandroid.puzzles.hidoku

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class HidokuGameActivity : GameGameActivity<HidokuGame, HidokuDocument, HidokuGameMove, HidokuGameState>() {
    private val document: HidokuDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = HidokuGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, HidokuHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        HidokuGame(level.layout, this, doc)
}
