package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NeighboursGameActivity : GameGameActivity<NeighboursGame, NeighboursDocument, NeighboursGameMove, NeighboursGameState>() {
    private val document: NeighboursDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NeighboursGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NeighboursHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NeighboursGame(level.layout, this, doc)
}