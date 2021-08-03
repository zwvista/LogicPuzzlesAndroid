package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink

import android.content.Intent
import android.os.Bundle
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.home.android.SoundManager
import org.koin.android.ext.android.inject

class NumberLinkGameActivity : GameGameActivity<NumberLinkGame, NumberLinkDocument, NumberLinkGameMove, NumberLinkGameState>() {
    private val document: NumberLinkDocument by inject()
    private val soundManager: SoundManager by inject()
    override val doc get() = document

    override fun onCreate(savedInstanceState: Bundle?) {
        gameView = NumberLinkGameView(this, soundManager)
        super.onCreate(savedInstanceState)
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this, NumberLinkHelpActivity::class.java))
        }
    }

    override fun newGame(level: GameLevel) =
        NumberLinkGame(level.layout, this, doc)
}