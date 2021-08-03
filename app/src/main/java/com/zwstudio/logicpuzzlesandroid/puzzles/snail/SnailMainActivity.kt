package com.zwstudio.logicpuzzlesandroid.puzzles.snail

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SnailMainActivity : GameMainActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    private val document: SnailDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SnailOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SnailGameActivity::class.java))
    }
}

class SnailOptionsActivity : GameOptionsActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    private val document: SnailDocument by inject()
    override val doc get() = document
}

class SnailHelpActivity : GameHelpActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    private val document: SnailDocument by inject()
    override val doc get() = document
}