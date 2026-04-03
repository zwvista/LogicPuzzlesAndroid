package com.zwstudio.logicpuzzlesandroid.puzzles.snakeislands

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SnakeIslandsGameActivity : GameGameActivity<SnakeIslandsGame, SnakeIslandsDocument, SnakeIslandsGameMove, SnakeIslandsGameState>() {
    private val document: SnakeIslandsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SnakeIslandsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SnakeIslandsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SnakeIslandsGame(level.layout, this, doc)
}