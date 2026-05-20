package com.zwstudio.logicpuzzlesandroid.puzzles.slantedmaze

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.koin.android.ext.android.inject

class SlantedMazeMainActivity : GameMainActivity<SlantedMazeGame, SlantedMazeDocument, SlantedMazeGameMove, SlantedMazeGameState>() {
    private val document: SlantedMazeDocument by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnOptions.setOnClickListener {
            startActivity(Intent(this, SlantedMazeOptionsActivity::class.java))
        }
    }

    override fun resumeGame() {
        doc.resumeGame()
        startActivity(Intent(this, SlantedMazeGameActivity::class.java))
    }
}

class SlantedMazeOptionsActivity : GameOptionsActivity<SlantedMazeGame, SlantedMazeDocument, SlantedMazeGameMove, SlantedMazeGameState>() {
    private val document: SlantedMazeDocument by inject()
    override val doc get() = document
}

class SlantedMazeHelpActivity : GameHelpActivity<SlantedMazeGame, SlantedMazeDocument, SlantedMazeGameMove, SlantedMazeGameState>() {
    private val document: SlantedMazeDocument by inject()
    override val doc get() = document
}
