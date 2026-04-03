package com.zwstudio.logicpuzzlesandroid.puzzles.snakeislands

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SnakeIslandsMainActivity : GameMainActivity<SnakeIslandsGame, SnakeIslandsDocument, SnakeIslandsGameMove, SnakeIslandsGameState>() {
    private val document: SnakeIslandsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SnakeIslandsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SnakeIslandsGameActivity::class.java))
    }
}

class SnakeIslandsOptionsActivity : GameOptionsActivity<SnakeIslandsGame, SnakeIslandsDocument, SnakeIslandsGameMove, SnakeIslandsGameState>() {
    private val document: SnakeIslandsDocument by inject()
    override val doc get() = document
}

class SnakeIslandsHelpActivity : GameHelpActivity<SnakeIslandsGame, SnakeIslandsDocument, SnakeIslandsGameMove, SnakeIslandsGameState>() {
    private val document: SnakeIslandsDocument by inject()
    override val doc get() = document
}