package com.zwstudio.logicpuzzlesandroid.puzzles.snakeomino

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SnakeominoMainActivity : GameMainActivity<SnakeominoGame, SnakeominoDocument, SnakeominoGameMove, SnakeominoGameState>() {
    private val document: SnakeominoDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SnakeominoOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SnakeominoGameActivity::class.java))
    }
}

class SnakeominoOptionsActivity : GameOptionsActivity<SnakeominoGame, SnakeominoDocument, SnakeominoGameMove, SnakeominoGameState>() {
    private val document: SnakeominoDocument by inject()
    override val doc get() = document
}

class SnakeominoHelpActivity : GameHelpActivity<SnakeominoGame, SnakeominoDocument, SnakeominoGameMove, SnakeominoGameState>() {
    private val document: SnakeominoDocument by inject()
    override val doc get() = document
}
