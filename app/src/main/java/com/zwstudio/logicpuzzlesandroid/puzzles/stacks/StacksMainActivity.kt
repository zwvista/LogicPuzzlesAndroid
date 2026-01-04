package com.zwstudio.logicpuzzlesandroid.puzzles.stacks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class StacksMainActivity : GameMainActivity<StacksGame, StacksDocument, StacksGameMove, StacksGameState>() {
    private val document: StacksDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, StacksOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, StacksGameActivity::class.java))
    }
}

class StacksOptionsActivity : GameOptionsActivity<StacksGame, StacksDocument, StacksGameMove, StacksGameState>() {
    private val document: StacksDocument by inject()
    override val doc get() = document
}

class StacksHelpActivity : GameHelpActivity<StacksGame, StacksDocument, StacksGameMove, StacksGameState>() {
    private val document: StacksDocument by inject()
    override val doc get() = document
}