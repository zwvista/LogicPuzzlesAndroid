package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ParksGameActivity : GameGameActivity<ParksGame, ParksDocument, ParksGameMove, ParksGameState>() {
    private val document: ParksDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ParksGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ParksHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel): ParksGame {
        val treesInEachArea = (level.settings["TreesInEachArea"] ?: "1").toInt()
        return ParksGame(level.layout, treesInEachArea, this, doc)
    }
}