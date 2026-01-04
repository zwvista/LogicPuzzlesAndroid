package com.zwstudio.logicpuzzlesandroid.puzzles.runinaloop

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class RunInALoopGameActivity : GameGameActivity<RunInALoopGame, RunInALoopDocument, RunInALoopGameMove, RunInALoopGameState>() {
    private val document: RunInALoopDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = RunInALoopGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, RunInALoopHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        RunInALoopGame(level.layout, this, doc)
}