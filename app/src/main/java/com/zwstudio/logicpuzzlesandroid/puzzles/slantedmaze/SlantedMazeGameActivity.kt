package com.zwstudio.logicpuzzlesandroid.puzzles.slantedmaze

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class SlantedMazeGameActivity : GameGameActivity<SlantedMazeGame, SlantedMazeDocument, SlantedMazeGameMove, SlantedMazeGameState>() {
    private val document: SlantedMazeDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = SlantedMazeGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, SlantedMazeHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        SlantedMazeGame(level.layout, this, doc)
}
