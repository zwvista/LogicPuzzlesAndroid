package com.zwstudio.logicpuzzlesandroid.puzzles.straightandturn

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class StraightAndTurnGameActivity : GameGameActivity<StraightAndTurnGame, StraightAndTurnDocument, StraightAndTurnGameMove, StraightAndTurnGameState>() {
    private val document: StraightAndTurnDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = StraightAndTurnGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, StraightAndTurnHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        StraightAndTurnGame(level.layout, this, doc)
}