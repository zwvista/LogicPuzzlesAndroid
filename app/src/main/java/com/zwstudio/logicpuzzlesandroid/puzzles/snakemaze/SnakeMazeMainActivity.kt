package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SnakeMazeMainActivity : GameMainActivity<SnakeMazeGame, SnakeMazeDocument, SnakeMazeGameMove, SnakeMazeGameState>() {
    private val document: SnakeMazeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SnakeMazeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SnakeMazeGameActivity::class.java))
    }
}

class SnakeMazeOptionsActivity : GameOptionsActivity<SnakeMazeGame, SnakeMazeDocument, SnakeMazeGameMove, SnakeMazeGameState>() {
    private val document: SnakeMazeDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class SnakeMazeHelpActivity : GameHelpActivity<SnakeMazeGame, SnakeMazeDocument, SnakeMazeGameMove, SnakeMazeGameState>() {
    private val document: SnakeMazeDocument by inject()
    override val doc get() = document
}