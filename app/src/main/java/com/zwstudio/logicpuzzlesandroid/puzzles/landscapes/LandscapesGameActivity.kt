package com.zwstudio.logicpuzzlesandroid.puzzles.landscapes

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LandscapesGameActivity : GameGameActivity<LandscapesGame, LandscapesDocument, LandscapesGameMove, LandscapesGameState>() {
    private val document: LandscapesDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LandscapesGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LandscapesHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel): LandscapesGame {
        val sum = (level.settings["sum"] ?: "10").toInt()
        return LandscapesGame(level.layout, sum, this, doc)
    }
}