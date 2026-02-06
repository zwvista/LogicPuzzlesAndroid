package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class CrosstownTrafficGameActivity : GameGameActivity<CrosstownTrafficGame, CrosstownTrafficDocument, CrosstownTrafficGameMove, CrosstownTrafficGameState>() {
    private val document: CrosstownTrafficDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = CrosstownTrafficGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, CrosstownTrafficHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        CrosstownTrafficGame(level.layout, this, doc)
}