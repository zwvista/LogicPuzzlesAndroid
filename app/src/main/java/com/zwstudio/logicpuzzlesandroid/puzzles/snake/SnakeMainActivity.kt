package com.zwstudio.logicpuzzlesandroid.puzzles.snake

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SnakeMainActivity : GameMainActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    private val document: SnakeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SnakeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SnakeGameActivity::class.java))
    }
}

class SnakeOptionsActivity : GameOptionsActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    private val document: SnakeDocument by inject()
    override val doc get() = document
}

class SnakeHelpActivity : GameHelpActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState>() {
    private val document: SnakeDocument by inject()
    override val doc get() = document
}