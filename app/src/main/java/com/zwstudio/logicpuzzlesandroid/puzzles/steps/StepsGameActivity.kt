package com.zwstudio.logicpuzzlesandroid.puzzles.steps

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class StepsGameActivity : GameGameActivity<StepsGame, StepsDocument, StepsGameMove, StepsGameState>() {
    private val document: StepsDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = StepsGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, StepsHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        StepsGame(level.layout, this, doc)
}