package com.zwstudio.logicpuzzlesandroid.puzzles.pointing

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class PointingGameActivity : GameGameActivity<PointingGame, PointingDocument, PointingGameMove, PointingGameState>() {
    private val document: PointingDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = PointingGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, PointingHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        PointingGame(level.layout, this, doc)
}
