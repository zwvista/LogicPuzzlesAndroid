package com.zwstudio.logicpuzzlesandroid.puzzles.islandconnections

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class IslandConnectionsGameActivity : GameGameActivity<IslandConnectionsGame, IslandConnectionsDocument, IslandConnectionsGameMove, IslandConnectionsGameState>() {
    private val document: IslandConnectionsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = IslandConnectionsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, IslandConnectionsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        IslandConnectionsGame(level.layout, this, doc)
}