package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class WallSentinelsGameActivity : GameGameActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    private val document: WallSentinelsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = WallSentinelsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, WallSentinelsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        WallSentinelsGame(level.layout, this, doc)
}
