package com.zwstudio.logicpuzzlesandroid.puzzles.turnmeup

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class TurnMeUpGameActivity : GameGameActivity<TurnMeUpGame, TurnMeUpDocument, TurnMeUpGameMove, TurnMeUpGameState>() {
    private val document: TurnMeUpDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = TurnMeUpGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, TurnMeUpHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        TurnMeUpGame(level.layout, this, doc)
}