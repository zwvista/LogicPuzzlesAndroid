package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TurnTwiceGameActivity : GameGameActivity<TurnTwiceGame, TurnTwiceDocument, TurnTwiceGameMove, TurnTwiceGameState>() {
    private val document: TurnTwiceDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TurnTwiceGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TurnTwiceHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TurnTwiceGame(level.layout, this, doc)
}