package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PowerGridGameActivity : GameGameActivity<PowerGridGame, PowerGridDocument, PowerGridGameMove, PowerGridGameState>() {
    private val document: PowerGridDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PowerGridGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PowerGridHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PowerGridGame(level.layout, this, doc)
}