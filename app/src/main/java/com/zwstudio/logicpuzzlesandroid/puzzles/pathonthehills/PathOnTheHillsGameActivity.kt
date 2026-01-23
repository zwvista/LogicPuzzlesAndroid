package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthehills

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PathOnTheHillsGameActivity : GameGameActivity<PathOnTheHillsGame, PathOnTheHillsDocument, PathOnTheHillsGameMove, PathOnTheHillsGameState>() {
    private val document: PathOnTheHillsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PathOnTheHillsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PathOnTheHillsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PathOnTheHillsGame(level.layout, this, doc)
}