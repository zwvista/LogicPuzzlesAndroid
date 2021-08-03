package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class LineSweeperGameActivity : GameGameActivity<LineSweeperGame, LineSweeperDocument, LineSweeperGameMove, LineSweeperGameState>() {
    private val document: LineSweeperDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = LineSweeperGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, LineSweeperHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        LineSweeperGame(level.layout, this, doc)
}