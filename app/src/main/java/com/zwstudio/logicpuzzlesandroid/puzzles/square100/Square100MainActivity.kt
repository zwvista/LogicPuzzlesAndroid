package com.zwstudio.logicpuzzlesandroid.puzzles.square100

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class Square100MainActivity : GameMainActivity<Square100Game, Square100Document, Square100GameMove, Square100GameState>() {
    private val document: Square100Document by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, Square100OptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, Square100GameActivity::class.java))
    }
}

class Square100OptionsActivity : GameOptionsActivity<Square100Game, Square100Document, Square100GameMove, Square100GameState>() {
    private val document: Square100Document by inject()
    override val doc get() = document
}

class Square100HelpActivity : GameHelpActivity<Square100Game, Square100Document, Square100GameMove, Square100GameState>() {
    private val document: Square100Document by inject()
    override val doc get() = document
}