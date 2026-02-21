package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadblocks

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CrossroadBlocksGameActivity : GameGameActivity<CrossroadBlocksGame, CrossroadBlocksDocument, CrossroadBlocksGameMove, CrossroadBlocksGameState>() {
    private val document: CrossroadBlocksDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CrossroadBlocksGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CrossroadBlocksHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CrossroadBlocksGame(level.layout, this, doc)
}