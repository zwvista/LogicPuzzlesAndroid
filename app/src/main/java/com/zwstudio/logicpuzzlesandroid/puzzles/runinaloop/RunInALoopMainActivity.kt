package com.zwstudio.logicpuzzlesandroid.puzzles.runinaloop

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class RunInALoopMainActivity : GameMainActivity<RunInALoopGame, RunInALoopDocument, RunInALoopGameMove, RunInALoopGameState>() {
    private val document: RunInALoopDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, RunInALoopOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, RunInALoopGameActivity::class.java))
    }
}

class RunInALoopOptionsActivity : GameOptionsActivity<RunInALoopGame, RunInALoopDocument, RunInALoopGameMove, RunInALoopGameState>() {
    private val document: RunInALoopDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class RunInALoopHelpActivity : GameHelpActivity<RunInALoopGame, RunInALoopDocument, RunInALoopGameMove, RunInALoopGameState>() {
    private val document: RunInALoopDocument by inject()
    override val doc get() = document
}