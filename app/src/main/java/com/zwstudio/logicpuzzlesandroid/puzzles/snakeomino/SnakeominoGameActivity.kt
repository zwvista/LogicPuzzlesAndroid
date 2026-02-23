package com.zwstudio.logicpuzzlesandroid.puzzles.snakeomino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SnakeominoGameActivity : GameGameActivity<SnakeominoGame, SnakeominoDocument, SnakeominoGameMove, SnakeominoGameState>() {
    private val document: SnakeominoDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SnakeominoGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SnakeominoHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SnakeominoGame(level.layout, this, doc)
}
