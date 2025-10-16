package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrossing

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NumberCrossingGameActivity : GameGameActivity<NumberCrossingGame, NumberCrossingDocument, NumberCrossingGameMove, NumberCrossingGameState>() {
    private val document: NumberCrossingDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NumberCrossingGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NumberCrossingHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NumberCrossingGame(level.layout, this, doc)
}