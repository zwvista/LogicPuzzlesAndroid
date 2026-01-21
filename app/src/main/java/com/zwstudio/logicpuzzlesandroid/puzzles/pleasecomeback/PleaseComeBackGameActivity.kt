package com.zwstudio.logicpuzzlesandroid.puzzles.pleasecomeback

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PleaseComeBackGameActivity : GameGameActivity<PleaseComeBackGame, PleaseComeBackDocument, PleaseComeBackGameMove, PleaseComeBackGameState>() {
    private val document: PleaseComeBackDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PleaseComeBackGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PleaseComeBackHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PleaseComeBackGame(level.layout, this, doc)
}