package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwarden

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TrafficWardenGameActivity : GameGameActivity<TrafficWardenGame, TrafficWardenDocument, TrafficWardenGameMove, TrafficWardenGameState>() {
    private val document: TrafficWardenDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TrafficWardenGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TrafficWardenHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TrafficWardenGame(level.layout, this, doc)
}