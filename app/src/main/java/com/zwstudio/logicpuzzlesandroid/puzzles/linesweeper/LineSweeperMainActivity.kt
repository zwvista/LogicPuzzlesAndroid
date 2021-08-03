package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class LineSweeperMainActivity : GameMainActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    private val document: LineSweeperDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, LineSweeperOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, LineSweeperGameActivity::class.java))
    }
}

class LineSweeperOptionsActivity : GameOptionsActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    private val document: LineSweeperDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class LineSweeperHelpActivity : GameHelpActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    private val document: LineSweeperDocument by inject()
    override val doc get() = document
}