package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SnakeMazeGameActivity : GameGameActivity<SnakeMazeGame, SnakeMazeDocument, SnakeMazeGameMove, SnakeMazeGameState>() {
    private val document: SnakeMazeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SnakeMazeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SnakeMazeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SnakeMazeGame(level.layout, this, doc)
}
