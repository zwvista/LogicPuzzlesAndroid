package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class HedgeMazeMainActivity : GameMainActivity<HedgeMazeGame, HedgeMazeDocument, HedgeMazeGameMove, HedgeMazeGameState>() {
    private val document: HedgeMazeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, HedgeMazeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, HedgeMazeGameActivity::class.java))
    }
}

class HedgeMazeOptionsActivity : GameOptionsActivity<HedgeMazeGame, HedgeMazeDocument, HedgeMazeGameMove, HedgeMazeGameState>() {
    private val document: HedgeMazeDocument by inject()
    override val doc get() = document
}

class HedgeMazeHelpActivity : GameHelpActivity<HedgeMazeGame, HedgeMazeDocument, HedgeMazeGameMove, HedgeMazeGameState>() {
    private val document: HedgeMazeDocument by inject()
    override val doc get() = document
}