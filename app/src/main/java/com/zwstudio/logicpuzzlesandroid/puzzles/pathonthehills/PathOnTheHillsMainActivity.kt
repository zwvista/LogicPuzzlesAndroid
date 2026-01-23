package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthehills

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class PathOnTheHillsMainActivity : GameMainActivity<PathOnTheHillsGame, PathOnTheHillsDocument, PathOnTheHillsGameMove, PathOnTheHillsGameState>() {
    private val document: PathOnTheHillsDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, PathOnTheHillsOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, PathOnTheHillsGameActivity::class.java))
    }
}

class PathOnTheHillsOptionsActivity : GameOptionsActivity<PathOnTheHillsGame, PathOnTheHillsDocument, PathOnTheHillsGameMove, PathOnTheHillsGameState>() {
    private val document: PathOnTheHillsDocument by inject()
    override val doc get() = document

    protected fun onDefault() {}
}

class PathOnTheHillsHelpActivity : GameHelpActivity<PathOnTheHillsGame, PathOnTheHillsDocument, PathOnTheHillsGameMove, PathOnTheHillsGameState>() {
    private val document: PathOnTheHillsDocument by inject()
    override val doc get() = document
}