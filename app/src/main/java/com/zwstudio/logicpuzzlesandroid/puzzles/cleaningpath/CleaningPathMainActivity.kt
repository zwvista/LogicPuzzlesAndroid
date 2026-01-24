package com.zwstudio.logicpuzzlesandroid.puzzles.cleaningpath

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class CleaningPathMainActivity : GameMainActivity<CleaningPathGame, CleaningPathDocument, CleaningPathGameMove, CleaningPathGameState>() {
    private val document: CleaningPathDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, CleaningPathOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, CleaningPathGameActivity::class.java))
    }
}

class CleaningPathOptionsActivity : GameOptionsActivity<CleaningPathGame, CleaningPathDocument, CleaningPathGameMove, CleaningPathGameState>() {
    private val document: CleaningPathDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class CleaningPathHelpActivity : GameHelpActivity<CleaningPathGame, CleaningPathDocument, CleaningPathGameMove, CleaningPathGameState>() {
    private val document: CleaningPathDocument by inject()
    override val doc get() = document
}