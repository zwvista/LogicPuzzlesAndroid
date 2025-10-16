package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class ThermometersGameActivity : GameGameActivity<ThermometersGame, ThermometersDocument, ThermometersGameMove, ThermometersGameState>() {
    private val document: ThermometersDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = ThermometersGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, ThermometersHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel): ThermometersGame {
        val onlyOneArrow = (level.settings["OnlyOneArrow"] ?: "0") == "1"
        return ThermometersGame(level.layout, onlyOneArrow, this, doc)
    }
}