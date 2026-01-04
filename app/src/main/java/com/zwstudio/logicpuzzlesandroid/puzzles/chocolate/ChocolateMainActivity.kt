package com.zwstudio.logicpuzzlesandroid.puzzles.chocolate

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class ChocolateMainActivity : GameMainActivity<ChocolateGame, ChocolateDocument, ChocolateGameMove, ChocolateGameState>() {
    private val document: ChocolateDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, ChocolateOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, ChocolateGameActivity::class.java))
    }
}

class ChocolateOptionsActivity : GameOptionsActivity<ChocolateGame, ChocolateDocument, ChocolateGameMove, ChocolateGameState>() {
    private val document: ChocolateDocument by inject()
    override val doc get() = document
}

class ChocolateHelpActivity : GameHelpActivity<ChocolateGame, ChocolateDocument, ChocolateGameMove, ChocolateGameState>() {
    private val document: ChocolateDocument by inject()
    override val doc get() = document
}